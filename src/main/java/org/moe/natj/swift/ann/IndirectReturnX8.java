package org.moe.natj.swift.ann;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Tells NatJ, to tell libFFI, to act as a void return, since the called method will put the result already in
 * a pointer in x8. No clue why it couldn't be returned just with the normal return register, but w/e
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface IndirectReturnX8 {
}
