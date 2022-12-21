package org.moe.natj.swift;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProtocolProxyHandler {

    public static Object createProtocolProxy(long peer, Class<?> type) {
        ProtocolProxy protocolProxy = new ProtocolProxy(peer);
        return Proxy.newProxyInstance(ProtocolProxyHandler.class.getClassLoader(), new Class[] {type}, protocolProxy);
    }

    public static class ProtocolProxy implements InvocationHandler {

        private final long peer;

        public ProtocolProxy(long peer) {
            this.peer = peer;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return null;
        }

        public long getPeer() {
            return peer;
        }
    }
}
