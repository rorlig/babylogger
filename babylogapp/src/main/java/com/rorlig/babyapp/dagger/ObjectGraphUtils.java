package com.rorlig.babyapp.dagger;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import dagger.ObjectGraph;

/**
 * @author gaurav gupta
 * Utils class to Inject things into the Object Graph.
 */
public class ObjectGraphUtils {

    private static String TAG = "ObjectGraphUtils";

    public static ObjectGraph getApplicationGraph(Context paramApplication) {
        ObjectGraph  objectGraph = ((ObjectGraphApplication)paramApplication).getApplicationGraph();
        return objectGraph;
    }


    public static ObjectGraph getApplicationGraph(Activity paramActivity) {
        Log.d(TAG, "paramActivity " + paramActivity + " application " + paramActivity.getApplication());
        ObjectGraph  objectGraph = ((ObjectGraphApplication)paramActivity.getApplication()).getApplicationGraph();
        Log.d(TAG, "objectGraph is " + objectGraph);
        return objectGraph;
//        ((ObjectGraphApplication)paramActivity.getApplication()).getApplicationGraph();
    }

    public static ObjectGraph getApplicationGraph(Service paramService){
        return ((ObjectGraphApplication)paramService.getApplication()).getApplicationGraph();
    }

    public static ObjectGraph getApplicationGraph(BroadcastReceiver paramBroadcastReceiver, Context paramContext){
        return ((ObjectGraphApplication)paramContext.getApplicationContext()).getApplicationGraph();
    }

    public static ObjectGraph getApplicationGraph(ContentProvider paramContentProvider){
        return ((ObjectGraphApplication)paramContentProvider.getContext().getApplicationContext()).getApplicationGraph();
    }

    public static ObjectGraph getApplicationGraph(Fragment paramFragment){
        FragmentActivity localFragmentActivity = paramFragment.getActivity();
        if (localFragmentActivity == null)
            throw new IllegalStateException("Attempting to get Activity before it has been attached to " + paramFragment.getClass().getName());
        return ((ObjectGraphApplication)localFragmentActivity.getApplication()).getApplicationGraph();
    }

    public static ObjectGraph getApplicationGraph(View paramView){
        return ((ObjectGraphApplication)paramView.getContext().getApplicationContext()).getApplicationGraph();
    }


    public static void inject(Activity paramActivity){
        ((ObjectGraphApplication)paramActivity.getApplication()).inject(paramActivity);
    }

    public static void inject(Service paramService){
        ((ObjectGraphApplication)paramService.getApplication()).inject(paramService);
    }

    public static void inject(BroadcastReceiver paramBroadcastReceiver, Context paramContext){
        ((ObjectGraphApplication)paramContext.getApplicationContext()).inject(paramBroadcastReceiver);
    }

    public static void inject(ContentProvider paramContentProvider){
        ((ObjectGraphApplication)paramContentProvider.getContext().getApplicationContext()).inject(paramContentProvider);
    }

    public static void inject(Fragment paramFragment){
        FragmentActivity localFragmentActivity = paramFragment.getActivity();
        if (localFragmentActivity == null)
            throw new IllegalStateException("Attempting to get Activity before it has been attached to " + paramFragment.getClass().getName());
        ((ObjectGraphApplication)localFragmentActivity.getApplication()).inject(paramFragment);
    }

    public static void inject(View paramView){
        ((ObjectGraphApplication)paramView.getContext().getApplicationContext()).inject(paramView);
    }


}
