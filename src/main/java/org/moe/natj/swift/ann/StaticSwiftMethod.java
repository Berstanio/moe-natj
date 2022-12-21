package org.moe.natj.swift.ann;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * For dispatching a function by a symbol. This needs to be used, if no vtable is existent.
 * This is e.g. the case for final functions.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface StaticSwiftMethod {

    String symbol();
}
