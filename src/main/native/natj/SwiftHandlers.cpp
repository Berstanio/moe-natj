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
                    if (info->needsStructRewrite) {
                        void* structPointer = values[0];
                        memmove(values, &values[1], (info->cif.nargs - 1) * sizeof(void*));
                        values[info->cif.nargs - 1] = structPointer;

                        // Caching rewritten cif?
                        ffi_cif rewritten_cif = info->cif;
                        // memcpy(&rewritten_cif, &info->cif, sizeof(ffi_cif));
                        ffi_type** args_old = info->cif.arg_types;

                        ffi_type* args_new[info->cif.nargs];
                        memcpy(args_new, &args_old[1], (info->cif.nargs - 1) * sizeof(ffi_type*));
                        args_new[info->cif.nargs - 1] = args_old[0];
                        rewritten_cif.arg_types = args_new;
                        ffi_call(&rewritten_cif, (void (*)())info->swiftFunction, value, values);
                    } else {
                        ffi_call(&info->cif, (void (*)())info->swiftFunction, value, values);
                    }
                } else {
                    void* function;
                    if (info->isProtocol) {
                        uint64_t ec = **(uint64_t**)values;
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

void swiftToJavaHandler(ffi_cif* cif, void* result, void** args, void* user) {
    void* swiftObjectPointer = NULL;
    void* swiftObjectPointerPointer = (void*)&swiftObjectPointer;

    asm("mov %[swiftObjectPointer], x20"
        : [swiftObjectPointer] "=r"(swiftObjectPointer));

    ToJavaCallInfo* info = (ToJavaCallInfo*)user;
    ATTACH_ENV();

    // Build cache if needed
    buildInfos(env, info->method, true, &info->paramInfos, &info->returnInfo, NULL, NULL, NULL, true, info->isProtocolCall);

    void* jargs[3];
    jargs[0] = &env;

    jobject* self = (jobject*)malloc(8);
    // Convert self value to Java
    ValueConverter<kToJava>(
        { .env = env,
            .nvalues = 1,
            .types = cif->arg_types,
            .values = &swiftObjectPointerPointer,
            .infos = info->paramInfos },
        [self](unsigned n, ffi_type** types, void** values) {
            memcpy(self, values[0], ffi_type_pointer.size);
        });

    jargs[1] = self;
    jargs[2] = &info->methodId;

    void* value = ALIGN(alloca(info->cif.rtype->size + info->cif.rtype->alignment - 1), info->cif.rtype->alignment);

    ValueConverter<kToJava>(
        { .env = env,
            .nvalues = cif->nargs - 1,
            .types = &cif->arg_types[1],
            .values = &args[0],
            .infos = &info->paramInfos[1],
            .promote = true,
            .preTypes = info->cif.arg_types,
            .preValues = jargs,
            .preNumber = 3 },
        [value, info](unsigned n, ffi_type** types, void** values) {
            ffi_call(&info->cif, (void (*)())info->jniFunction, value, values);
        });

    // TODO: Handle java exception

    if (&ffi_type_void != cif->rtype) {

        // Convert native value to Java
        ValueConverter<kToNative>(
            { .env = env,
                .nvalues = 1,
                .types = &cif->rtype,
                .values = &value,
                .infos = &info->returnInfo },
            [result, cif](unsigned n, ffi_type** types, void** values) {
                memcpy(result, values[0], cif->rtype->size);
            });
    }

    free(self);

    DETACH_ENV();
}
