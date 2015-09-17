package com.rorlig.babyapp.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.parse_dao.BabyLogBaseParseObject;
import com.rorlig.babyapp.ui.adapter.parse.ArrayAdapter;

import java.util.ArrayList;
import java.util.Date;
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
    protected SwipeRefreshLayout swipeRefreshLayout;

    @Optional
    @InjectView(R.id.snackbar)
    CoordinatorLayout snackBarLayout;


    @Optional
    @InjectView(R.id.list_parse)
    protected UltimateRecyclerView ultimateRecyclerView;

    @Optional
    @InjectView(R.id.emptyView)
    RelativeLayout emptyView;

    protected int skip = 0 ;
    protected int limit = 10;


    protected List<ParseObject> parseObjectList = new ArrayList<>();
    protected ArrayAdapter baseParseAdapter2;


    public BaseInjectableListFragment(String parseClassName) {
        this.parseClassName = parseClassName;
    }

    /*
     *
     */
    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);

        updateListView();

        if (ultimateRecyclerView!=null) {

            ultimateRecyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
                @Override
                public void loadMore(final int itemsCount, final int maxLastVisiblePosition) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            Log.d(TAG, "onLoadMore itemsCount " + itemsCount + " maxLastVisiblePosition " + maxLastVisiblePosition);
//                       setSkip(itemsCount);
                            updateListView(itemsCount);
                        }
                    }, 1000);
                }
            });


            ultimateRecyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
//                Date logCreationTime = diaperChangeList.size()==0? null: ((BabyLogBaseParseObject)(diaperChangeList.get(0))).getLogCreationDate();
//                populateLatestFromNetwork(logCreationTime);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            Log.d(TAG, "refresing");
                            Date logCreationTime = parseObjectList.size() == 0 ? null : ((BabyLogBaseParseObject) (parseObjectList.get(0))).getLogCreationDate();
                            populateLatestFromNetwork(logCreationTime);
//                        ultimateRecyclerView.setRefreshing(false);


                        }
                    }, 1000);
                }
            });


        }


//        updateListView();
    }

    /*
     * updatelistview  -- first check local store then network
     */
    public void updateListView() {
        populateLocalStore(true);
    }


    /*
     * updatelistview  -- first check local store then network
     * @param - skip - number of records to skip
     */

    public void updateListView(int skip) {
        this.skip = skip;
        populateLocalStore(true);
    }


    /*
     * get records from local store first and then the network if checkNetwork is true
     *
     */

    protected void populateLocalStore(final boolean checkNetwork) {
        Log.d(TAG, "populateLocaStore");
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

    /*
     * populateFromNetwork - get records from network...
     * @param data - not sure why this is needed...
     */
    protected void populateFromNetwork(final List<ParseObject> data) {
        Log.d(TAG, "populateFromNetwork");
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

    protected void populateLatestFromNetwork(Date lastLogCreationDate){
        getSyncQuery(lastLogCreationDate).findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                Log.d(TAG, "new items got from the network " + objects + "e " + e);

                if (e == null) {
                        if (objects != null) {
                            ParseObject.pinAllInBackground(parseClassName, objects);
                            appendListResults(objects);
//                            setListResults(objects);
                        }
                } else {
                    Log.d(TAG, "exception " + e);
                    setError(e);
                }
            }
        });
    }


    private ParseQuery<ParseObject> getBaseQuery() {
        Log.d(TAG, "getBaseQuery skip: " + skip + " limit: " + limit);
        final ParseQuery<ParseObject> query = ParseQuery.getQuery(parseClassName);
        query.orderByDescending("logCreationDate");
        query.setLimit(limit);
        query.setSkip(skip);
        return query;
    }


    /*
     * getSyncQuery -- gets the logs created after the logCreationDate
     * param - Date logCreationDate - date on which the last item was created...
     */
    private ParseQuery<ParseObject> getSyncQuery(Date logCreationDate) {
        Log.d(TAG, "getSyncQuery " + logCreationDate);
        final ParseQuery<ParseObject> query = ParseQuery.getQuery(parseClassName);
        query.orderByDescending("logCreationDate");
        query.whereGreaterThan("logCreationDate", logCreationDate != null ? logCreationDate : -1);
        return query;
    }

    private void setError(ParseException e) {
        Log.d(TAG, "error in parse network call" + e.getMessage() + " " + getActivity() + " view ");
        swipeRefreshLayout.setRefreshing(false);
        Snackbar.make(snackBarLayout, e.getMessage(), Snackbar.LENGTH_LONG)
                .show();
    }

    protected void setListResults(List<ParseObject> objects) {
        swipeRefreshLayout.setRefreshing(false);
        if (objects!=null && objects.size()>0) {
            Log.d(TAG, "adding " + objects.size() + " to the list");
            parseObjectList.addAll(objects);
            baseParseAdapter2.notifyDataSetChanged();
        }

        if (parseObjectList.size() > 0) {
            ultimateRecyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        } else {
            ultimateRecyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);

        }

    }

    protected void appendListResults(List<ParseObject> objects) {

        Log.d(TAG, "appendListResults");
        if (objects!=null && objects.size()>0) {
            Log.d(TAG, "adding objects " + objects);
            parseObjectList.addAll(0, objects);
            baseParseAdapter2.notifyDataSetChanged();
            scrollLayoutManagerToPos(0);
        }

        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }

    }

    protected void scrollLayoutManagerToPos(int position) {

        //since the item is added to the top of the screen scroll up..
        LinearLayoutManager layoutManager = (LinearLayoutManager) ultimateRecyclerView.mRecyclerView.getLayoutManager();
        if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
            layoutManager.scrollToPosition(position);
        }

    }


//    protected void addItem(ParseObject object) {
//
//        Log.d(TAG, "addItem");
//
//    }
//
//    public void setSkip(int skip) {
//        this.skip = skip;
//    }
//
//    public void setLimit(int limit) {
//        this.limit = limit;
//    }
}
