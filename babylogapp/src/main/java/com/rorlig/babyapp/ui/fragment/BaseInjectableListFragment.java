package com.rorlig.babyapp.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.otto.ItemDeleted;
import com.rorlig.babyapp.otto.events.growth.ItemCreatedOrChanged;
import com.rorlig.babyapp.otto.events.stats.StatsItemEvent;
import com.rorlig.babyapp.parse_dao.BabyLogBaseParseObject;
import com.rorlig.babyapp.ui.adapter.parse.ArrayAdapter;
import com.rorlig.babyapp.ui.adapter.parse.ArrayAdapterFactory;
import com.rorlig.babyapp.utils.AppUtils;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    protected EventListener eventListener = new EventListener();
    private ParseQuery<ParseObject> query;


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



//        updateListView();
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        baseParseAdapter2 = ArrayAdapterFactory.getAdapter(parseClassName, parseObjectList);
        if (ultimateRecyclerView!=null) {
            ultimateRecyclerView.setAdapter(baseParseAdapter2);
            ultimateRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }


    }

    /*
        * Register to events...
        */
    @Override
    public void onStart(){


        super.onStart();
        Log.d(TAG, "onStart");
        scopedBus.register(eventListener);
    }

    /*
     * Unregister from events ...
     */
    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "onStop");
        scopedBus.unregister(eventListener);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_stats:
                scopedBus.post(new StatsItemEvent());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void setupRecyclerView() {
        if (ultimateRecyclerView!=null) {
            ultimateRecyclerView.enableLoadmore();
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


            //item decorator
//            RecyclerView.ItemDecoration itemDecoration = new
//                    DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
//
//            ultimateRecyclerView.addItemDecoration(itemDecoration);
        }
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
        if (!AppUtils.isNetworkAvailable(getActivity())){
            showErrorIfNotConnected();
            setListResults(data);
        } else {
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

    }

    protected void populateLatestFromNetwork(Date lastLogCreationDate){
        if (!AppUtils.isNetworkAvailable(getActivity())) {
            showErrorIfNotConnected();
            swipeRefreshLayout.setRefreshing(false);
        } else {
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

    }


    private ParseQuery<ParseObject> getBaseQuery() {
        Log.d(TAG, "getBaseQuery skip: " + skip + " limit: " + limit);
        query = ParseQuery.getQuery(parseClassName);
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
        if (objects != null && objects.size() > 0) {
            Log.d(TAG, "adding " + objects.size() + " to the list");
            parseObjectList.addAll(objects);
//            baseParseAdapter2.notifyDataSetChanged();
        }

        showHideListView();



    }

    protected void showHideListView(){
        if (parseObjectList.size() > 0) {
            ultimateRecyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            setupRecyclerView();

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

    private class EventListener {


        //handle the addition or editing of item from list view...
        // position == -1 in case of addition else a non negative number ...
        @Subscribe
        public void onItemAdded(final ItemCreatedOrChanged event) {
            Log.d(TAG, "onItemAdded");
            final ParseQuery<ParseObject> query = ParseQuery.getQuery(parseClassName);
            query.orderByDescending("logCreationDate");
            query.fromLocalDatastore().findInBackground(
                    new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, com.parse.ParseException e) {
                            Log.d(TAG, "got list from the cache " + e + " objects " + objects);
                            parseObjectList.clear();
                            Log.d(TAG, "adding objects to the list " + event.getPosition());
                            parseObjectList.addAll(objects);
                            sort(parseObjectList);
                            baseParseAdapter2.notifyDataSetChanged();
                            showHideListView();
                        }
                    });
        }


        //handle the removal of an item from the listview.
        @Subscribe
        public void onItemDeleted(final ItemDeleted event) {
            parseObjectList.remove(event.getPosition());
            baseParseAdapter2.notifyItemRemoved(event.getPosition());
            showHideListView();
        }

    }

    /*
     * sorts the parse object list based on the log creation date...
     */
    private void sort(List<ParseObject> parseObjectList) {
        Collections.sort(parseObjectList, new Comparator<ParseObject>() {
            @Override
            public int compare(ParseObject lhs, ParseObject rhs) {
                return ((BabyLogBaseParseObject) rhs).getLogCreationDate()
                        .compareTo(((BabyLogBaseParseObject) lhs).getLogCreationDate());
            }
        });
    }

}
