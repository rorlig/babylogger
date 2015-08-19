package com.rorlig.babyapp.dagger;

import dagger.ObjectGraph;

/**
 * @author gaurav gupta
 */
public interface ObjectGraphActivity {
    ObjectGraph getActivityGraph();
    void inject(Object paramObject);
}
