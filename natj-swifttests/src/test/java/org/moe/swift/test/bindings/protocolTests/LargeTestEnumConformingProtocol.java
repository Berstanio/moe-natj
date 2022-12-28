package org.moe.swift.test.bindings.protocolTests;

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

@SwiftEnum(size = 8 * 5)
@Runtime(SwiftRuntime.class)
public abstract class LargeTestEnumConformingProtocol extends SwiftEnumObject implements TestProtocol {

    static {
        NatJ.register();
    }

    private static long __natjCache;

    protected LargeTestEnumConformingProtocol(Pointer peer) {
        super(peer);
    }

    private LargeTestEnumConformingProtocol(byte ordinal) {
        super(LargeTestEnumConformingProtocol.class, ordinal);
    }

    @StaticSwiftMethod(symbol = "$s9swiftTest05LargeB22EnumConformingProtocolOMa")
    public static native long getType();

    @StaticSwiftMethod(symbol = "$s9swiftTest05LargeB22EnumConformingProtocolO9protoFuncSiyF")
    public native long protoFunc();

    @StaticSwiftMethod(symbol = "$s9swiftTest08getLargeB14EnumAsProtocolAA0bG0_pyF")
    @ByValue
    public static native TestProtocol getEnumAsProtocol();

    @Structure
    @SwiftEnumCase(ordinal = 3)
    public static class EmptyTestCase extends LargeTestEnumConformingProtocol {

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
    public static class NotEmptyTestCase extends LargeTestEnumConformingProtocol {

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
    public static class SecondTestCase extends LargeTestEnumConformingProtocol {

        static {
            NatJ.register();
        }

        private static byte __ordinal;
        private static long __natjCache;

        protected SecondTestCase(Pointer peer) {
            super(peer);
        }

        public SecondTestCase(long field1, long field2) {
            super(__ordinal);
            setField1(field1);
            setField2(field2);
        }

        @StructureField(order = 0, isGetter = true)
        public native long getField1();

        @StructureField(order = 0, isGetter = false)
        private native void setField1(long field);

        @StructureField(order = 1, isGetter = true)
        public native long getField2();

        @StructureField(order = 1, isGetter = false)
        private native void setField2(long field);

    }

    @Structure
    @SwiftEnumCase(ordinal = 2)
    public static class LargeTestCase extends LargeTestEnumConformingProtocol {

        static {
            NatJ.register();
        }

        private static byte __ordinal;
        private static long __natjCache;

        protected LargeTestCase(Pointer peer) {
            super(peer);
        }

        public LargeTestCase(long field1, long field2, long field3, long field4, long field5) {
            super(__ordinal);
            setField1(field1);
            setField2(field2);
            setField3(field3);
            setField4(field4);
            setField5(field5);
        }

        @StructureField(order = 0, isGetter = true)
        public native long getField1();

        @StructureField(order = 0, isGetter = false)
        private native void setField1(long field1);

        @StructureField(order = 1, isGetter = true)
        public native long getField2();

        @StructureField(order = 1, isGetter = false)
        private native void setField2(long field);

        @StructureField(order = 2, isGetter = true)
        public native long getField3();

        @StructureField(order = 2, isGetter = false)
        private native void setField3(long field);

        @StructureField(order = 3, isGetter = true)
        public native long getField4();

        @StructureField(order = 3, isGetter = false)
        private native void setField4(long field);

        @StructureField(order = 4, isGetter = true)
        public native long getField5();

        @StructureField(order = 4, isGetter = false)
        private native void setField5(long field);

    }
}
