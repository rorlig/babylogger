package com.rorlig.babylog.ui.fragment.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rorlig.babylog.R;
import com.rorlig.babylog.model.ItemModel;
import com.rorlig.babylog.ui.activity.DiaperChangeActivity;
import com.rorlig.babylog.ui.activity.FeedingActivity;
import com.rorlig.babylog.ui.activity.GrowthActivity;
import com.rorlig.babylog.ui.adapter.HomeItemAdapter;
import com.rorlig.babylog.ui.fragment.InjectableFragment;
import com.rorlig.babylog.ui.activity.DiaperChangeListActivity2;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by rorlig on 5/31/15.
 */
public class HomeFragment extends InjectableFragment {

    @InjectView(R.id.babyName)
    TextView babyName;

    @Inject
    SharedPreferences preferences;
    private String TAG = "HomeFragment";

    @Inject
    Gson gson;

    @InjectView(R.id.homeList)
    ListView listView;

    @InjectView(R.id.action_sleep)
    FloatingActionButton floatingActionButtonSleep;

    private ArrayList<ItemModel> logs;
    private HomeItemAdapter homeAdapter;
    private ArrayList<ItemModel> filteredLogs;

    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);

        babyName.setText(preferences.getString("name", ""));

        String logItems = preferences.getString("logItems", "");

        Log.d(TAG, logItems);
        if (!logItems.equals("")) {

            logs = gson.fromJson(logItems, new TypeToken<List<ItemModel>>(){}.getType());
            Log.d(TAG, "logs " + logs);

            filteredLogs = filter(logs);

            homeAdapter = new HomeItemAdapter(getActivity(),R.layout.list_item_home, filteredLogs);
            listView.setAdapter(homeAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Log.d(TAG, "onItemClick ");

                    switch (position) {
                        case 0:
                            startActivity(new Intent(getActivity(), DiaperChangeActivity.class));
                            break;
                        case 1:
                            startActivity(new Intent(getActivity(), FeedingActivity.class   ));
                            break;

                        case 2:
                            startActivity(new Intent(getActivity(), GrowthActivity.class));
                            break;
//                        case 3:
//                            startActivity(new Intent(getActivity(), ));


                    }
//                ItemModel itemModel = (ItemModel) parent.getItemAtPosition(position);
//                itemModel.setItemChecked(!itemModel.isItemChecked());
//                CheckBox checkBox = (CheckBox) view;
//                checkBox.setChecked(!checkBox.isCheck());
//                Toast.makeText(getApplicationContext(),
//                        "Clicked on Row: " + position,
//                        Toast.LENGTH_LONG).show();

                }
            });
        }





    }

    private ArrayList<ItemModel> filter(ArrayList<ItemModel> logs) {
        ArrayList<ItemModel> filteredLogs = new ArrayList<ItemModel>();
        for (ItemModel itemModel: logs) {
            if (itemModel.isItemChecked()) {
                filteredLogs.add(itemModel);
            }
        }

        return filteredLogs;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @OnClick(R.id.action_bottle_feed)
    public void actionBottleFeed() {
        Intent intent = new Intent(getActivity(), FeedingActivity.class);
        intent.putExtra("intent", "feeding_activity_bottle");
        startActivity(intent);

    }
    @OnClick(R.id.action_sleep)
    public void actionSleep() {
        Log.d(TAG, "action sleep");
    }

    @OnClick(R.id.action_milestones)
    public void actionMilestones() {
        Log.d(TAG, "action milestones");
    }

    @OnClick(R.id.action_growth)
    public void actionGrowth(){

    }

    @OnClick(R.id.action_diaper_change)
    public void actionDiaperChange() {
        Intent intent = new Intent(getActivity(), DiaperChangeActivity.class);
        intent.putExtra("intent", "diaper_change");
        startActivity(intent);

    }
}
