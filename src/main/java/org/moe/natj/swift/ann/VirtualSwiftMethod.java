package org.moe.natj.swift.ann;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Used to dispatch a function based on a vtable.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface VirtualSwiftMethod {

    // Offset seems to start at +88

    long offset();
}
