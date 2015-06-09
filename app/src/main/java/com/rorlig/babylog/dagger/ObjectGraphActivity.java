package com.rorlig.babylog.dagger;

import dagger.ObjectGraph;

/**
 * Created by gaurav
 */
public interface ObjectGraphActivity {
    public ObjectGraph getActivityGraph();
    public void inject(Object paramObject);
}
