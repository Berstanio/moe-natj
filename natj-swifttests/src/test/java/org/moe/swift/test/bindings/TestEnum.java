package org.moe.swift.test.bindings;

import org.moe.natj.c.ann.Structure;
import org.moe.natj.c.ann.StructureField;
import org.moe.natj.general.NatJ;
import org.moe.natj.general.Pointer;
import org.moe.natj.general.ann.ByValue;
import org.moe.natj.general.ann.Runtime;
import org.moe.natj.swift.SwiftEnumObject;
import org.moe.natj.swift.SwiftRuntime;
import org.moe.natj.swift.ann.StaticSwiftMethod;
import org.moe.natj.swift.ann.SwiftEnum;
import org.moe.natj.swift.ann.SwiftEnumCase;

@SwiftEnum(size = 8 * 1)
@Runtime(SwiftRuntime.class)
public abstract class TestEnum extends SwiftEnumObject {

    static {
        NatJ.register();
    }

    private static long __natjCache;

    protected TestEnum(Pointer peer) {
        super(peer);
    }

    private TestEnum(byte ordinal) {
        super(TestEnum.class);
        SwiftRuntime.setAtOffset(getPeerPointer(), TestEnum.class.getAnnotation(SwiftEnum.class).size(), ordinal);
    }

    @StaticSwiftMethod(symbol = "$s9swiftTest0B4EnumOMa")
    public static native long getType();

    @StaticSwiftMethod(symbol = "$s9swiftTest10returnEnumAA0bD0OyF")
    @ByValue
    public static native TestEnum getTestEnum();

    @StaticSwiftMethod(symbol = "$s9swiftTest12retrieveEnumySiAA0bD0OF")
    public static native long passTestEnum(@ByValue TestEnum testEnum);

    @StaticSwiftMethod(symbol = "$s9swiftTest0B4EnumO04testC8FunctionyS2iF")
    public native long testEnumFunction(long value);

    @Structure
    @SwiftEnumCase(ordinal = 3)
    public static class EmptyTestCase extends TestEnum {

        static {
            NatJ.register();
        }

        private static byte __ordinal;
        private static long __natjCache;

        protected EmptyTestCase(Pointer peer) {
            super(peer);
        }

        public EmptyTestCase() {
            super(__ordinal);
        }
    }

    @Structure
    @SwiftEnumCase(ordinal = 0)
    public static class NotEmptyTestCase extends TestEnum {

        static {
            NatJ.register();
        }

        private static byte __ordinal;
        private static long __natjCache;

        protected NotEmptyTestCase(Pointer peer) {
            super(peer);
        }

        public NotEmptyTestCase(long field) {
            super(__ordinal);
            setField1(field);
        }

        @StructureField(order = 0, isGetter = true)
        public native long getField1();

        @StructureField(order = 0, isGetter = false)
        private native void setField1(long field1);
    }

    @Structure
    @SwiftEnumCase(ordinal = 1)
    public static class SecondTestCase extends TestEnum {

        static {
            NatJ.register();
        }

        private static byte __ordinal;
        private static long __natjCache;

        protected SecondTestCase(Pointer peer) {
            super(peer);
        }

        public SecondTestCase(long field1) {
            super(__ordinal);
            setField1(field1);
        }

        @StructureField(order = 0, isGetter = true)
        private native long getField1();

        @StructureField(order = 0, isGetter = false)
        private native void setField1(long field1);
    }

    @Structure
    @SwiftEnumCase(ordinal = 2)
    public static class FloatTestCase extends TestEnum {

        static {
            NatJ.register();
        }

        private static byte __ordinal;
        private static long __natjCache;

        protected FloatTestCase(Pointer peer) {
            super(peer);
        }

        public FloatTestCase(float field1) {
            super(__ordinal);
            setField1(field1);
        }

        @StructureField(order = 0, isGetter = true)
        private native float getField1();

        @StructureField(order = 0, isGetter = false)
        private native void setField1(float field1);
    }
}
