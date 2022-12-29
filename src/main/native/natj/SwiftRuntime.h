#include "NatJ.h"

extern jobject getSwiftRuntime();

extern "C" {
JNIEXPORT void JNICALL Java_org_moe_natj_swift_SwiftRuntime_initialize(JNIEnv* env, jclass clazz, jobject instance);
JNIEXPORT void JNICALL Java_org_moe_natj_swift_SwiftRuntime_registerClass(JNIEnv* env, jclass clazz, jclass type);
JNIEXPORT jlong JNICALL Java_org_moe_natj_swift_SwiftRuntime_createSwiftClosure(JNIEnv* env, jclass clazz, jobject object, jlong infoL, jlong cifL);

void* dereferencePeer(void** peer);
/**
 * Macros for declaring forwarders for native protocol calls
 */
#define TYPED_PROTOCOL_CALL_FORWARDER_IMPL(type, name) \
    JNIEXPORT type JNICALL Java_org_moe_natj_swift_SwiftRuntime_forward##name##ProtocolCall(JNIEnv* env, jclass clazz, jclass protocolClass, jobject method, jobjectArray args);

TYPED_PROTOCOL_CALL_FORWARDER_IMPL(jboolean, Boolean)
TYPED_PROTOCOL_CALL_FORWARDER_IMPL(jbyte, Byte)
TYPED_PROTOCOL_CALL_FORWARDER_IMPL(jchar, Char)
TYPED_PROTOCOL_CALL_FORWARDER_IMPL(jshort, Short)
TYPED_PROTOCOL_CALL_FORWARDER_IMPL(jint, Int)
TYPED_PROTOCOL_CALL_FORWARDER_IMPL(jlong, Long)
TYPED_PROTOCOL_CALL_FORWARDER_IMPL(jfloat, Float)
TYPED_PROTOCOL_CALL_FORWARDER_IMPL(jdouble, Double)
TYPED_PROTOCOL_CALL_FORWARDER_IMPL(jobject, Object)

#undef TYPED_PROTOCOL_CALL_FORWARDER_IMPL

JNIEXPORT void JNICALL Java_org_moe_natj_swift_SwiftRuntime_forwardVoidProtocolCall(JNIEnv* env, jclass clazz, jclass protocolClass, jobject method, jobjectArray args);
}
