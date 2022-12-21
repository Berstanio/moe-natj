#include "SwiftRuntime.h"
#include "SwiftHandlers.h"

static jobject gRuntime = NULL;

jclass gSwiftStaticMethod = NULL;
jclass gSwiftConstructor = NULL;
jclass gSwiftVirtualMethod = NULL;

jmethodID gSwiftMethodSymbolMethod = NULL;

jmethodID gSwiftMethodOffsetMethod = NULL;

jobject getSwiftRuntime() { return gRuntime; }

void JNICALL Java_org_moe_natj_swift_SwiftRuntime_initialize(JNIEnv* env, jclass clazz, jobject instance) {
    gRuntime = env->NewGlobalRef(instance);

    env->PushLocalFrame(2);

    gSwiftStaticMethod = (jclass)env->NewGlobalRef(env->FindClass("org/moe/natj/swift/ann/StaticSwiftMethod"));
    gSwiftVirtualMethod = (jclass)env->NewGlobalRef(env->FindClass("org/moe/natj/swift/ann/VirtualSwiftMethod"));

    gSwiftConstructor = (jclass)env->NewGlobalRef(env->FindClass("org/moe/natj/swift/ann/SwiftConstructor"));

    gSwiftMethodSymbolMethod = env->GetMethodID(gSwiftStaticMethod, "symbol", "()Ljava/lang/String;");
    gSwiftMethodOffsetMethod = env->GetMethodID(gSwiftVirtualMethod, "offset", "()J");
    env->PopLocalFrame(NULL);
}

void JNICALL Java_org_moe_natj_swift_SwiftRuntime_registerClass(JNIEnv* env, jclass clazz, jclass type) {
    jobjectArray methods = (jobjectArray)env->CallObjectMethod(type, gGetDeclaredMethodsMethod);
    jsize length = env->GetArrayLength(methods);

    for (size_t i = 0; i < length; i++) {
        jobject method = env->GetObjectArrayElement(methods, i);
        jobject swiftMethodAnnotation = env->CallObjectMethod(method, gGetAnnotationMethod, gSwiftStaticMethod);
        jobject swiftVirtualMethodAnnotation = env->CallObjectMethod(method, gGetAnnotationMethod, gSwiftVirtualMethod);
        void* symbol = NULL;
        uint64_t offset = 0;
        if (swiftMethodAnnotation) {
            jstring symbolString = (jstring)env->CallObjectMethod(swiftMethodAnnotation, gSwiftMethodSymbolMethod);
            const char* symbolName = env->GetStringUTFChars(symbolString, NULL);
            symbol = dlsym(RTLD_DEFAULT, symbolName);
            if (symbol == NULL) {
                std::cout << "Symbol " << symbolName << " not found!" << std::endl;
                continue;
            }
        } else if (swiftVirtualMethodAnnotation) {
            offset = env->CallLongMethod(swiftVirtualMethodAnnotation, gSwiftMethodOffsetMethod);
        } else {
            continue;
        }

        jobjectArray parametersJava = (jobjectArray)env->CallObjectMethod(method, gGetParameterTypesMethod);
        jsize parameterCount = env->GetArrayLength(parametersJava);
        
        jobjectArray parameterAnns = (jobjectArray)env->CallObjectMethod(method, gGetParameterAnnotationsMethod);


        jint modifiers = env->CallIntMethod(method, gGetModifiersMethod);
        bool isStatic = modifiers & ACC_STATIC;

        ffi_type** parametersSwift = new ffi_type*[parameterCount + !isStatic];

        if (!isStatic) {
            parametersSwift[0] = &ffi_type_pointer;
        }

        ffi_type** parametersFFI = new ffi_type*[parameterCount + 2];
        parametersFFI[0] = &ffi_type_pointer;
        parametersFFI[1] = &ffi_type_pointer;

        for (jsize i = 0; i < parameterCount; i++) {
            jclass parameterJava = (jclass)env->GetObjectArrayElement(parametersJava, i);
            jobjectArray paramAnns = (jobjectArray)env->GetObjectArrayElement(parameterAnns, i);
            jsize annCount = env->GetArrayLength(paramAnns);
            
            bool isByValue = false;
            for (jsize j = 0; j < annCount; j++) {
                jclass paramAnn = (jclass)env->GetObjectArrayElement(paramAnns, j);
                if (env->IsInstanceOf(paramAnn, gByValueClass)) {
                    isByValue = true;
                }
            }

            parametersFFI[i + 2] = getFFIType(env, parameterJava, false);
            parametersSwift[i + !isStatic] = getFFIType(env, parameterJava, isByValue);
        }

        jobject returnByValueAnnotation = env->CallObjectMethod(method, gGetAnnotationMethod, gByValueClass);
        jclass returnJava = (jclass)env->CallObjectMethod(method, gGetReturnTypeMethod);
        ffi_type* returnFFI = getFFIType(env, returnJava, returnByValueAnnotation != NULL);

        ToNativeCallInfo* info = new ToNativeCallInfo;
        info->method = env->NewGlobalRef(method);
        info->swiftFunction = symbol;
        info->variadic = kNotVariadic;
        info->cached = false;
        info->isStatic = isStatic;
        info->offset = offset;
        ffi_prep_cif(&info->cif, FFI_DEFAULT_ABI, parameterCount + !isStatic, returnFFI, parametersSwift);

        jobject swiftConstructorAnnotation = env->CallObjectMethod(method, gGetAnnotationMethod, gSwiftConstructor);
        // SOLVE BETTER
        if (!isStatic || swiftConstructorAnnotation) {
            // Hack, to support x20 registers
            info->cif.flags = info->cif.flags | 256;
        }

        ffi_cif* closureCif = new ffi_cif;
        void* code = NULL;
        ffi_closure* closure = (ffi_closure*)ffi_closure_alloc(sizeof(ffi_closure), &code);

        ffi_prep_cif(closureCif, FFI_DEFAULT_ABI, parameterCount + 2, returnFFI, parametersFFI);

        ffi_prep_closure_loc(closure, closureCif, javaToSwiftHandler, info, code);

        // Register method
        jstring methodName = (jstring)env->CallObjectMethod(method, gGetMethodNameMethod);
        const char* methodCName = env->GetStringUTFChars(methodName, NULL);
        jstring methodDesc = (jstring)env->CallStaticObjectMethod(gAsmTypeClass, gGetMethodDescriptorStaticMethod, method);
        const char* methodCDesc = env->GetStringUTFChars(methodDesc, NULL);
        JNINativeMethod nativeMethod;
        nativeMethod.name = methodCName;
        nativeMethod.signature = methodCDesc;
        nativeMethod.fnPtr = code;
        env->RegisterNatives(type, &nativeMethod, 1);
    }
}
