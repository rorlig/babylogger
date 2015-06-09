package com.rorlig.babylog.dagger;

import dagger.ObjectGraph;

/**
 * Created by gaurav
 */
public interface ObjectGraphApplication {
    public abstract ObjectGraph getApplicationGraph();
    public abstract void inject(Object paramObject);
}
