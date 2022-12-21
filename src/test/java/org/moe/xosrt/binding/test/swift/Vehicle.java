package org.moe.xosrt.binding.test.swift;

import org.moe.natj.general.NatJ;
import org.moe.natj.general.NativeObject;
import org.moe.natj.general.Pointer;
import org.moe.natj.general.ann.Runtime;
import org.moe.natj.swift.SwiftRuntime;
import org.moe.natj.swift.ann.SwiftConstructor;
import org.moe.natj.swift.ann.SwiftMethod;

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

    @SwiftMethod(symbol = "$s5swift7VehicleCMa")
    private static native long getType();

    @SwiftConstructor
    @SwiftMethod(symbol = "$s5swift7VehicleCACycfC")
    private static native long constructor(long type);

    @SwiftMethod(symbol = "$s5swift7VehicleC9makeNoiseyySiF")
    public native void makeNoise(int par);

    @SwiftMethod(symbol = "$s5swift7VehicleC12currentSpeedSivg")
    public native int getSpeed();

    @SwiftMethod(symbol = "$s5swift7VehicleC12currentSpeedSivs")
    public native void setSpeed(int speed);
}
