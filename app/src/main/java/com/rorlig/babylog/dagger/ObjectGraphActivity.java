package com.rorlig.babylog.dagger;

import dagger.ObjectGraph;

/**
 * @author gaurav gupta
 */
public interface ObjectGraphActivity {
    ObjectGraph getActivityGraph();
    void inject(Object paramObject);
}
