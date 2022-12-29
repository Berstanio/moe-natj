package org.moe.natj.swift;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ClosureProxyHandler {

    public static Object createClosureProxy(long closure, Class<?> type) {
        ClosureProxyHandler.ClosureProxy closureProxy = new ClosureProxyHandler.ClosureProxy(closure);
        return Proxy.newProxyInstance(ClosureProxyHandler.class.getClassLoader(), new Class[] {type}, closureProxy);
    }


    public static class ClosureProxy implements InvocationHandler {
        private final long closure;
        public ClosureProxy(long closure) {
            this.closure = closure;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getName().equals("toString")) return "Proxy"; // TODO: 29.12.22 Reeeally needs to be done better
            return null;
        }
    }
}
