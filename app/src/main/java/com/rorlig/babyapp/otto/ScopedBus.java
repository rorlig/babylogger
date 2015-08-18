package com.rorlig.babyapp.otto;

import com.squareup.otto.Bus;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.inject.Inject;

/**
 * Created by gaurav
 */
public class ScopedBus {
    private boolean active;
    private final Bus mBus = BusProvider.getInstance();
    private final Set<Object> mObjects = new HashSet();

    @Inject
    public ScopedBus() {
    }

    public void paused() {
        this.active = false;
        Iterator localIterator = this.mObjects.iterator();
        while (localIterator.hasNext()){
            Object localObject = localIterator.next();
            this.mBus.unregister(localObject);
        }
    }

    public void post(Object paramObject) {
        this.mBus.post(paramObject);
    }

    public void register(Object paramObject){
        this.mObjects.add(paramObject);
        if (this.active)
            this.mBus.register(paramObject);
    }

    public void resumed() {
        this.active = true;
        Iterator localIterator = this.mObjects.iterator();
        while (localIterator.hasNext()){
            Object localObject = localIterator.next();
            this.mBus.register(localObject);
        }
    }

    public void unregister(Object paramObject) {
        this.mObjects.remove(paramObject);
        if (this.active)
            this.mBus.unregister(paramObject);
    }
}