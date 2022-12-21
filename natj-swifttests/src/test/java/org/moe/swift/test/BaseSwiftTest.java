package org.moe.swift.test;

public abstract class BaseSwiftTest {

    static {
        System.loadLibrary("natj");
        System.loadLibrary("swiftTest");
    }
}
