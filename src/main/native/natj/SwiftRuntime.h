#include "NatJ.h"

extern jobject getSwiftRuntime();

extern "C" {
JNIEXPORT void JNICALL Java_org_moe_natj_swift_SwiftRuntime_initialize(JNIEnv* env, jclass clazz, jobject instance);
JNIEXPORT void JNICALL Java_org_moe_natj_swift_SwiftRuntime_registerClass(JNIEnv* env, jclass clazz, jclass type);
void* dereferencePeer(void** peer);
}
