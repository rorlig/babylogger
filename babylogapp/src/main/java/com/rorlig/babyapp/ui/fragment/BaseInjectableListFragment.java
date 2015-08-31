package com.rorlig.babyapp.ui.fragment;

import android.os.Bundle;
import android.util.Log;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * @author gaurav gupta
 */
public abstract class BaseInjectableListFragment extends InjectableFragment {


    private final String parseClassName;
    private String TAG = "BaseInjectableListFragment";

    public BaseInjectableListFragment(String parseClassName) {
        this.parseClassName = parseClassName;
    }

    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);
//        updateListView();
    }

    public void updateListView() {
        populateLocalStore();
    }

    private void populateLocalStore() {
        final ParseQuery<ParseObject> query = ParseQuery.getQuery(parseClassName);
        query.orderByDescending("logCreationDate");
        query.fromLocalDatastore();
//        query.fromPin(parseClassName);

//        Log.d(TAG, " isCached " + query.fromPin(parseClassName));

//        ParseObject.pin
        query.findInBackground(
                new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, com.parse.ParseException e) {
                        Log.d(TAG, "got list from the cache " + e);
                        if (e == null) {
                            Log.d(TAG, "number of items " + objects.size());
                            if (objects.size() == 0) {
                                populateFromNetwork(objects);
                            } else {
                                setListResults(objects);

                            }
                        } else {
                            Log.d(TAG, "exception " + e);
                        }
                    }
                }


        );
    }

    private void populateFromNetwork(final List<ParseObject> data) {
        Log.d(TAG, "populateFromNetwork");
        final ParseQuery<ParseObject> query = ParseQuery.getQuery(parseClassName);
        query.orderByDescending("createdAt");

        query.findInBackground(
                new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, com.parse.ParseException e) {
                        Log.d(TAG, "got list from the network");
                        if (e == null) {
                            Log.d(TAG, "number of items " + objects.size());
//                            if(objects.size()==0) {
//                                populateFromNetwork();
//                            } else {

                            ParseObject.unpinAllInBackground(parseClassName, data, new DeleteCallback() {
                                @Override
                                public void done(com.parse.ParseException e) {
                                    Log.d(TAG, "deleted " + parseClassName + " pin " + e);
                                }

                            });
                            ParseObject.pinAllInBackground(parseClassName, objects);
                            setListResults(objects);
                        } else {
                            Log.d(TAG, "exception " + e);
                        }
                    }
                }
        );
    }

    protected abstract void setListResults(List<ParseObject> objects);

}
