package com.rorlig.babyapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.rorlig.babyapp.dagger.ObjectGraphService;
import com.rorlig.babyapp.dagger.ObjectGraphUtils;
import com.rorlig.babyapp.otto.ScopedBus;

import javax.inject.Inject;

import dagger.ObjectGraph;

/**
 *  @author gaurav gupta
 *  Base Service Injected into the ObjectGraph
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
