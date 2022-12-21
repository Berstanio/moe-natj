package org.moe.natj.swift.ann;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marker annotation, that the class is a pure binding class. It's main purpose is to identify inherited classes.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface SwiftBindingClass {
}
