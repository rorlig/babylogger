package com.rorlig.babylog.ui.fragment.diaper;

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
//import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gc.materialdesign.views.Button;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.mobsandgeeks.adapters.SimpleSectionAdapter;
import com.rorlig.babylog.R;
import com.rorlig.babylog.dagger.ForActivity;
import com.rorlig.babylog.dao.BaseDao;
import com.rorlig.babylog.dao.DiaperChangeDao;
import com.rorlig.babylog.db.BabyLoggerORMUtils;
import com.rorlig.babylog.db.ContactsLoader;
import com.rorlig.babylog.model.ContactItem;
import com.rorlig.babylog.otto.DiaperLogCreatedEvent;
import com.rorlig.babylog.otto.events.AddDiaperChangeEvent;
import com.rorlig.babylog.otto.events.other.AddItemEvent;
import com.rorlig.babylog.otto.events.other.AddItemTypes;
import com.rorlig.babylog.otto.events.ui.FragmentCreated;
import com.rorlig.babylog.ui.adapter.DiaperChangeAdapter;
import com.rorlig.babylog.ui.adapter.DiaperChangeSectionizer;
import com.rorlig.babylog.ui.fragment.InjectableFragment;
import com.squareup.otto.Subscribe;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by rorlig on 7/18/14.
 * @author gaurav gupta
 * history of diaper changes
 */
public class DiaperChangeListFragment extends InjectableFragment implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<List<DiaperChangeDao>> {

    @ForActivity
    @Inject
    Context context;

//    @InjectView(R.id.currentDate)
//    TextView currentDate;
//
//    @InjectView(R.id.currentTime)
//    TextView currentTime;



//    @InjectView(R.id.gridview)
//    GridView actionsList;

//    @InjectView(R.id.menu_header)
//    TextView menuHeader;

    @InjectView(R.id.diaperchangelist)
    ListView diaperChangeListView;

    @InjectView(R.id.emptyView)
    RelativeLayout emptyView;

    @InjectView(R.id.errorText)
    TextView errorText;



    @InjectView(R.id.add_item)
    Button btnDiaperChange;
    private BabyLoggerORMUtils babyORMLiteUtils;
    private List<DiaperChangeDao> diaperChangeList;
    private DiaperChangeAdapter diaperChangeAdapter;
    private SimpleSectionAdapter<BaseDao> sectionAdapter;
    private int LOADER_ID=2;

    @OnClick(R.id.add_item)
    public void onDiaperChangeClicked(){
//        scopedBus.post(new AddDiaperChangeEvent());

        scopedBus.post(new AddItemEvent(AddItemTypes.DIAPER_CHANGE));
    }


    Typeface typeface;

    private String TAG = "DiaperChangeListFragment";

    private EventListener eventListener = new EventListener();

    PreparedQuery<DiaperChangeDao> queryBuilder;


    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);

//        typeface=Typeface.createFromAsset(getActivity().getAssets(),
//                "fonts/proximanova_light.ttf");

//        diaperChangeListView.setEmptyView(emptyView);

//        btnDiaperChange.setTypeface(typeface);

//        errorText.setTypeface(typeface);

        scopedBus.post(new FragmentCreated("Diaper Change List"));



        babyORMLiteUtils = new BabyLoggerORMUtils(getActivity());
        try {
            queryBuilder = babyORMLiteUtils.getDiaperChangeDao().queryBuilder().orderBy("time", false).prepare();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        diaperChangeList = new ArrayList<DiaperChangeDao>();
        Log.d(TAG, "number of diaper changes " + diaperChangeList.size());
        diaperChangeAdapter = new DiaperChangeAdapter(getActivity(), R.layout.list_item_diaper_change, diaperChangeList);
        sectionAdapter = new SimpleSectionAdapter<BaseDao>(context,
                diaperChangeAdapter, R.layout.section_header, R.id.title,
                new DiaperChangeSectionizer());
        diaperChangeListView.setAdapter(sectionAdapter);
        diaperChangeListView.setOnItemClickListener(this);

        getLoaderManager().initLoader(LOADER_ID, null, this);

//        getActivity().getActionBar().setTitle("Diaper Change List");



    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


//        currentTime.setText(today.hour + ":" + today.minute + ":" + today.second);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_diaperchage_list, null);
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
        getLoaderManager().restartLoader(LOADER_ID, null, this);
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_item, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_add:
                scopedBus.post(new AddItemEvent(AddItemTypes.DIAPER_CHANGE));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public Loader<List<DiaperChangeDao>> onCreateLoader(int i, Bundle bundle) {
        Log.d(TAG, "create Loader");

        return new DiaperLoader(getActivity());

    }

    @Override
    public void onLoadFinished(Loader<List<DiaperChangeDao>> listLoader, List<DiaperChangeDao> diaperChangeDaoList) {
        Log.d(TAG, "number of diaper changes " + diaperChangeDaoList.size());
        Log.d(TAG, "loader finished");

        if (diaperChangeDaoList.size()>0) {
            emptyView.setVisibility(View.GONE);
            diaperChangeListView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.VISIBLE);
            diaperChangeListView.setVisibility(View.GONE);
        }
        diaperChangeList = diaperChangeDaoList;

        diaperChangeAdapter = new DiaperChangeAdapter(getActivity(), R.layout.list_item_diaper_change, diaperChangeList);

//        diaperChangeAdapter.update(diaperChangeDaoList);

        sectionAdapter = new SimpleSectionAdapter<BaseDao>(context,
                diaperChangeAdapter, R.layout.section_header, R.id.title,
                new DiaperChangeSectionizer());

        diaperChangeListView.setAdapter(sectionAdapter);
        //        diaperChangeAdapter = new DiaperChangeAdapter(getActivity(), R.layout.list_item_diaper_change, diaperChangeDaoList);
//        sectionAdapter = new SimpleSectionAdapter<DiaperChangeDao>(context,
//                diaperChangeAdapter, R.layout.section_header, R.id.title,
//                new DiaperChangeSectionizer());
//        diaperChangeListView.setAdapter(diaperChangeAdapter);
//        sectionAdapter = new SimpleSectionAdapter<DiaperChangeDao>(context,
//                diaperChangeAdapter, R.layout.section_header, R.id.title,
//                new DiaperChangeSectionizer());
//        diaperChangeListView.setAdapter(diaperChangeAdapter);

//        diaperChangeAdapter = new DiaperChangeAdapter(getActivity(), R.layout.list_item_diaper_change, diaperChangeList);
//        sectionAdapter = new SimpleSectionAdapter<DiaperChangeDao>(context,
//                diaperChangeAdapter, R.layout.section_header, R.id.title,
//                new DiaperChangeSectionizer());
//        diaperChangeListView.setAdapter(sectionAdapter);
//        diaperChangeListView.setOnItemClickListener(this);
    }


    @Override
    public void onLoaderReset(Loader<List<DiaperChangeDao>> listLoader) {

    }


    private class EventListener {
        private EventListener(){
        }

        @Subscribe
        public void onDiaperChangeEvent(DiaperLogCreatedEvent event) {
            Log.d(TAG, "onDiaperChangeEvent");
        }




    }
}
