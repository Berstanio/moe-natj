package org.moe.natj.swift.ann;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SwiftMethod {

    String symbol();
}
