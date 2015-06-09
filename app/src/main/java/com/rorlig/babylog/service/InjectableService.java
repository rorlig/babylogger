package com.rorlig.babylog.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


import com.rorlig.babylog.dagger.ObjectGraphService;
import com.rorlig.babylog.dagger.ObjectGraphUtils;
import com.rorlig.babylog.otto.ScopedBus;

import javax.inject.Inject;

import dagger.ObjectGraph;

/**
 * Created by Gaurav on 5/25/14.
 */
public class InjectableService extends Service implements ObjectGraphService {

    @Inject
    ScopedBus scopedBus;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public ObjectGraph getApplicationGraph() {
        return ObjectGraphUtils.getApplicationGraph(this);
    }

    @Override
    public void inject(Object paramObject) {
        getApplicationGraph().inject(paramObject);

    }

    @Override
    public void onCreate() {
        getApplicationGraph().inject(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


}
