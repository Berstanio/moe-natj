package org.moe.natj.swift.ann;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Tells NatJ, to treat the method as a object method, even though it is static
 * Important for swiftself calling conventions
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface SwiftConstructor {
}
