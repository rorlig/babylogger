package com.rorlig.babylog.ui.fragment.feed;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gc.materialdesign.views.Button;
import com.mobsandgeeks.adapters.SimpleSectionAdapter;
import com.rorlig.babylog.R;
import com.rorlig.babylog.dagger.ForActivity;
import com.rorlig.babylog.dao.BaseDao;
import com.rorlig.babylog.dao.FeedDao;
import com.rorlig.babylog.otto.events.ui.FragmentCreated;
import com.rorlig.babylog.ui.adapter.DiaperChangeSectionizer;
import com.rorlig.babylog.ui.adapter.FeedAdapter;
import com.rorlig.babylog.ui.fragment.InjectableFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by rorlig on 7/18/14.
 * @author gaurav gupta
 * history of feeds changes
 */
public class FeedingListFragment extends InjectableFragment implements LoaderManager.LoaderCallbacks<List<FeedDao>> {

    @ForActivity
    @Inject
    Context context;


    @InjectView(R.id.feedListView)
    ListView feedListView;

    @InjectView(R.id.emptyView)
    RelativeLayout emptyView;

    @InjectView(R.id.errorText)
    TextView errorText;



    @InjectView(R.id.add_item)
    Button btnFeedAdd;


    private int LOADER_ID=3;
    private List<FeedDao> feedList;
    private FeedAdapter feedAdapter;
    private SimpleSectionAdapter<BaseDao> sectionAdapter;


    @OnClick(R.id.add_item)
    public void onFeedEventAdd(){
//        scopedBus.post(new AddFeedEvent());
        showFeedSelectFragment(new FeedSelectFragment(), "feed_select");
//        scopedBus.post(new AddItemEvent(AddItemTypes.FEED_LOG));
    }

    private void showFeedSelectFragment(FeedSelectFragment feedSelectFragment, String s) {
        feedSelectFragment.show(getActivity().getSupportFragmentManager(),"feed_select");
    }

    Typeface typeface;

    private String TAG = "FeedingListFragment";

    private EventListener eventListener = new EventListener();

    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);

//        typeface=Typeface.createFromAsset(getActivity().getAssets(),
//                "fonts/proximanova_light.ttf");

        feedListView.setEmptyView(emptyView);


        scopedBus.post(new FragmentCreated("Feeding List"));

        feedList = new ArrayList<FeedDao>();


        getLoaderManager().initLoader(LOADER_ID, null, this);




    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);



//        currentTime.setText(today.hour + ":" + today.minute + ":" + today.second);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_feeding_list, null);
        ButterKnife.inject(this, view);
        return view;
    }

    /*
    * Register to events...
    */
    @Override
    public void onStart(){


        super.onStart();
        Log.d(TAG, "onStart");
        scopedBus.register(eventListener);
        getLoaderManager().restartLoader(LOADER_ID, null, this);

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
        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_add:
                showFeedSelectFragment(new FeedSelectFragment(), "feed_select");

//                scopedBus.post(new AddItemEvent(AddItemTypes.FEED_BOTTLE));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_feed, menu);
    }

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<List<FeedDao>> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "create Loader");

        return new FeedLoader(getActivity());
    }


    @Override
    public void onLoadFinished(Loader<List<FeedDao>> loader, List<FeedDao> data) {
//        Log.d(TAG, "number of diaper changes " + data.size());
        Log.d(TAG, "loader finished");

        if (data!=null && data.size()>0) {
            emptyView.setVisibility(View.GONE);
            feedListView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.VISIBLE);
            feedListView.setVisibility(View.GONE);
        }
        feedList = data;

        feedAdapter = new FeedAdapter(getActivity(), R.layout.list_item_diaper_change, feedList);

//        diaperChangeAdapter.update(diaperChangeDaoList);

        sectionAdapter = new SimpleSectionAdapter<BaseDao>(context,
                feedAdapter,
                R.layout.section_header_blue,
                R.id.title,
                new DiaperChangeSectionizer());

        feedListView.setAdapter(sectionAdapter);
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<List<FeedDao>> loader) {

    }


    private class EventListener {
        public EventListener() {

        }


    }
}
