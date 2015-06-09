package com.rorlig.babylog.dagger;

import dagger.ObjectGraph;

/**
 * Created by gaurav
 */
public interface ObjectGraphProvider {
    public abstract ObjectGraph getApplicationGraph();
    public abstract void inject(Object paramObject);
}
