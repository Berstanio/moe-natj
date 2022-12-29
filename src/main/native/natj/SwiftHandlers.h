#include "SwiftRuntime.h"

struct ToNativeCallInfo {
    /** The method we will build construction infos for */
    jobject method;

    /** Set to false after the caching is done */
    bool cached;

    /** The built construction infos for the complex arguments */
    jobject* paramInfos;

    /** The built construction info for complex return value */
    jobject returnInfo;

    /** The c callback to call */
    void* swiftFunction;

    uint64_t offset;

    /** The ffi_cif needed for the native call */
    ffi_cif cif;

    bool isStatic;

    bool isProtocol;

    bool needsStructRewrite;

    /** Info needed for variadic methods */
    int8_t variadic;

#ifdef __APPLE__
    /** Contains indexes of out arguments */
    std::vector<size_t> outObjectReferences;
#endif
};

struct ToJavaCallInfo {
    jobject method;
    jmethodID methodId;
    bool cached;
    jobject* paramInfos;
    jobject returnInfo;
    void* jniFunction;
    ffi_cif cif;
    bool isProtocolCall;
    jobject objectToCall = NULL;
};

void javaToSwiftHandler(ffi_cif* cif, void* result, void** args, void* user);

void swiftToJavaHandler(ffi_cif* cif, void* result, void** args, void* user);
