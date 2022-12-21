package org.moe.natj.swift.ann;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marker annotation for NatJ, so it knows it needs to perform/expect existential container boxing
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface SwiftProtocol {
}
