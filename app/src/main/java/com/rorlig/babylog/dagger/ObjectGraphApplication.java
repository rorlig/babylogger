package com.rorlig.babylog.dagger;

import dagger.ObjectGraph;

/**
 * Created by gaurav
 */
public interface ObjectGraphApplication {
      ObjectGraph getApplicationGraph();
      void inject(Object paramObject);
}
