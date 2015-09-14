package com.rorlig.babyapp.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.rorlig.babyapp.R;

import java.util.List;

import butterknife.InjectView;
import butterknife.Optional;

/**
 * @author gaurav gupta
 */
public abstract class BaseInjectableListFragment extends InjectableFragment {


    private final String parseClassName;
    private String TAG = "BaseInjectableListFragment";

    @Optional
    @InjectView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Optional
    @InjectView(R.id.snackbar)
    CoordinatorLayout snackBarLayout;
    protected int skip = 0 ;
    protected int limit = 10;

    public BaseInjectableListFragment(String parseClassName) {
        this.parseClassName = parseClassName;
    }

    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                AppUtils.invalidateParseCache(parseClassName, getActivity());
                populateFromNetwork(null);
            }
        });

//        updateListView();
    }

    public void updateListView() {
        populateLocalStore(true);
    }

    public void updateListView(int page) {
        skip = (page-1)*limit;
        populateLocalStore(true);
    }

    protected void populateLocalStore(final boolean checkNetwork) {
//        final ParseQuery<ParseObject> query = ParseQuery.getQuery(parseClassName);
//        query.orderByDescending("logCreationDate");
//        query.fromLocalDatastore();
//

//        query.fromPin(parseClassName);

//        Log.d(TAG, " isCached " + query.fromPin(parseClassName));

//        ParseObject.pin
        getBaseQuery().fromLocalDatastore().findInBackground(
                new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, com.parse.ParseException e) {
                        Log.d(TAG, "got list from the cache " + e);
                        if (e == null) {
                            Log.d(TAG, "number of items " + objects.size());
                            if (objects.size() == 0 && checkNetwork) {
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

    protected void populateFromNetwork(final List<ParseObject> data) {
//        Log.d(TAG, "populateFromNetwork");
//        final ParseQuery<ParseObject> query = ParseQuery.getQuery(parseClassName);
//        query.orderByDescending("createdAt");

        getBaseQuery().findInBackground(
                new FindCallback<ParseObject>() {
                    @Override
                    public void done(final List<ParseObject> objects, com.parse.ParseException e) {
                        Log.d(TAG, "got list from the network");
                        if (e == null) {
                            Log.d(TAG, "number of items " + objects.size());
//                            if(objects.size()==0) {
//                                populateFromNetwork();
//                            } else {
                            if (data != null) {
                                ParseObject.unpinAllInBackground(parseClassName, data, new DeleteCallback() {
                                    @Override
                                    public void done(com.parse.ParseException e) {
                                        Log.d(TAG, "deleted " + parseClassName + " pin " + e);
                                        ParseObject.pinAllInBackground(parseClassName, objects);

                                    }

                                });
                            } else {
                                ParseObject.pinAllInBackground(parseClassName, objects);
                            }

                            setListResults(objects);
                        } else {
                            Log.d(TAG, "exception " + e);
                            setError(e);
                        }
                    }
                }
        );
    }

    private ParseQuery getBaseQuery() {
        Log.d(TAG, "getBaseQuery skip: " + skip + " limit: " + limit);
        final ParseQuery<ParseObject> query = ParseQuery.getQuery(parseClassName);
        query.orderByDescending("logCreationDate");
        query.setLimit(limit);
        query.setSkip(skip);
        return query;
    }

    private void setError(ParseException e) {
        Log.d(TAG, "error in parse network call" + e.getMessage() + " " + getActivity() + " view " );
        swipeRefreshLayout.setRefreshing(false);
        Snackbar.make(snackBarLayout, e.getMessage(), Snackbar.LENGTH_LONG)
                .show();
    }

    protected void setListResults(List<ParseObject> objects) {
        swipeRefreshLayout.setRefreshing(false);
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
