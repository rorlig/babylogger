package com.rorlig.babylog.ui.fragment;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Address;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.rorlig.babylog.R;
import com.rorlig.babylog.dagger.ForActivity;
import com.rorlig.babylog.scheduler.TypeFaceManager;
import com.squareup.otto.Subscribe;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;



/**
 * Created by gaurav on 12/16/13.
 * LoggingFragment class maintains a list of Events from EventBrite that are nearby based on the appropriate filters...
 * @author gaurav gupta
 *
 */
public class MainFragment extends InjectableFragment  {


    @ForActivity
    @Inject
    Context context;

    @Inject
    ActionBar mActionBar;



    private EventListener eventListener = new EventListener();


    @InjectView(R.id.list)
    ListView listView;

    @InjectView(R.id.progressBar1)
    ProgressBar progressBar;





    @Inject
    TypeFaceManager typeFaceManager;

    private Typeface typeface;

    private Typeface typefaceSemiBold;


    private String[] mItems;






    private String TAG = "LoggingFragment";




    @Override
    public void onActivityCreated(final Bundle paramBundle) {
        super.onActivityCreated(paramBundle);
        Log.d(TAG, "onActivityCreated() + context is " + context);
//        Log.d(TAG, "userDataManager: " + userDataManager);
//        if (paramBundle!=null) {
//            Log.d(TAG, "param bundle is not null");
//            eventArrayList = paramBundle.getParcelableArrayList("event_list");
//            Log.d(TAG, "eventArrayList is " + eventArrayList);
//            if (eventArrayList!=null) {
//                Log.d(TAG, "eventArrayList size " + eventArrayList.size());
//            }
//            progressBar.setVisibility(View.GONE);
//            listView.setVisibility(View.VISIBLE);
//        }
//
//        if (eventArrayList==null||eventArrayList.size()==0) {
//            Log.d(TAG, "array list is null reload");
////            jobManager.addJob(new GetEventsJob(null));
////            eventClient.getEvents(context, 1, greetSharedPreferences);
////            getUserJob = new GetUserJob()
////            GetUserJob getUserJob = ((ObjectGraphActivity)getActivity()).getActivityGraph().get(GetUserJob.class);
////               new GetUserJob()
////            getUserJob.s
////            jobManager.addJob(new GetUserJob(null, greetSharedPreferences.getSharedPreferences().getString(AppConstants.YUSER_ID,"")));
//            jobManager.addJob(getUserJob);
//        } else {
//            Log.d(TAG, "array list from savedInstance");
//        }

//        mItems = context.getResources().getStringArray(R.array.filter_event_types);

//        eventAdapter = new EventAdapter(getActivity(), R.layout.list_item_event_greet, eventArrayList,
//                typeFaceManager);
//        listView.setAdapter(eventAdapter);
//        gridView.setOnItemClickListener(new ListItemClickListener());
        listView.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                //check if this is correct way...
                Log.d(TAG, "onLoadMore page: " + page + " totalItemsCount:  " + totalItemsCount);
//                eventArrayList = paramBundle.getParcelableArrayList("event_list");
//                if (eventArrayList==null) {
//                    Log.d(TAG, "array list is null reload");
//                eventClient.getEvents(getActivity().getApplicationContext(), page);
////                } else {
////                    Log.d(TAG, "array list from savedInstance");
////                }
////                eventAdapter.set
////                eventAdapter;
//                eventAdapter = new EventAdapter(getActivity(), 0, eventArrayList, typeFaceManager);
//                listView.setAdapter(eventAdapter);
//                eventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
//                listView.closeOpenedItems();
            }
        });

//        listView.setSwipeListViewListener(new BaseSwipeListViewListener() {
//            @Override
//            public void onOpened(int position, boolean toRight) {
//            }
//
//            @Override
//            public void onClosed(int position, boolean fromRight) {
//            }
//
//            @Override
//            public void onListChanged() {
//            }
//
//            @Override
//            public void onMove(int position, float x) {
//                Log.d("swipe", "moved");
//                super.onMove(position,x);
////                gridView.
////                gridView.closeOpenedItems();
//            }
//
//            @Override
//            public void onStartOpen(int position, int action, boolean right) {
////                gridView.closeOpenedItems();
////                gridView.
//                Log.d("swipe", String.format("onStartOpen %d - action %d", position, action));
//            }
//
//            @Override
//            public void onStartClose(int position, boolean right) {
//                Log.d("swipe", String.format("onStartClose %d", position));
//            }
//
//            @Override
//            public void onClickFrontView(int position) {
//                Log.d("swipe", String.format("onClickFrontView %d", position));
//                selectEvent(position);
//            }
//
//            @Override
//            public void onClickBackView(int position) {
//                Log.d("swipe", String.format("onClickBackView %d", position));
//            }
//
//            @Override
//            public void onDismiss(int[] reverseSortedPositions) {
////                for (int position : reverseSortedPositions) {
////                    data.remove(position);
////                }
////                adapter     Log.d(TAG, "userDataManager: " + userDataManager);
//        if (paramBundle!=null) {
//            Log.d(TAG, "param bundle is not null");
//            eventArrayList = paramBundle.getParcelableArrayList("event_list");
//            Log.d(TAG, "eventArrayList is " + eventArrayList);
//            if (eventArrayList!=null) {
//                Log.d(TAG, "eventArrayList size " + eventArrayList.size());
//            }
//            progressBar.setVisibility(View.GONE);
//            listView.setVisibility(View.VISIBLE);
//   .notifyDataSetChanged();
//            }
//
//        });

//        getActivity().startService(new Intent(getActivity(), BackgroundLocationService.class));

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, null);
        ButterKnife.inject(this, view);
        return view;
    }

    /*
     * Register to events...
     */
    public void onStart(){
        super.onStart();
        scopedBus.register(eventListener);
    }

    /*
     * Unregister from events ...
     */
    public void onStop(){
        super.onStop();
        scopedBus.unregister(eventListener);
//        getActivity().stopService(new Intent(getActivity(), BackgroundLocationService.class));
    }

    private class EventListener {

        private EventListener() {
        }

//        @Subscribe
//        public void onFilterChanged(FilterChangedEvent filterChangedEvent) {
//            Log.d(TAG, "FilterChangedEvent:  " + filterChangedEvent.getFilterEntry().isActive());
//            //todo - not quite working out...
////            eventArrayList = eventClient.getEvents(context, 1, greetSharedPreferences);
////            Log.d(TAG, "eventArrayList " + eventArrayList + " size " + eventArrayList.size());
////            eventAdapter = new EventAdapter(getActivity().getApplicationContext(), R.layout.list_item_event_greet, eventArrayList);
////            gridView.setAdapter(eventAdapter);
//        }


//
//        @Subscribe
//        public void onLocationChangedEvent(LocationChangedEvent event){
//            Log.d(TAG, "location change event " + event);
//            HashMap<String,String> options = new HashMap<String, String>();
//            options.put("slat", "" +  event.getAddress().getLatitude());
//            options.put("slng", "" +  event.getAddress().getLongitude());
//            jobManager.addJob(new GetEventsJob(null, options));
//        }
//
//        @Subscribe
//        public void onLocationEvent(LocationEvent event){
//            Log.d(TAG, "location event");
//            if (eventArrayList==null||eventArrayList.size()==0) {
//                //don't load events if you are here because of rotation...
//                HashMap<String,String> options = new HashMap<String, String>();
//                options.put("slat", "" +  event.getLocation().getLatitude());
//                options.put("slng", "" +  event.getLocation().getLongitude());
//                jobManager.addJob(new GetEventsJob(null, options));
//            }
//
//        }
//
//        @Subscribe
//        public void onReceivedEventError(ReceivedEventError receivedEventError){
//            Log.d(TAG, "error receiving error");
//            Log.d(TAG, "size " + receivedEventError.getError());
////            eventArrayList.addAll(receivedEventError.getError());
//            listView.setVisibility(View.VISIBLE);
//            progressBar.setVisibility(View.GONE);
//
//        }
//
//        @Subscribe
//        public void onDistanceFilterChanged(DistanceFilterChanged event){
//            Log.d(TAG, "onDistanceFilterChanged");
////            todo requery
//            HashMap<String,String> options = new HashMap<String, String>();
//            Address savedAddress = filterManager.getLocationAddress();
//            options.put("slat", "" +  savedAddress.getLatitude());
//            options.put("slng", "" +  savedAddress.getLongitude());
//            options.put("radius", "" + event.getRadius(event.getValue()));
//            jobManager.addJob(new GetEventsJob(null, options));
//            jobManager.addJob(new GetEventsJob(null, options));
//        }
//
//        @Subscribe
//        public void onTimeFilterChanged(TimeFilterChanged event){
//            Log.d(TAG, "onTimeFilterChanged");
//            //todo requery...
//            HashMap<String,String> options = new HashMap<String, String>();
//            Address savedAddress = filterManager.getLocationAddress();
//            options.put("slat", "" +  savedAddress.getLatitude());
//            options.put("slng", "" +  savedAddress.getLongitude());
//            options.put("date", "" + event.getDateType(event.getValue()));
//            jobManager.addJob(new GetEventsJob(null, options));
//        }
//
//        @Subscribe
//        public void onEventAdded(GetEventsJobsAdded events){
//            Log.d(TAG, "onEventAdded");
//            progressBar.setVisibility(View.VISIBLE);
//            listView.setVisibility(View.GONE);
//        }
//
//
//        @Subscribe
//        public void onEventCompleted(GetEventsCompleted getEventsCompleted){
//            Log.d(TAG, "onEventCompleted");
//            Log.d(TAG, "size " + getEventsCompleted.getEventList().size());
//            eventArrayList.clear();
//            eventArrayList.addAll(getEventsCompleted.getEventList());
//            Log.d(TAG, eventArrayList.toString());
//            eventAdapter.update(eventArrayList);
//            listView.setVisibility(View.VISIBLE);
//            progressBar.setVisibility(View.GONE);
//        }
//
//        @Subscribe
//        public void onGetUserJob(GetUserJobCompleted event){
//            Log.d(TAG, "onGetUserJob account: " + event.getUserObj().getAccounts() + " events " + event.getUserObj().getEvents());
//        }
//
//        @Subscribe
//        public void onLocationServiceError(LocationServicesError locationServicesError){
//            Log.d(TAG, "LocationServiceError " + locationServicesError);
//            showErrorDialog(locationServicesError.getErrorString());
//        }



    }


    private void showErrorDialog(String errorString) {
        FragmentManager fm = getFragmentManager();
//        ErrorDialogFragment errorDialogFragment = ErrorDialogFragment.newInstance(errorString);
//        errorDialogFragment.show(fm, "");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }
    /*
     * Inner class handling the menu item clicks...
     */
    private class ListItemClickListener implements ListView.OnItemClickListener {

        public ListItemClickListener() {
            Log.d(TAG, "ListItemClickListener Constructor");
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d(TAG, "onItemClick position "  + position);
            selectEvent(position);
        }


    }

    private void selectEvent(int position) {
        Log.d(TAG, "selectEvent");
        //need to really event specific information to
//        scopedBus.post(new EventSelectedEvent(eventArrayList.get(position)));
    }

    /*
     * Class to implement endless scroll...
     */
    public abstract class EndlessScrollListener implements AbsListView.OnScrollListener {
        // The minimum amount of items to have below your current scroll position
        // before loading more.
        private int visibleThreshold = 5;
        // The current offset index of data you have loaded
        private int currentPage = 0;
        // The total number of items in the dataset after the last load
        private int previousTotalItemCount = 0;
        // True if we are still waiting for the last set of data to load.
        private boolean loading = true;
        // Sets the starting page index
        private int startingPageIndex = 0;

        public EndlessScrollListener() {
        }

        public EndlessScrollListener(int visibleThreshold) {
            this.visibleThreshold = visibleThreshold;
        }

        public EndlessScrollListener(int visibleThreshold, int startPage) {
            this.visibleThreshold = visibleThreshold;
            this.startingPageIndex = startPage;
            this.currentPage = startPage;
        }

        // This happens many times a second during a scroll, so be wary of the code you place here.
        // We are given a few useful parameters to help us work out if we need to load some more data,
        // but first we check if we are waiting for the previous load to finish.
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            // If the total item count is zero and the previous isn't, assume the
            // list is invalidated and should be reset back to initial state
            // If there are no items in the list, assume that initial items are loading
            if (!loading && (totalItemCount < previousTotalItemCount)) {
                this.currentPage = this.startingPageIndex;
                this.previousTotalItemCount = totalItemCount;
                if (totalItemCount == 0) { this.loading = true; }
            }

            // If it’s still loading, we check to see if the dataset count has
            // changed, if so we conclude it has finished loading and update the current page
            // number and total item count.
            if (loading) {
                if (totalItemCount > previousTotalItemCount) {
                    loading = false;
                    previousTotalItemCount = totalItemCount;
                    currentPage++;
                }
            }

            // If it isn’t currently loading, we check to see if we have breached
            // the visibleThreshold and need to reload more data.
            // If we do need to reload some more data, we execute onLoadMore to fetch the data.
            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                onLoadMore(currentPage + 1, totalItemCount);
                loading = true;
            }
        }

        // Defines the process for actually loading more data based on page
        public abstract void onLoadMore(int page, int totalItemsCount);

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            // Don't take any action on changed
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "saving event list");
//        outState.putParcelableArrayList("event_list", eventArrayList);
    }



}
