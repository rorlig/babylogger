package com.rorlig.babylog.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.rorlig.babylog.R;
import com.rorlig.babylog.dagger.ActivityModule;
import com.rorlig.babylog.dagger.ObjectGraphActivity;
import com.rorlig.babylog.dagger.ObjectGraphUtils;
import com.rorlig.babylog.otto.ScopedBus;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import dagger.ObjectGraph;

//import butterknife.;

/**
 * Created by admin on 12/14/13.
 * @author gaurav gupta
 *
 *
 */
public class InjectableActivity extends AppCompatActivity implements ObjectGraphActivity
//        , GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener
    {

    private ObjectGraph activityGraph;

    @Inject
    public ScopedBus scopedBus;

    private String TAG = "InjectableActivity";


    @Override
    public void inject(Object paramObject) {
       Log.d(TAG, "injecting " + paramObject);
       activityGraph.inject(paramObject);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityGraph = ObjectGraphUtils.getApplicationGraph(this).plus(getModules().toArray());
        activityGraph.inject(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityGraph = null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.scopedBus.paused();
    }

    @Override
    protected void onResume(){
        super.onResume();
        scopedBus.resumed();
    }


    protected List<Object> getModules(){
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = new ActivityModule(this);
//        arrayOfObject[1] = new SocialActivityModule(this, this, this);
        return Arrays.asList(arrayOfObject);
    }
    public void onContentChanged(){
        super.onContentChanged();
        ButterKnife.inject(this);
    }


    public ObjectGraph getActivityGraph() {
        return activityGraph;
    }

    public void setActivityGraph(ObjectGraph activityGraph) {
        this.activityGraph = activityGraph;
    }

    public ScopedBus getScopedBus() {
        return scopedBus;
    }

    public void setScopedBus(ScopedBus scopedBus) {
        this.scopedBus = scopedBus;
    }


    protected void showFragment(Class<?> paramClass,String paramString, boolean addToStack ){
        Log.d(TAG, "showFragment for " + paramClass);

        FragmentManager localFragmentManager = getSupportFragmentManager();



        Fragment localFragment = localFragmentManager.findFragmentById(R.id.fragment_container);

        if ((localFragment==null)||(!paramClass.isInstance(localFragment))){
            Log.d(TAG, "adding to back stack ");
            try {
                localFragment = (Fragment)paramClass.newInstance();
                if (addToStack) {
                    localFragmentManager.beginTransaction()
                            .add(R.id.fragment_container, localFragment)
                            .addToBackStack("main_screen_stack")
                            .commit();
                } else {
                    localFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, localFragment)
                            .commit();
                }

            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
//
//    /*
//      * handle play services callback...
//     */
//    @Override
//    public void onConnected(Bundle bundle) {
//
//    }
//
//    /*
//     * handle play services callback...
//     */
//    @Override
//    public void onDisconnected() {
//
//    }
//
//    /*
//    * handle play services callback...
//    */
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//
//    }
}
