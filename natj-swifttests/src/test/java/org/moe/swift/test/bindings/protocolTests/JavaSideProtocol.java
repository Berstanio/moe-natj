package org.moe.swift.test.bindings.protocolTests;

import org.moe.natj.c.CRuntime;
import org.moe.natj.general.NatJ;
import org.moe.natj.general.NativeObject;
import org.moe.natj.general.Pointer;
import org.moe.natj.general.ann.Runtime;
import org.moe.natj.general.ptr.impl.PtrFactory;
import org.moe.natj.swift.SwiftRuntime;
import org.moe.natj.swift.ann.VirtualSwiftMethod;

@Runtime(SwiftRuntime.class)
public class JavaSideProtocol extends NativeObject implements TestProtocol {

    static {
        NatJ.register();
    }

    /**
     * Constructs a {@link NativeObject} from a {@link Pointer}.
     *
     * @param peer The pointer pointing to the native peer.
     */
    protected JavaSideProtocol(Pointer peer) {
        super(peer);
    }

    public JavaSideProtocol() {
        super(new Pointer(CRuntime.malloc(8)));
        // TODO: 23.12.22 Weeeell, this is silly... We DEFINITLY need a better way to create the dummy object
        CRuntime.memcpy(getPeerPointer(), PtrFactory.newLongReference(SwiftRuntime.getMetadataForClass(JavaSideProtocol.class)).getPeer().getPeer(), 8);
    }

    @Override
    @VirtualSwiftMethod(offset = 8)
    public long protoFunc() {
        return 77;
    }
}
