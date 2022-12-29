package org.moe.natj.swift.ann;

import org.moe.natj.general.ann.Callable;
import org.moe.natj.general.ann.Runtime;
import org.moe.natj.swift.SwiftRuntime;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Callable
@Retention(RetentionPolicy.RUNTIME)
@Runtime(SwiftRuntime.class)
public @interface SwiftClosure {
}
