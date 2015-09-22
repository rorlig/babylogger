package com.rorlig.babyapp.ui.fragment.feed;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.parse.ParseObject;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.otto.events.other.AddItemEvent;
import com.rorlig.babyapp.otto.events.other.AddItemTypes;
import com.rorlig.babyapp.otto.events.ui.FragmentCreated;
import com.rorlig.babyapp.ui.adapter.parse.FeedAdapter2;
import com.rorlig.babyapp.ui.fragment.BaseInjectableListFragment;
import com.rorlig.babyapp.utils.AppConstants;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by rorlig on 7/18/14.
 * @author gaurav gupta
 * history of feeds changes
 */
public class FeedingListFragment extends BaseInjectableListFragment {



    @InjectView(R.id.feed_item_types)
    FloatingActionsMenu floatingActionsMenu;


    public FeedingListFragment() {
        super(AppConstants.PARSE_CLASS_FEED);
    }


    @OnClick(R.id.add_item)
    public void onFeedEventAdd(){
        showFeedSelectFragment(new FeedSelectFragment(), "feed_select");
    }

    @OnClick(R.id.add_breast_feed)
    public void onAddBreastFeedBtnClicked(){
        collapseFloatingMenuIfOpen();
        scopedBus.post(new AddItemEvent(AddItemTypes.FEED_NURSING));
    }

    @OnClick(R.id.add_bottle_feed)
    public void onAddBottleFeedBtnClicked(){
        collapseFloatingMenuIfOpen();
        scopedBus.post(new AddItemEvent(AddItemTypes.FEED_BOTTLE));
    }

    private void showFeedSelectFragment(FeedSelectFragment feedSelectFragment, String s) {
        feedSelectFragment.show(getActivity().getSupportFragmentManager(),"feed_select");
    }

    private String TAG = "FeedingListFragment";

//    private EventListener eventListener = new EventListener();

    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);
        scopedBus.post(new FragmentCreated("Feeding List"));
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        baseParseAdapter2 = new FeedAdapter2(parseObjectList);
        ultimateRecyclerView.setAdapter(baseParseAdapter2);
        ultimateRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        ultimateRecyclerView.enableLoadmore();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_feeding_list_2, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    protected void setListResults(List<ParseObject> objects) {
        Log.d(TAG, "setListResults");
        super.setListResults(objects);
//        sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter(getActivity().getApplicationContext(),
//                R.layout.section_header, R.id.title, baseParseAdapter2, new DateSectionizer());

//        sectionedRecyclerViewAdapter.setSections(objects);


//        sectionedRecyclerViewAdapter.notifyDataSetChanged();
        ultimateRecyclerView.setAdapter(baseParseAdapter2);


    }

// s

//    /*
//     * Unregister from events ...
//     */
//    @Override
//    public void onStop(){
//        super.onStop();
//        Log.d(TAG, "onStop");
//        scopedBus.unregister(eventListener);
//
//    }
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//
//            case R.id.action_add:
//                showFeedSelectFragment(new FeedSelectFragment(), "feed_select");
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_feed, menu);
    }




//    // event listener to listen to events on the buss
//    private class EventListener {
//        public EventListener() {
//
//        }
//
//        //handle the addition or editing of item from list view...
//        // position == -1 in case of addition else a non negative number ...
//        @Subscribe
//        public void onItemAdded(final ItemCreatedOrChanged event) {
//            Log.d(TAG, "onDiaperChangeItemChange");
//            final ParseQuery<ParseObject> query = ParseQuery.getQuery("Feed");
//            query.orderByDescending("logCreationDate");
//            query.setLimit(1);
//            query.setSkip(event.getPosition() == -1 ? 0 : event.getPosition());
////
////
//            query.fromLocalDatastore().findInBackground(
//                    new FindCallback<ParseObject>() {
//                        @Override
//                        public void done(List<ParseObject> objects, com.parse.ParseException e) {
//                            Log.d(TAG, "got list from the cache " + e + " objects " + objects);
//                            if (objects!=null) {
//                                Log.d(TAG, "adding objects to the list " + event.getPosition());
//                                if (event.getPosition()==-1) {
//                                    Log.d(TAG, "adding item to position -1 ");
//                                    //new item added
//                                    parseObjectList.add(0, objects.get(0));
//                                    baseParseAdapter2.notifyItemInserted(0);
//                                    baseParseAdapter2.notifyDataSetChanged();
//                                    scrollLayoutManagerToPos(0);
//
//                                } else {
//                                    //item edited...
//                                    Log.d(TAG, "editing the items ");
//                                    parseObjectList.set(event.getPosition(), objects.get(0));
//                                    baseParseAdapter2.notifyItemChanged(event.getPosition());
//                                }
//
//
//
//                            }
//                        }
//                    }
//
//
//            );
//
//        }
//
//        //handle the removal of an item from the listview.
//        @Subscribe
//        public void onItemDeleted(final ItemDeleted event) {
//            parseObjectList.remove(event.getPosition());
//            baseParseAdapter2.notifyItemRemoved(event.getPosition());
//        }
//
//
//    }

    /*
     * click on anywhere in fragment ...
     */
    public void fragmentClicked(View v) {
       Log.d(TAG, "fragmentClicked");
      collapseFloatingMenuIfOpen();
    }

    /*
     * collapse floating menu if open
     */
    private void collapseFloatingMenuIfOpen() {
        Log.d(TAG, "collapseFloatingMenu  " + floatingActionsMenu.isExpanded());
        if (floatingActionsMenu.isExpanded()) {
            floatingActionsMenu.collapse();
        }
    }


    //collapse the floating menu on back stack popup....
    @Override
    public void onFragmentResume() {
        collapseFloatingMenuIfOpen();
        super.onFragmentResume();
    }




}
