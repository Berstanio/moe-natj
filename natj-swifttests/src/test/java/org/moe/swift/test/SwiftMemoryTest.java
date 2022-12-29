package org.moe.swift.test;

import org.junit.jupiter.api.Test;
import org.moe.natj.swift.SwiftRuntime;
import org.moe.swift.test.bindings.memoryTests.DummyObject;

import java.lang.ref.WeakReference;

import static org.junit.jupiter.api.Assertions.*;

public class SwiftMemoryTest extends BaseSwiftTest {

    @Test
    public void testRetainCount() {
        DummyObject dummyObject = new DummyObject();
        assertEquals(1, SwiftRuntime.retainCount(dummyObject.getPeerPointer()));
        SwiftRuntime.retain(dummyObject.getPeerPointer());
        assertEquals(2, SwiftRuntime.retainCount(dummyObject.getPeerPointer()));
        SwiftRuntime.release(dummyObject.getPeerPointer());
        assertEquals(1, SwiftRuntime.retainCount(dummyObject.getPeerPointer()));

        // Outside of the scope of the test, but we need to manually clean up to prevent interfere with later tests
        SwiftRuntime.release(dummyObject.getPeerPointer());
        dummyObject.getPeer().setPeer(0);
    }

    @Test
    public void testFreeAfterManualRelease() {
        DummyObject dummyObject = new DummyObject();
        assertFalse(DummyObject.isReleased());
        SwiftRuntime.release(dummyObject.getPeerPointer());
        assertTrue(DummyObject.isReleased());
        dummyObject.getPeer().setPeer(0);
    }

    @Test
    public void testFreeAfterGCReleaseConstructorObject() {
        DummyObject dummyObject = new DummyObject();
        assertFalse(DummyObject.isReleased());
        WeakReference<DummyObject> weakReference = new WeakReference<>(dummyObject);
        dummyObject = null;
        while (weakReference.get() != null) {
            assertFalse(DummyObject.isReleased());
            System.gc();
        }
        System.runFinalization();
        assertTrue(DummyObject.isReleased());
    }

    @Test
    public void testFreeAfterGCReleaseReturnedObject() {
        DummyObject dummyObject = DummyObject.constructorReturnsObjec(DummyObject.getType());
        assertFalse(DummyObject.isReleased());
        WeakReference<DummyObject> weakReference = new WeakReference<>(dummyObject);
        dummyObject = null;
        while (weakReference.get() != null) {
            assertFalse(DummyObject.isReleased());
            System.gc();
        }
        System.runFinalization();
        assertTrue(DummyObject.isReleased());
    }
}
