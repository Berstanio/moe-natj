package org.moe.xosrt.binding.test.swift;

import org.junit.BeforeClass;
import org.junit.Test;
import org.moe.natj.general.NatJ;
import org.moe.natj.general.Pointer;
import org.moe.natj.general.ann.Runtime;
import org.moe.natj.swift.SwiftRuntime;
import org.moe.natj.swift.ann.SwiftMethod;

@Runtime(SwiftRuntime.class)
public class SwiftTest {
    static {

        System.loadLibrary("TestClasses");
        System.load("/Users/berstanio/IdeaProjects/NatJ-Swift/libswift.dylib");

        NatJ.register();
    }

    @SwiftMethod(symbol = "$s5swift12pleeeaseWorkAA7VehicleCyF")
    public static native Vehicle test();

    @SwiftMethod(symbol = "$s5swift17funcWithMoreParam4par14par2ySi_SitF")
    public static native void globalFunction(int par1, int par2);

    @Test
    public void realTest(){
        Vehicle vehicle = new Vehicle();
        vehicle.makeNoise(15);
        vehicle.setSpeed(12);
        vehicle.makeNoise(6);
        System.out.println("SPEED: " + vehicle.getSpeed());
        globalFunction(15, 18);
    }
}
