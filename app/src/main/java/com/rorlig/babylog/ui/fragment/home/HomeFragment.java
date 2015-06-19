package com.rorlig.babylog.ui.fragment.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.rorlig.babylog.R;
import com.rorlig.babylog.model.ItemModel;
import com.rorlig.babylog.ui.activity.BarchartActivity;
import com.rorlig.babylog.ui.activity.DiaperChangeActivity;
import com.rorlig.babylog.ui.activity.FeedingActivity;
import com.rorlig.babylog.ui.activity.GrowthActivity;
import com.rorlig.babylog.ui.activity.MilestonesActivity;
import com.rorlig.babylog.ui.activity.ProfileActivity;
import com.rorlig.babylog.ui.adapter.HomeItemAdapter;
import com.rorlig.babylog.ui.fragment.InjectableFragment;

import java.util.ArrayList;
import java.util.Calendar;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by rorlig on 5/31/15.
 */
public class HomeFragment extends InjectableFragment {

    @InjectView(R.id.baby_name)
    TextView babyNameTextView;

//    @InjectView(R.id.baby_age)
//    TextView babyAgeTextView;

    @Inject
    SharedPreferences preferences;
    private String TAG = "HomeFragment";

    @Inject
    Gson gson;

    @InjectView(R.id.homeList)
    ListView listView;

    @InjectView(R.id.basicInfoBlock)
    RelativeLayout basicInfoBlockLayout;

    @InjectView(R.id.baby_image)
    CircleImageView babyImageView;

    @InjectView(R.id.action_sleep)
    FloatingActionButton floatingActionButtonSleep;

    private ArrayList<ItemModel> logs;
    private HomeItemAdapter homeAdapter;
    private ArrayList<ItemModel> filteredLogs;
    private String[] itemNames;

    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);


        String logItems = preferences.getString("logItems", "");


        itemNames = getResources().getStringArray(R.array.items);

        ArrayList<ItemModel> itemModelArrayList = new ArrayList<ItemModel>();

        for (String item: itemNames) {
            itemModelArrayList.add(new ItemModel(item, true));
        }


        Log.d(TAG, logItems);
//        if (!logItems.equals("")) {
//
//            logs = gson.fromJson(logItems, new TypeToken<List<ItemModel>>(){}.getType());
//            Log.d(TAG, "logs " + logs);
//
//            filteredLogs = filter(logs);

            homeAdapter = new HomeItemAdapter(getActivity(),R.layout.list_item_home, itemModelArrayList);
            listView.setAdapter(homeAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Log.d(TAG, "onItemClick ");
                    CharSequence charSequence = "Todo";

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

                        case 3:
                            startActivity(new Intent(getActivity(), MilestonesActivity.class));
                            break;
                        default:
                            Toast.makeText(getActivity(), "TODO" , Toast.LENGTH_SHORT).show();
                            break;


                    }
//

                }
            });

            basicInfoBlockLayout.getBackground().setAlpha(100);
            String imageUri = preferences.getString("imageUri", "");
            if (!imageUri.equals("")){
                babyImageView.setImageURI(Uri.parse(imageUri));
            }
        }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        String name = preferences.getString("name", "");
        if (name.equals("")){
//            babyAgeTextView.setVisibility(View.GONE);
        } else {
            babyNameTextView.setText("Welcome " + preferences.getString("name", ""));
            String dob = preferences.getString("dob", "");
            if (!dob.equals("")){
                String[] dateElements = dob.split(",");
                Log.d(TAG,"" + dateElements.length);
                Log.d(TAG, dateElements[0]);
                int year = Integer.parseInt(dateElements[0]);
                int month = Integer.parseInt(dateElements[1]);
                int day = Integer.parseInt(dateElements[2]);
                Log.d(TAG, " year "  + year + " month " + month + " day " + day);
                Calendar c = Calendar.getInstance();
                c.set(year,month,day);
                Log.d(TAG, "time " + c.getTimeInMillis());
                long diff = System.currentTimeMillis() - c.getTimeInMillis();
                long days = diff/(86400*1000);
                Log.d(TAG, "days old " + days);
//                babyAgeTextView.setVisibility(View.VISIBLE);
//                babyAgeTextView.setText(days + " days old");


            }

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");

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

        Intent intent = new Intent(getActivity(), GrowthActivity.class);
        intent.putExtra("intent", "growth_activity");
        startActivity(intent);

    }

    @OnClick(R.id.action_diaper_change)
    public void actionDiaperChange() {
        Intent intent = new Intent(getActivity(), DiaperChangeActivity.class);
        intent.putExtra("intent", "diaper_change");
        startActivity(intent);

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.home_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");

        switch (item.getItemId()) {
            case R.id.action_stats:
                startActivity(new Intent(getActivity(), BarchartActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @OnClick(R.id.baby_image)
    public void babyImageClicked(){
        startActivity(new Intent(getActivity(), ProfileActivity.class));

    }
}
