package com.rorlig.babylog.ui.fragment.growth;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gc.materialdesign.views.Button;
import com.rorlig.babylog.R;
import com.rorlig.babylog.dagger.ForActivity;
import com.rorlig.babylog.otto.events.other.AddItemEvent;
import com.rorlig.babylog.otto.events.other.AddItemTypes;
import com.rorlig.babylog.otto.events.ui.FragmentCreated;
import com.rorlig.babylog.ui.fragment.InjectableFragment;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by rorlig on 7/18/14.
 * @author gaurav gupta
 * history of growth items
 */
public class GrowthListFragment extends InjectableFragment{

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

    @InjectView(R.id.itemList)
    ListView listView;

    @InjectView(R.id.emptyView)
    RelativeLayout emptyView;

    @InjectView(R.id.errorText)
    TextView errorText;



    @InjectView(R.id.add_item)
    Button btnAddItem;

    @OnClick(R.id.add_item)
    public void onDiaperChangeClicked(){
//        scopedBus.post(new AddDiaperChangeEvent());

        scopedBus.post(new AddItemEvent(AddItemTypes.GROWTH_LOG));
    }


    Typeface typeface;

    private String TAG = "GrowthListFragment";

    private EventListener eventListener = new EventListener();

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
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);

        typeface=Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/proximanova_light.ttf");

        listView.setEmptyView(emptyView);
//
//        btnAddItem.setTypeface(typeface);
//
//        errorText.setTypeface(typeface);

        scopedBus.post(new FragmentCreated("Growth"));


//        getActivity().getActionBar().setTitle("Diaper Change List");


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
                scopedBus.post(new AddItemEvent(AddItemTypes.GROWTH_LOG));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }






    private class EventListener {
        public EventListener() {

        }


    }
}
