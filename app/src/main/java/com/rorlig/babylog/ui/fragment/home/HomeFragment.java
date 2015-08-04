package com.rorlig.babylog.ui.fragment.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.rorlig.babylog.R;
import com.rorlig.babylog.model.ItemModel;
import com.rorlig.babylog.ui.activity.DiaperChangeActivity;
import com.rorlig.babylog.ui.activity.ExportActivity;
import com.rorlig.babylog.ui.activity.FeedingActivity;
import com.rorlig.babylog.ui.activity.GrowthActivity;
import com.rorlig.babylog.ui.activity.MilestonesActivity;
import com.rorlig.babylog.ui.activity.ProfileActivity;
import com.rorlig.babylog.ui.activity.SleepActivity;
import com.rorlig.babylog.ui.adapter.HomeItemAdapter;
import com.rorlig.babylog.ui.fragment.InjectableFragment;
import com.rorlig.babylog.utils.transform.BlurTransformation;
import com.rorlig.babylog.utils.transform.CircleTransform;
import com.rorlig.babylog.utils.transform.CropTransform;
import com.squareup.picasso.Picasso;

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

    @InjectView(R.id.profile_block)
    ImageView profileImageView;

    @InjectView(R.id.baby_name)
    TextView babyNameTextView;


    @InjectView(R.id.baby_image)
    CircleImageView babyImageView;

    @Inject
    Picasso picasso;


    private ArrayList<ItemModel> logs;
    private HomeItemAdapter homeAdapter;
    private ArrayList<ItemModel> filteredLogs;
    private String[] itemNames;
    private Parcelable[] itemList;
    private static final String HOME_LIST = "list";
    private String TAG = "HomeFragment2";

    @Inject
    SharedPreferences preferences;

    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);


        }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        String name = preferences.getString("name", "");
        if (!name.equals("")) {
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
            }

        }




    }




    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, " onResume");
        final String imageUri = preferences.getString("imageUri", "");

        Log.d(TAG, "imageUri " + imageUri);
//        profileImageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                Log.d(TAG, "here");
                if (!imageUri.equals("")) {

                    //             load background image in nav drawer
                    Log.d(TAG, "display metrics dpi " + getResources().getDisplayMetrics().density);
                    float density = getResources().getDisplayMetrics().density + 1;
                    picasso.load(Uri.parse(imageUri))
                            .transform(new CropTransform(profileImageView.getWidth()/density, profileImageView.getHeight()/density))
                            .transform(new BlurTransformation(getActivity()))
                            .fit()
                            .into(profileImageView);

                    // load background image in nav drawer
                    picasso.load(Uri.parse(imageUri))
                            .transform(new CircleTransform())
                            .fit()
                            .into(babyImageView);


                } else {

                    resetImageViews();
                }

            }

//        Log.d(TAG, "onResume");

//        });
//    }

    private void resetImageViews() {
        Log.d(TAG, "resetImageViews");
        babyImageView.setImageURI(null);
        babyImageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.boy_normal));
        profileImageView.setImageURI(null);
        profileImageView.setImageDrawable(null);
        profileImageView.setBackgroundColor(getResources().getColor(R.color.gray_cloud));

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





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        Log.d(TAG, "onOptionsItemSelected");

        switch (item.getItemId()) {

//            case R.id.action_settings:
//                startActivity(new Intent(getActivity(), PrefsActivity.class));
//                break;
            case R.id.action_export:
                startActivity(new Intent(getActivity(), ExportActivity.class));

                break;
//            case R.id.action_licenses:
//                startActivity(new Intent(getActivity(), LicenseActivity.class));
//                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putParcelableArray("list", homeAdapter.getLogListItem().toArray(new Parcelable[homeAdapter.getLogListItem().size()]));
    }

    @OnClick(R.id.diaper_block)
    public void diaperBlockClicked(){
        startActivity(new Intent(getActivity(), DiaperChangeActivity.class));
    }

    @OnClick(R.id.feed_block)
    public void feedBlockClicked(){
        startActivity(new Intent(getActivity(), FeedingActivity.class));
    }

    @OnClick(R.id.growth_block)
    public void growthBlockClicked(){
        startActivity(new Intent(getActivity(), GrowthActivity.class));
    }

    @OnClick(R.id.milestone_block)
    public void milestoneClicked(){
        startActivity(new Intent(getActivity(), MilestonesActivity.class));
    }



    @OnClick(R.id.profile_block)
    public void babyImageClicked() {
        startActivity(new Intent(getActivity(), ProfileActivity.class));
    }

    @OnClick(R.id.sleep_block)
    public void sleepBlockClicked(){
        Log.d(TAG, "sleep block clicked");
        startActivity(new Intent(getActivity(), SleepActivity.class));
    }
}
