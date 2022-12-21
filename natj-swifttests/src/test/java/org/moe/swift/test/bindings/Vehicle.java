package org.moe.swift.test.bindings;

import org.moe.natj.general.NatJ;
import org.moe.natj.general.NativeObject;
import org.moe.natj.general.Pointer;
import org.moe.natj.general.ann.Runtime;
import org.moe.natj.swift.SwiftRuntime;
import org.moe.natj.swift.ann.SwiftConstructor;
import org.moe.natj.swift.ann.StaticSwiftMethod;

@Runtime(SwiftRuntime.class)
public class Vehicle extends NativeObject {

    static {
        NatJ.register();
    }
    /**
     * Constructs a {@link NativeObject} from a {@link Pointer}.
     *
     * @param peer The pointer pointing to the native peer.
     */
    protected Vehicle(Pointer peer) {
        super(peer);
    }

    public Vehicle() {
        super(new Pointer(constructor(getType())));
    }

    @StaticSwiftMethod(symbol = "$s9swiftTest7VehicleCMa")
    private static native long getType();

    @SwiftConstructor
    @StaticSwiftMethod(symbol = "$s9swiftTest7VehicleCACycfC")
    private static native long constructor(long type);

    @StaticSwiftMethod(symbol = "$s9swiftTest7VehicleC19currentSpeedPlusParyS2iF")
    public native int currentSpeedPlusPar(int par);

    @StaticSwiftMethod(symbol = "$s9swiftTest7VehicleC12currentSpeedSivg")
    public native int getSpeed();

    @StaticSwiftMethod(symbol = "$s9swiftTest7VehicleC12currentSpeedSivs")
    public native void setSpeed(int speed);
}
