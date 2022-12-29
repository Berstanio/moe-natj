package org.moe.natj.swift;

import org.moe.natj.c.CRuntime;
import org.moe.natj.general.Pointer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProtocolProxyHandler {

    public static Object createProtocolProxy(long peer, Class<?> type) {
        ProtocolProxy protocolProxy = new ProtocolProxy(peer);
        return Proxy.newProxyInstance(ProtocolProxyHandler.class.getClassLoader(), new Class[] {type}, protocolProxy);
    }

    public static class ProtocolProxy implements InvocationHandler {

        private final Pointer pointer;

        public ProtocolProxy(long peer) {
            this.pointer = CRuntime.createStrongPointer(peer, true);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getName().equals("toString")) return "Proxy";
            if (args == null) args = new Object[0];
            Object[] argWithProxy = new Object[args.length + 1];
            System.arraycopy(args, 0, argWithProxy, 1, args.length);
            argWithProxy[0] = proxy;

            Class<?> returnType = method.getReturnType();
            if (returnType == Boolean.TYPE) {
                return SwiftRuntime.forwardBooleanProtocolCall(method.getDeclaringClass(), method, argWithProxy);
            } else if (returnType == Byte.TYPE) {
                return SwiftRuntime.forwardByteProtocolCall(method.getDeclaringClass(), method, argWithProxy);
            } else if (returnType == Character.TYPE) {
                return SwiftRuntime.forwardCharProtocolCall(method.getDeclaringClass(), method, argWithProxy);
            } else if (returnType == Short.TYPE) {
                return SwiftRuntime.forwardShortProtocolCall(method.getDeclaringClass(), method, argWithProxy);
            } else if (returnType == Integer.TYPE) {
                return SwiftRuntime.forwardIntProtocolCall(method.getDeclaringClass(), method, argWithProxy);
            } else if (returnType == Long.TYPE) {
                return SwiftRuntime.forwardLongProtocolCall(method.getDeclaringClass(), method, argWithProxy);
            } else if (returnType == Float.TYPE) {
                return SwiftRuntime.forwardFloatProtocolCall(method.getDeclaringClass(), method, argWithProxy);
            } else if (returnType == Double.TYPE) {
                return SwiftRuntime.forwardDoubleProtocolCall(method.getDeclaringClass(), method, argWithProxy);
            } else if (returnType == Void.TYPE) {
                SwiftRuntime.forwardVoidProtocolCall(method.getDeclaringClass(), method, argWithProxy);
                return null;
            } else {
                return SwiftRuntime.forwardObjectProtocolCall(method.getDeclaringClass(), method, argWithProxy);
            }
        }

        public long getPeer() {
            return pointer.getPeer();
        }
    }
}
