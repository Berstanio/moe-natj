#include "SwiftRuntime.h"
#include "CRuntime.h"
#include "SwiftHandlers.h"
#include <unordered_map>

static jobject gRuntime = NULL;

jclass gSwiftRuntimeClass = NULL;

jclass gSwiftStaticMethod = NULL;
jclass gSwiftConstructor = NULL;
jclass gSwiftVirtualMethod = NULL;
jclass gSwiftBindingClass = NULL;

jmethodID gSwiftMethodSymbolMethod = NULL;
jmethodID gSwiftProtocolDescriptorMethod = NULL;

jmethodID gSwiftMethodOffsetMethod = NULL;
jmethodID gSwiftRuntimeRegisterProtocol = NULL;
jmethodID gSwiftRuntimeRegisterMetadata = NULL;
jmethodID gSwiftRuntimeGetMetadata = NULL;
jmethodID gSwiftRuntimeGetPWT = NULL;
jmethodID gSwiftRuntimeGetAllInheritedInterfaces = NULL;
jmethodID gSwiftRuntimeRegisterPWT = NULL;
jmethodID gSwiftRuntimeFindOriginMethods = NULL;

jobject getSwiftRuntime() { return gRuntime; }

std::unordered_map<jmethodID, ToNativeCallInfo*>* protocolMap = NULL;

void* dereferencePeer(void** peer) {
    return *peer;
}

#define TYPED_PROTOCOL_CALL_FORWARDER(type, name, type_ffi)                                                                                                     \
    type Java_org_moe_natj_swift_SwiftRuntime_forward##name##ProtocolCall(JNIEnv* env, jclass clazz, jclass protocolClass, jobject method, jobjectArray args) { \
        ffi_cif cif;                                                                                                                                            \
        jsize length = env->GetArrayLength(args) + 1;                                                                                                           \
        void* arg_values[length];                                                                                                                               \
        jobject objects[length - 1];                                                                                                                            \
        for (jsize i = 0; i < length - 1; i++) {                                                                                                                \
            objects[i] = env->GetObjectArrayElement(args, i);                                                                                                   \
        }                                                                                                                                                       \
        for (jsize i = 1; i < length; i++) {                                                                                                                    \
            arg_values[i] = &objects[i - 1];                                                                                                                    \
        }                                                                                                                                                       \
        arg_values[0] = &env;                                                                                                                                   \
        type result;                                                                                                                                            \
        ToNativeCallInfo* info = (*protocolMap)[env->FromReflectedMethod(method)];                                                                              \
        cif.rtype = &type_ffi;                                                                                                                                  \
        javaToSwiftHandler(&cif, &result, (void**)&arg_values, (void*)info);                                                                                    \
        return result;                                                                                                                                          \
    }

TYPED_PROTOCOL_CALL_FORWARDER(jboolean, Boolean, ffi_type_uint8)
TYPED_PROTOCOL_CALL_FORWARDER(jbyte, Byte, ffi_type_sint8)
TYPED_PROTOCOL_CALL_FORWARDER(jchar, Char, ffi_type_uint16)
TYPED_PROTOCOL_CALL_FORWARDER(jshort, Short, ffi_type_sint16)
TYPED_PROTOCOL_CALL_FORWARDER(jint, Int, ffi_type_sint32)
TYPED_PROTOCOL_CALL_FORWARDER(jlong, Long, ffi_type_sint64)
TYPED_PROTOCOL_CALL_FORWARDER(jfloat, Float, ffi_type_float)
TYPED_PROTOCOL_CALL_FORWARDER(jdouble, Double, ffi_type_double)
TYPED_PROTOCOL_CALL_FORWARDER(jobject, Object, ffi_type_pointer)

#undef TYPED_BLOCK_CALL_FORWARDER

void JNICALL Java_org_moe_natj_swift_SwiftRuntime_forwardVoidProtocolCall(JNIEnv* env, jclass clazz, jclass protocolClass, jobject method, jobjectArray args) {
    ffi_cif cif;
    jsize length = env->GetArrayLength(args) + 1;
    void* arg_values[length];
    jobject objects[length - 1];
    for (jsize i = 0; i < length - 1; i++) {
        objects[i] = env->GetObjectArrayElement(args, i);
    }
    for (jsize i = 1; i < length; i++) {
        arg_values[i] = &objects[i - 1];
    }
    arg_values[0] = &env;

    ToNativeCallInfo* info = (*protocolMap)[env->FromReflectedMethod(method)];
    cif.rtype = &ffi_type_void;
    javaToSwiftHandler(&cif, NULL, (void**)&arg_values, (void*)info);
}

void JNICALL Java_org_moe_natj_swift_SwiftRuntime_initialize(JNIEnv* env, jclass clazz, jobject instance) {
    gRuntime = env->NewGlobalRef(instance);
    gSwiftRuntimeClass = (jclass)env->NewGlobalRef(clazz);
    gSwiftRuntimeRegisterProtocol = env->GetStaticMethodID(gSwiftRuntimeClass, "registerProtocolClass", "(Ljava/lang/Class;J)V");
    gSwiftRuntimeRegisterMetadata = env->GetStaticMethodID(gSwiftRuntimeClass, "registerMetadataPointer", "(Ljava/lang/Class;J)V");
    gSwiftRuntimeGetMetadata = env->GetStaticMethodID(gSwiftRuntimeClass, "getClosestMetadataPointerFromInheritedClass", "(Ljava/lang/Class;)J");
    gSwiftRuntimeGetPWT = env->GetStaticMethodID(gSwiftRuntimeClass, "getProtocolWitnessTable", "(JLjava/lang/Class;)J");
    gSwiftRuntimeRegisterPWT = env->GetStaticMethodID(gSwiftRuntimeClass, "registerProtocolWitnessTable", "(JLjava/lang/Class;J)V");
    gSwiftRuntimeGetAllInheritedInterfaces = env->GetStaticMethodID(gSwiftRuntimeClass, "getAllInheritedInterfaces", "(Ljava/lang/Class;)[Ljava/lang/Class;");
    gSwiftRuntimeFindOriginMethods = env->GetStaticMethodID(gSwiftRuntimeClass, "findOriginMethods", "(Ljava/lang/reflect/Method;)[Ljava/lang/reflect/Method;");
    gSwiftProtocolDescriptorMethod = env->GetMethodID(gSwiftProtocolAnnotationClass, "protocolDescriptor", "()Ljava/lang/String;");
    env->PushLocalFrame(2);

    gSwiftStaticMethod = (jclass)env->NewGlobalRef(env->FindClass("org/moe/natj/swift/ann/StaticSwiftMethod"));
    gSwiftVirtualMethod = (jclass)env->NewGlobalRef(env->FindClass("org/moe/natj/swift/ann/VirtualSwiftMethod"));

    gSwiftConstructor = (jclass)env->NewGlobalRef(env->FindClass("org/moe/natj/swift/ann/SwiftConstructor"));
    gSwiftBindingClass = (jclass)env->NewGlobalRef(env->FindClass("org/moe/natj/swift/ann/SwiftBindingClass"));

    gSwiftMethodSymbolMethod = env->GetMethodID(gSwiftStaticMethod, "symbol", "()Ljava/lang/String;");
    gSwiftMethodOffsetMethod = env->GetMethodID(gSwiftVirtualMethod, "offset", "()J");
    env->PopLocalFrame(NULL);
    protocolMap = new std::unordered_map<jmethodID, ToNativeCallInfo*>();
}

#define set_at_offset(ptr, type, offset, value) \
    (*((type*)((uintptr_t)(ptr) + (offset))) = (value))

#define get_at_offset(ptr, offset) \
    (void*)(((uintptr_t)(ptr) + (offset)))

void createPWTForDirectClass(JNIEnv* env, jclass type, void* metadata) {
    jobjectArray interfaces = (jobjectArray) env->CallObjectMethod(type, gGetClassInterfacesMethod);
    jsize interfaceCount = env->GetArrayLength(interfaces);
    for (jsize i = 0; i < interfaceCount; i++) {
        jclass interface = (jclass) env->GetObjectArrayElement(interfaces, i);
        jobjectArray methods = (jobjectArray)env->CallObjectMethod(interface, gGetDeclaredMethodsMethod);
        jsize length = env->GetArrayLength(methods);
        void* pwt = malloc(length * 8 + 8);
        env->CallStaticObjectMethod(gSwiftRuntimeClass, gSwiftRuntimeRegisterPWT, (jlong) metadata, interface, (jlong) pwt);
    }
}

void* generateEmptyStructMetadataPointer(JNIEnv* env, jclass type) {
    void* metadata = malloc(3 * 8);
    metadata = get_at_offset(metadata, 8);
    void* vwt = dlsym(RTLD_DEFAULT, "$sytWV");
    set_at_offset(metadata, void*, -8, vwt);
    set_at_offset(metadata, uint64_t, 0, 512);
    
    // Proper implement
    set_at_offset(metadata, void*, 0, NULL);
    return metadata;
}

void* generateClassMetadataPointer(JNIEnv* env, jclass type, bool* isClass) {
    void* closestMetadata = (void*)env->CallStaticLongMethod(gSwiftRuntimeClass, gSwiftRuntimeGetMetadata, type);
    if (closestMetadata == 0) {
        *isClass = false;
        return generateEmptyStructMetadataPointer(env, type);
    }
    size_t metadataSize = 104 + 16; // Find out the size :( // Also, use swift runtime methods, e.g. swift_allocateGenericClassMetadata maybe?
    void* newMetadata = malloc(metadataSize); // get real values
    memcpy(newMetadata, get_at_offset(closestMetadata, -16), metadataSize);
    newMetadata = get_at_offset(newMetadata, 16);

    // offset 0 => metaclass pointer

    // offset 8 = metadata* parent
    set_at_offset(newMetadata, uintptr_t, 8, (uintptr_t)closestMetadata);

    // offset 32 => some data (__DATA__) + 2
    size_t dataSize = 9 * 8;
    void* data = malloc(dataSize); // Seems to be fixed size
    memcpy(data, *(void**)get_at_offset(newMetadata, 32), dataSize);
    jstring className = (jstring)env->CallObjectMethod(type, gGetClassNameMethod);
    const char* classNameC = env->GetStringUTFChars(className, NULL);
    set_at_offset(data, const char*, 24, classNameC);
    set_at_offset(data, uintptr_t, 48, 0);

    set_at_offset(newMetadata, void*, 32, data);
    // offset 64 => nominal type descriptor

    // Create PWT's
    jobjectArray interfaces = (jobjectArray) env->CallStaticObjectMethod(gSwiftRuntimeClass, gSwiftRuntimeGetAllInheritedInterfaces, type);
    jsize interfaceCount = env->GetArrayLength(interfaces);
    for (jsize i = 0; i < interfaceCount; i++) {
        jclass interface = (jclass) env->GetObjectArrayElement(interfaces, i);
        void* pwt = (void*) env->CallStaticLongMethod(gSwiftRuntimeClass, gSwiftRuntimeGetPWT, closestMetadata, interface);
        env->CallStaticObjectMethod(gSwiftRuntimeClass, gSwiftRuntimeRegisterPWT, (jlong) newMetadata, type, (jlong) pwt);
    }
    
    return newMetadata;
}

void registerNativeMethod(JNIEnv* env, jclass type, jobject method, bool isStatic, bool isStructure, bool isProtocolClass, void* fnPtr, uint64_t offset) {

    jobjectArray parametersJava = (jobjectArray)env->CallObjectMethod(method, gGetParameterTypesMethod);
    jsize parameterCount = env->GetArrayLength(parametersJava);

    jobjectArray parameterAnns = (jobjectArray)env->CallObjectMethod(method, gGetParameterAnnotationsMethod);

    ffi_type** parametersSwift = new ffi_type*[parameterCount + !isStatic];

    bool needsStructRewrite = false;

    if (!isStatic) {
        parametersSwift[0] = getFFIType(env, type, isStructure);
        needsStructRewrite = isStructure && parametersSwift[0]->size <= 32;
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
        ffi_type* parameterFFISwift = getFFIType(env, parameterJava, isByValue);
        if (env->IsAssignableFrom(parameterJava, gStringClass))
            parameterFFISwift = swiftString;
        parametersSwift[i + !isStatic] = parameterFFISwift;
    }

    jobject returnByValueAnnotation = env->CallObjectMethod(method, gGetAnnotationMethod, gByValueClass);
    jclass returnJava = (jclass)env->CallObjectMethod(method, gGetReturnTypeMethod);
    bool isProtocolReturn = env->CallBooleanMethod(returnJava, gIsAnnotationPresentMethod, gSwiftProtocolAnnotationClass);

    if (isProtocolReturn) {
        // We register a class that can return a plain protocol. It can be, that it returns something, we don't have bindings for. Therefor we need to register the protocol class now too
        Java_org_moe_natj_swift_SwiftRuntime_registerClass(env, gSwiftRuntimeClass, returnJava);
    }
    ffi_type* returnFFI = getFFIType(env, returnJava, false);
    ffi_type* returnFFISwift = getFFIType(env, returnJava, returnByValueAnnotation != NULL);
    if (env->IsAssignableFrom(returnJava, gStringClass))
        returnFFISwift = swiftString;

    ToNativeCallInfo* info = new ToNativeCallInfo;
    info->method = env->NewGlobalRef(method);
    info->swiftFunction = fnPtr;
    info->variadic = kNotVariadic;
    info->cached = false;
    info->isStatic = isStatic;
    info->offset = offset;
    info->isProtocol = isProtocolClass;
    info->needsStructRewrite = needsStructRewrite;

    ffi_prep_cif(&info->cif, FFI_DEFAULT_ABI, parameterCount + !isStatic, returnFFISwift, parametersSwift);

    jobject swiftConstructorAnnotation = env->CallObjectMethod(method, gGetAnnotationMethod, gSwiftConstructor);
    // SOLVE BETTER
    if ((!isStatic && !needsStructRewrite) || swiftConstructorAnnotation) {
        // Hack, to support x20 registers
        info->cif.flags = info->cif.flags | 256;
    }

    if (isProtocolClass) {
        // We don't need a closure here, so early exit
        jmethodID methodAsID = env->FromReflectedMethod(method);
        (*protocolMap)[methodAsID] = info;
        return;
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

void registerJavaMethod(JNIEnv* env, jclass type, jobject method, void* metadataPointer, uint64_t offset, bool patchMetadata = true) {
    jobjectArray parametersJava = (jobjectArray)env->CallObjectMethod(method, gGetParameterTypesMethod);
    jsize parameterCount = env->GetArrayLength(parametersJava);

    jobjectArray parameterAnns = (jobjectArray)env->CallObjectMethod(method, gGetParameterAnnotationsMethod);

    ffi_type** parametersSwift = new ffi_type*[parameterCount + 1]; // First one is self
    ffi_type** parametersToJava = new ffi_type*[parameterCount + 3]; // JNIEnv*, jobject, jmethodID

    parametersSwift[0] = &ffi_type_pointer;

    parametersToJava[0] = &ffi_type_pointer; // JNIEnv*
    parametersToJava[1] = &ffi_type_pointer; // jobject
    parametersToJava[2] = &ffi_type_pointer; // jmethodID

    for (jint i = 0; i < parameterCount; i++) {
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

        parametersToJava[i + 3] = getFFIType(env, parameterJava, false);
        ffi_type* parameterFFISwift = getFFIType(env, parameterJava, isByValue);
        if (env->IsAssignableFrom(parameterJava, gStringClass))
            parameterFFISwift = swiftString;
        parametersSwift[i + +1] = parameterFFISwift;
    }

    jobject returnByValueAnnotation = env->CallObjectMethod(method, gGetAnnotationMethod, gByValueClass);
    jclass returnJava = (jclass)env->CallObjectMethod(method, gGetReturnTypeMethod);
    ffi_type* returnToJava = getFFIType(env, returnJava, false);
    ffi_type* returnSwift = getFFIType(env, returnJava, returnByValueAnnotation != NULL);

    ToJavaCallInfo* info = new ToJavaCallInfo;
    info->method = env->NewGlobalRef(method);
    info->jniFunction = getJNICallFunction(env, returnToJava, false);
    info->methodId = env->FromReflectedMethod(info->method);
    info->isProtocolCall = false;
    
    ffi_prep_cif_var(&info->cif, FFI_DEFAULT_ABI, 3, parameterCount + 3, returnToJava, parametersToJava);
    ffi_cif* closureCif = new ffi_cif;
    ffi_prep_cif(closureCif, FFI_DEFAULT_ABI, parameterCount + 1, returnSwift, parametersSwift);

    closureCif->flags = closureCif->flags | 256;
    
    if (patchMetadata) {

        void* code = NULL;
        ffi_closure* closure = (ffi_closure*)ffi_closure_alloc(sizeof(ffi_closure), &code);

        ffi_prep_closure_loc(closure, closureCif, swiftToJavaHandler, info, code);

        set_at_offset(metadataPointer, void*, offset, code);
    }
    
    // Now also patch the correct pwt and than everything is cool!
    jobjectArray methods = (jobjectArray) env->CallStaticObjectMethod(gSwiftRuntimeClass, gSwiftRuntimeFindOriginMethods, method);
    jsize length = env->GetArrayLength(methods);
    for (jsize i = 0; i < length; i++) {
        jobject method = env->GetObjectArrayElement(methods, i);
        jclass interface = (jclass) env->CallObjectMethod(method, gGetMethodDeclaringClassMethod);
        void* pwt = (void*) env->CallStaticLongMethod(gSwiftRuntimeClass, gSwiftRuntimeGetPWT, metadataPointer, interface);
        jobject swiftVirtualMethodAnnotation = env->CallObjectMethod(method, gGetAnnotationMethod, gSwiftVirtualMethod);
        jlong offset = env->CallLongMethod(swiftVirtualMethodAnnotation, gSwiftMethodOffsetMethod);
        
        // TODO: We need different closures here, to differentiate between "called as protocol" and "called as object method", so we can correctly unpack it when converting to java
        ToJavaCallInfo* protocolInfo = new ToJavaCallInfo;
        memcpy(protocolInfo, info, sizeof(ToJavaCallInfo));
        protocolInfo->isProtocolCall = true;
        
        void* code = NULL;
        ffi_closure* closure = (ffi_closure*)ffi_closure_alloc(sizeof(ffi_closure), &code);

        ffi_prep_closure_loc(closure, closureCif, swiftToJavaHandler, protocolInfo, code);
        
        
        set_at_offset(pwt, void*, offset, code);
    }
}

void JNICALL Java_org_moe_natj_swift_SwiftRuntime_registerClass(JNIEnv* env, jclass clazz, jclass type) {
    jobjectArray methods = (jobjectArray)env->CallObjectMethod(type, gGetDeclaredMethodsMethod);
    jsize length = env->GetArrayLength(methods);

    bool isStructure = env->CallBooleanMethod(type, gIsAnnotationPresentMethod, gStructureClass);
    jobject protocolClassAnnotation = env->CallObjectMethod(type, gGetAnnotationMethod, gSwiftProtocolAnnotationClass);
    bool isProtocolClass = protocolClassAnnotation != NULL;
    if (isStructure) {
        Java_org_moe_natj_c_CRuntime_registerClass(env, clazz, type);
    }

    if (isProtocolClass) {
        jstring symbolString = (jstring)env->CallObjectMethod(protocolClassAnnotation, gSwiftProtocolDescriptorMethod);
        const char* symbolName = env->GetStringUTFChars(symbolString, NULL);
        void* witnessTable = dlsym(RTLD_DEFAULT, symbolName);
        if (witnessTable == NULL) {
            LOGW << "Symbol " << symbolName << " not found!";
        }
        env->CallStaticVoidMethod(gSwiftRuntimeClass, gSwiftRuntimeRegisterProtocol, type, (jlong)witnessTable);
    }

    bool isInherited = !env->CallBooleanMethod(type, gIsAnnotationPresentMethod, gSwiftBindingClass) && !isProtocolClass && !isStructure && env->IsAssignableFrom(type, gNativeObjectClass);
    void* metadataPointer = NULL;
    bool isClass = true;
    if (isInherited) {
        metadataPointer = generateClassMetadataPointer(env, type, &isClass);
        // We sadly can't use fast acces methods like __natjCache here, since we don't deal with generated bindings. We need to rely on the hashmaps
        env->CallStaticVoidMethod(gSwiftRuntimeClass, gSwiftRuntimeRegisterMetadata, type, (jlong)metadataPointer);
        createPWTForDirectClass(env, type, metadataPointer);
    }

    for (jint i = 0; i < length; i++) {
        jobject method = env->GetObjectArrayElement(methods, i);
        jint modifiers = env->CallIntMethod(method, gGetModifiersMethod);
        bool isNative = modifiers & ACC_NATIVE;
        bool isStatic = modifiers & ACC_STATIC;
        jobject swiftMethodAnnotation = env->CallObjectMethod(method, gGetAnnotationMethod, gSwiftStaticMethod);
        jobject swiftVirtualMethodAnnotation = env->CallObjectMethod(method, gGetAnnotationMethod, gSwiftVirtualMethod);
        void* symbol = NULL;
        uint64_t offset = 0;
        if (swiftMethodAnnotation) {
            jstring symbolString = (jstring)env->CallObjectMethod(swiftMethodAnnotation, gSwiftMethodSymbolMethod);
            const char* symbolName = env->GetStringUTFChars(symbolString, NULL);

            if (!isNative) {
                LOGW << "Method annotated with SwiftStaticMethod(" << symbolName << ") is not native.";
                continue;
            }

            symbol = dlsym(RTLD_DEFAULT, symbolName);
            if (symbol == NULL) {
                LOGW << "Symbol " << symbolName << " not found!";
                continue;
            }
        } else if (swiftVirtualMethodAnnotation) {
            offset = env->CallLongMethod(swiftVirtualMethodAnnotation, gSwiftMethodOffsetMethod);
        } else {
            continue;
        }

        if (!isNative && isInherited) {
            registerJavaMethod(env, type, method, metadataPointer, offset, isClass);
            continue;
        } else {
            registerNativeMethod(env, type, method, isStatic, isStructure, isProtocolClass, symbol, offset);
        }
    }
}
