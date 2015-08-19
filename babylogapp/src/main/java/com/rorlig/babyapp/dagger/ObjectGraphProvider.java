package com.rorlig.babyapp.dagger;

import dagger.ObjectGraph;

/**
 * @author gaurav gupta
 */
public interface ObjectGraphProvider {
     ObjectGraph getApplicationGraph();
     void inject(Object paramObject);
}
