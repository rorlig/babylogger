package com.rorlig.babyapp.ui.fragment.milestones;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseObject;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.otto.events.other.AddItemEvent;
import com.rorlig.babyapp.otto.events.other.AddItemTypes;
import com.rorlig.babyapp.ui.fragment.BaseInjectableListFragment;
import com.rorlig.babyapp.utils.AppConstants;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

//import android.widget.Button;

/**
 * @author gaurav gupta
 * history of growth items
 */
public class MilestoneListFragment extends BaseInjectableListFragment {







    public MilestoneListFragment() {
        super(AppConstants.PARSE_CLASS_MILESTONE);

    }


    @OnClick(R.id.add_item)
    public void onMilestoneAddItemClicked(){
        scopedBus.post(new AddItemEvent(AddItemTypes.MILESTONE));
    }


    private String TAG = "MilestoneListFragment";


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




    }

    @Override
    protected void setListResults(List<ParseObject> objects) {
        Log.d(TAG, "setListResults");
        super.setListResults(objects);
        ultimateRecyclerView.setAdapter(baseParseAdapter2);

    }





    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        baseParseAdapter2 = new MilestonesItemAdapter2(parseObjectList);
//        ultimateRecyclerView.setAdapter(baseParseAdapter2);
//        ultimateRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_milestone_list_2, null);
        ButterKnife.inject(this, view);
        return view;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @OnClick(R.id.add_milestone_item)
    public void onAddMilestoneItem(){
        scopedBus.post(new AddItemEvent(AddItemTypes.MILESTONE));
    }

}
