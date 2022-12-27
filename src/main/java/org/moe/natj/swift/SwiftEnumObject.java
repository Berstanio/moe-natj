package org.moe.natj.swift;

import org.moe.natj.c.StructObject;
import org.moe.natj.general.Pointer;
import org.moe.natj.general.ann.Runtime;

@Runtime(SwiftRuntime.class)
public abstract class SwiftEnumObject extends StructObject {

    // TODO: 27.12.22 Is this class needed?

    protected SwiftEnumObject(Class<? extends SwiftEnumObject> type) {
        super(type);
    }
    protected SwiftEnumObject(Pointer peer) {
        super(peer);
    }
}
