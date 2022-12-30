package org.moe.natj.swift.map;

import org.moe.natj.general.Mapper;
import org.moe.natj.general.NatJ;
import org.moe.natj.general.Pointer;
import org.moe.natj.general.ann.Runtime;
import org.moe.natj.swift.SwiftRuntime;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.WeakHashMap;

@Runtime(SwiftRuntime.class)
public class SwiftClosureMapper implements Mapper {

    private WeakHashMap<Object, Pointer> instanceClosureMap = new WeakHashMap<>();
    private HashMap<Long, WeakReference<Object>> closureInstanceMap = new HashMap<>();

    @Override
    public long toNative(Object instance, NatJ.NativeObjectConstructionInfo info) {
        if (instanceClosureMap.containsKey(instance)) return instanceClosureMap.get(instance).getPeer();
        long peer = SwiftRuntime.createSwiftClosure(instance, (Class<?>) info.data);
        instanceClosureMap.put(instance, new Pointer(peer, closureReleaser));
        closureInstanceMap.put(peer, new WeakReference<>(instance));
        return peer;
    }

    @Override
    public Object toJava(long instance, NatJ.JavaObjectConstructionInfo info) {
        // TODO: 30.12.22 TOTALLY INSUFFICIENT
        if (closureInstanceMap.containsKey(instance)) return closureInstanceMap.get(instance).get();
        throw new RuntimeException("Implement unknown closures " + instance);
    }

    private Pointer.Releaser closureReleaser = new Pointer.Releaser() {
        @Override
        public void release(long peer) {
            closureInstanceMap.remove(peer);
            SwiftRuntime.releaseSwiftClosure(peer);
        }

        @Override
        public boolean ifFinalizedExternally() {
            return false;
        }
    };
}
