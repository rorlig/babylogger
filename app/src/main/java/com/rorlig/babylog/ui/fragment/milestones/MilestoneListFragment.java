package com.rorlig.babylog.ui.fragment.milestones;

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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gc.materialdesign.views.Button;
import com.j256.ormlite.dao.Dao;
import com.mobsandgeeks.adapters.SimpleSectionAdapter;
import com.rorlig.babylog.R;
import com.rorlig.babylog.dagger.ForActivity;
import com.rorlig.babylog.dao.BaseDao;
import com.rorlig.babylog.dao.GrowthDao;
import com.rorlig.babylog.dao.MilestonesDao;
import com.rorlig.babylog.db.BabyLoggerORMLiteHelper;
import com.rorlig.babylog.otto.events.milestones.MilestoneCancelEvent;
import com.rorlig.babylog.otto.events.milestones.MilestoneResetEvent;
import com.rorlig.babylog.otto.events.milestones.MilestoneSaveEvent;
import com.rorlig.babylog.otto.events.other.AddItemEvent;
import com.rorlig.babylog.otto.events.other.AddItemTypes;
import com.rorlig.babylog.otto.events.ui.FragmentCreated;
import com.rorlig.babylog.ui.adapter.GrowthAdapter;
import com.rorlig.babylog.ui.adapter.MilestonesItemAdapter;
import com.rorlig.babylog.ui.fragment.InjectableFragment;
import com.squareup.otto.Subscribe;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

//import android.widget.Button;

/**
 * @author gaurav gupta
 * history of growth items
 */
public class MilestoneListFragment extends InjectableFragment implements LoaderManager.LoaderCallbacks<List<MilestonesDao>>{

    @ForActivity
    @Inject
    Context context;

    @InjectView(R.id.itemList)
    ListView listView;

    @InjectView(R.id.emptyView)
    RelativeLayout emptyView;

    @InjectView(R.id.errorText)
    TextView errorText;



    @InjectView(R.id.add_item)
    Button btnAddItem;
    private int LOADER_ID = 4;
    private List<GrowthDao> growthList;
    private SimpleSectionAdapter<BaseDao> sectionAdapter;
    private GrowthAdapter growthAdapter;
    private String[] itemNames;
    private MilestonesItemAdapter milestonesAdapter;

//    ArrayList<Milestones> milestonesArrayList = new ArrayList<Milestones>();
    private List<MilestonesDao> milestoneData;

    @Inject
    BabyLoggerORMLiteHelper babyLoggerORMLiteHelper;
    private Dao<MilestonesDao, Integer> milestoneDaoHelper;

    @OnClick(R.id.add_item)
    public void onDiaperChangeClicked(){
//        scopedBus.post(new AddDiaperChangeEvent());

        scopedBus.post(new AddItemEvent(AddItemTypes.GROWTH_LOG));
    }


    Typeface typeface;

    private String TAG = "MilestoneListFragment";

    private EventListener eventListener = new EventListener();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);

        itemNames = getResources().getStringArray(R.array.milestones);

//        for (String item: itemNames) {
//            Milestones milestones = new Milestones(item, false);
//            milestonesArrayList.add(milestones);
//        }

//         milestonesAdapter = new MilestonesItemAdapter(getActivity(), 1, milestonesArrayList);


        typeface=Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/proximanova_light.ttf");

        listView.setEmptyView(emptyView);
//
//        listView.setAdapter(milestonesAdapter);
//        btnAddItem.setTypeface(typeface);
//
//        errorText.setTypeface(typeface);

        scopedBus.post(new FragmentCreated("Milestones"));


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.d(TAG, "onItemClick " + position);
                itemClicked(parent, view, position, id);





            }
        });

        getLoaderManager().initLoader(LOADER_ID, null, this);


//        getActivity().getActionBar().setTitle("Diaper Change List");


    }

    private void itemClicked(AdapterView<?> parent, View view, int position, long id) {
        MilestonesCompletedFragment milestonesCompletedFragment = MilestonesCompletedFragment.newInstance(position);
//        Bundle bundle = new Bundle();
//        bundle.putInt("position", position);
//        milestonesCompletedFragment.setArguments(bundle);
        milestonesCompletedFragment.show(getFragmentManager(),"milestone_completed");
//        showFeedSelectFragment(new MilestonesCompletedFragment(), "milestone_complet/ed");

//        SimpleDialogFragment.createBuilder(getActivity(), getFragmentManager()).setTitle("hello").setMessage("date").setPositiveButtonText("Yes").setNegativeButtonText("No").show();

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


//        currentTime.setText(today.hour + ":" + today.minute + ":" + today.second);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_growth_list, null);
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
//        getLoaderManager().restartLoader(LOADER_ID, null, this);

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
//            case R.id.action_add:
//                scopedBus.post(new AddItemEvent(AddItemTypes.GROWTH_LOG));
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<List<MilestonesDao>> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "create Loader");
        return new MilestonesLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<MilestonesDao>> loader, List<MilestonesDao> data) {

        Log.d(TAG, "number of diaper changes " + data.size());
        Log.d(TAG, "loader finished");

        if (data.size()>0) {
            emptyView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
        milestoneData = data;

        milestonesAdapter = new MilestonesItemAdapter(getActivity(), R.layout.list_item_diaper_change, milestoneData);

//        diaperChangeAdapter.update(diaperChangeDaoList);

//        sectionAdapter = new SimpleSectionAdapter<BaseDao>(context,
//                milestonesAdapter,
//                R.layout.section_header,
//                R.id.title,
//                new DiaperChangeSectionizer());

        listView.setAdapter(milestonesAdapter);

    }

    @Override
    public void onLoaderReset(Loader<List<MilestonesDao>> loader) {

    }


    private class EventListener {
        public EventListener() {

        }

        @Subscribe
        public void onMileStoneSaved(MilestoneSaveEvent event){

            Date date = new Date(event.getYear(), event.getMonth(), event.getDay());
            setCompleted(event.getPosition(), date.getTime(), true);
        }



        @Subscribe
        public void onMileStoneCancel(MilestoneCancelEvent event){
            Log.d(TAG, "onMileStoneCancel");
        }

        @Subscribe
        public void onMileStoneReset(MilestoneResetEvent event) {
            Log.d(TAG, "onMileStoneReset" + event.getPosition());
            setCompleted(event.getPosition(), false);

        }


        private void setCompleted(int position, boolean value) {
            MilestonesDao milestoneItem = milestoneData.get(position);
            milestoneItem.setCompleted(value);


            try {
                milestoneDaoHelper = babyLoggerORMLiteHelper.getMilestonesDao();
                milestoneDaoHelper.update(milestoneItem);
                milestonesAdapter.notifyDataSetChanged();

            } catch (SQLException e) {
                Log.d(TAG, "update item");
                e.printStackTrace();
            }
        }

        private void setCompleted(int position, long time, boolean value) {
            MilestonesDao milestoneItem = milestoneData.get(position);
            milestoneItem.setCompleted(value);
            milestoneItem.setTime(time);


            try {
                milestoneDaoHelper = babyLoggerORMLiteHelper.getMilestonesDao();
                milestoneDaoHelper.update(milestoneItem);
                milestonesAdapter.notifyDataSetChanged();

            } catch (SQLException e) {
                Log.d(TAG, "update item");
                e.printStackTrace();
            }
        }

    }
}
