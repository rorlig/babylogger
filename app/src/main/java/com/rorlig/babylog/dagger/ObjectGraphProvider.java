package com.rorlig.babylog.dagger;

import dagger.ObjectGraph;

/**
 * @author gaurav gupta
 */
public interface ObjectGraphProvider {
     ObjectGraph getApplicationGraph();
     void inject(Object paramObject);
}
