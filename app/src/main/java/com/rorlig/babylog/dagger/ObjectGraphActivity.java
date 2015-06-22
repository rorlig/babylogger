package com.rorlig.babylog.dagger;

import dagger.ObjectGraph;

/**
 * Created by gaurav
 */
public interface ObjectGraphActivity {
    ObjectGraph getActivityGraph();
    void inject(Object paramObject);
}
