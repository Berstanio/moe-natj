package org.moe.swift.test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.moe.swift.test.bindings.Vehicle;

public class SwiftClassTest extends BaseSwiftTest {

    @Test
    public void testClass(){
        Vehicle vehicle = new Vehicle();
        assertEquals(0, vehicle.getSpeed());
        assertEquals(5, vehicle.currentSpeedPlusPar(5));
        vehicle.setSpeed(12);
        assertEquals(12, vehicle.getSpeed());
        assertEquals(17, vehicle.currentSpeedPlusPar(5));
    }
}
