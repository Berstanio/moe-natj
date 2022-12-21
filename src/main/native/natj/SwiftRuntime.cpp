#include "SwiftRuntime.h"
#include "SwiftHandlers.h"
#include "CRuntime.h"
#include <unordered_map>

static jobject gRuntime = NULL;

jclass gSwiftRuntimeClass = NULL;

jclass gSwiftStaticMethod = NULL;
jclass gSwiftConstructor = NULL;
jclass gSwiftVirtualMethod = NULL;

jmethodID gSwiftMethodSymbolMethod = NULL;
jmethodID gSwiftProtocolDescriptorMethod = NULL;

jmethodID gSwiftMethodOffsetMethod = NULL;
jmethodID gSwiftRuntimeRegisterProtocol = NULL;

jobject getSwiftRuntime() { return gRuntime; }

std::unordered_map<jmethodID, ToNativeCallInfo*>* protocolMap = NULL;

void* dereferencePeer(void** peer) {
    return *peer;
}

#define TYPED_PROTOCOL_CALL_FORWARDER(type, name, type_ffi)                                               \
  type Java_org_moe_natj_swift_SwiftRuntime_forward##name##ProtocolCall(JNIEnv* env, jclass clazz, jclass protocolClass, jobject method, jobjectArray args) {                   \
    ffi_cif cif; \
    jsize length = env->GetArrayLength(args) + 1; \
    void* arg_values[length]; \
    jobject objects[length - 1]; \
    for (jsize i = 0; i < length - 1; i++) { \
        objects[i] = env->GetObjectArrayElement(args, i); \
    } \
    for (jsize i = 1; i < length; i++) { \
        arg_values[i] = &objects[i - 1]; \
    } \
    arg_values[0] = &env; \
    type result; \
    ToNativeCallInfo* info = (*protocolMap)[getMethodIDFromMethod(env, protocolClass, method)]; \
    cif.rtype = &type_ffi; \
    javaToSwiftHandler(&cif, &result, (void**)&arg_values, (void*)info); \
    return result;                                                                              \
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

    ToNativeCallInfo* info = (*protocolMap)[getMethodIDFromMethod(env, protocolClass, method)];
    cif.rtype = &ffi_type_void;
    javaToSwiftHandler(&cif, NULL, (void**)&arg_values, (void*)info);
}



void JNICALL Java_org_moe_natj_swift_SwiftRuntime_initialize(JNIEnv* env, jclass clazz, jobject instance) {
    gRuntime = env->NewGlobalRef(instance);
    gSwiftRuntimeClass = (jclass)env->NewGlobalRef(clazz);
    gSwiftRuntimeRegisterProtocol = env->GetStaticMethodID(gSwiftRuntimeClass, "registerProtocolClass", "(Ljava/lang/Class;J)V");
    gSwiftProtocolDescriptorMethod = env->GetMethodID(gSwiftProtocolAnnotationClass, "protocolDescriptor", "()Ljava/lang/String;");
    env->PushLocalFrame(2);

    gSwiftStaticMethod = (jclass)env->NewGlobalRef(env->FindClass("org/moe/natj/swift/ann/StaticSwiftMethod"));
    gSwiftVirtualMethod = (jclass)env->NewGlobalRef(env->FindClass("org/moe/natj/swift/ann/VirtualSwiftMethod"));

    gSwiftConstructor = (jclass)env->NewGlobalRef(env->FindClass("org/moe/natj/swift/ann/SwiftConstructor"));

    gSwiftMethodSymbolMethod = env->GetMethodID(gSwiftStaticMethod, "symbol", "()Ljava/lang/String;");
    gSwiftMethodOffsetMethod = env->GetMethodID(gSwiftVirtualMethod, "offset", "()J");
    env->PopLocalFrame(NULL);
    protocolMap = new std::unordered_map<jmethodID, ToNativeCallInfo*>();
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
            std::cout << "Symbol " << symbolName << " not found!" << std::endl;
        }
        env->CallStaticVoidMethod(gSwiftRuntimeClass, gSwiftRuntimeRegisterProtocol, type, (jlong)witnessTable);
    }

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
            parametersSwift[i + !isStatic] = getFFIType(env, parameterJava, isByValue);
        }

        jobject returnByValueAnnotation = env->CallObjectMethod(method, gGetAnnotationMethod, gByValueClass);
        jclass returnJava = (jclass)env->CallObjectMethod(method, gGetReturnTypeMethod);
        bool isProtocolReturn = env->CallBooleanMethod(returnJava, gIsAnnotationPresentMethod, gSwiftProtocolAnnotationClass);
        
        if (isProtocolReturn) {
            // We register a class that can return a plain protocol. It can be, that it returns something, we don't have bindings for. Therefor we need to register the protocol class now too
            Java_org_moe_natj_swift_SwiftRuntime_registerClass(env, clazz, returnJava);
        }
        ffi_type* returnFFI = getFFIType(env, returnJava, false);
        ffi_type* returnFFISwift = getFFIType(env, returnJava, returnByValueAnnotation != NULL);

        ToNativeCallInfo* info = new ToNativeCallInfo;
        info->method = env->NewGlobalRef(method);
        info->swiftFunction = symbol;
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
            jmethodID methodAsID = getMethodIDFromMethod(env, type, method);
            (*protocolMap)[methodAsID] = info;
            continue;
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
