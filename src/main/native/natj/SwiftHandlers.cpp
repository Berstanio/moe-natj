#include "SwiftHandlers.h"

void javaToSwiftHandler(ffi_cif* cif, void* result, void** args, void* user) {
    ToNativeCallInfo* info = (ToNativeCallInfo*)user;
    JNIEnv* env = *(JNIEnv**)args[0];
    // Build cache if needed
    buildInfos(env, info->method, false, &info->paramInfos, &info->returnInfo, &info->variadic, NULL, NULL, !info->isStatic);

    void* value = ALIGN(alloca(info->cif.rtype->size + info->cif.rtype->alignment - 1), info->cif.rtype->alignment);
    ValueConverter<kToNative>(
        { .env = env,
            .nvalues = info->cif.nargs,
            .types = info->cif.arg_types,
            .values = &args[2 - !info->isStatic],
            .infos = info->paramInfos,
            .variadic = info->variadic,
            .promote = false,
            .runtime = getSwiftRuntime() },
        [value, info](unsigned n, ffi_type** types, void** values) {
            if (info->variadic == kNotVariadic) {
                if (info->swiftFunction) {
                    ffi_call(&info->cif, (void (*)())info->swiftFunction, value, values);
                } else {
                    void* function;
                    if (info->isProtocol) {
                        uint64_t ec = **(uint64_t**) values;
                        uint64_t pwt = *(uint64_t*)(ec + 32);
                        function = *(void**)(pwt + info->offset);
                    } else {
                        uint64_t metadata = ***(uint64_t***)values;
                        function = *(void**)(metadata + info->offset);
                    }
                    ffi_call(&info->cif, (void (*)())function, value, values);
                }
            } else {
                ffi_cif cif;
                ffi_prep_cif_var(&cif, info->cif.abi, info->cif.nargs, n, info->cif.rtype, types);
                ffi_call(&cif, (void (*)())info->swiftFunction, value, values);
            }
        });

    if (&ffi_type_void != cif->rtype) {
        // Convert native value to Java
        ValueConverter<kToJava>(
            { .env = env,
                .nvalues = 1,
                .types = &info->cif.rtype,
                .values = &value,
                .infos = &info->returnInfo },
            [result, cif](unsigned n, ffi_type** types, void** values) {
                memcpy(result, values[0], cif->rtype->size);
            });
    }
}
