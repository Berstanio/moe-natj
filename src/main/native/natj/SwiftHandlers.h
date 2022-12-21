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

  /** The ffi_cif needed for the native call */
  ffi_cif cif;
    
    bool isStatic;

  /** Info needed for variadic methods */
  int8_t variadic;

#ifdef __APPLE__
  /** Contains indexes of out arguments */
  std::vector<size_t> outObjectReferences;
#endif
};

void javaToSwiftHandler(ffi_cif* cif, void* result, void** args, void* user);

