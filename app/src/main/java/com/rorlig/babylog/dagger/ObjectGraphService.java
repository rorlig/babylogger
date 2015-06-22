package com.rorlig.babylog.dagger;

import dagger.ObjectGraph;

/**
 * @author gaurav gupta
 */
public interface ObjectGraphService {
     ObjectGraph getApplicationGraph();
     void inject(Object paramObject);
}
