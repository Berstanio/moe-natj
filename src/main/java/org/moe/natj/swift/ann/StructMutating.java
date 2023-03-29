package org.moe.natj.swift.ann;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marker annotation, that method mutates the self struct.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface StructMutating {
}
