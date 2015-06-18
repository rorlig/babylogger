package com.rorlig.babylog.ui.fragment.home;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gc.materialdesign.views.Button;
import com.rorlig.babylog.R;
import com.rorlig.babylog.dagger.ForActivity;
import com.rorlig.babylog.model.ItemModel;
import com.rorlig.babylog.otto.ItemCheckChangeEvent;
import com.rorlig.babylog.otto.ItemsSelectedEvent;
import com.rorlig.babylog.ui.activity.BabySex;
import com.rorlig.babylog.ui.adapter.LogItemAdapter;
import com.rorlig.babylog.ui.fragment.InjectableFragment;
import com.squareup.otto.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by rorlig on 5/31/15.
 */
public class LaunchFragment extends InjectableFragment implements DatePickerDialog.OnDateSetListener{

    @ForActivity
    @Inject
    Context context;

//    @InjectView(R.id.gridview)
//    GridView actionsList;

//    @InjectView(R.id.menu_header)
//    TextView menuHeader;


    Typeface typeface;

    private String TAG = "LaunchFragment";
    private ListView listView;
    private LogItemAdapter logItemsAdapter;
    private TextView currentDate;

    @InjectView(R.id.baby_name)
    EditText babyNameText;

    @InjectView(R.id.babySex)
    RadioGroup babySexRadioGroup;

    @InjectView(R.id.baby_pic)
    ImageView babyPicImageView;

    @InjectView(R.id.save_btn)
    Button saveBtn;

    BabySex babySex = BabySex.BOY;
    private DatePickerDialog datePickerDialog;
    private int nameLen;
    private String[] itemNames;
    private int[] itemStates;
    private ArrayList<ItemModel> logListItem;


    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);




    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView) view.findViewById(R.id.logList);
//
        itemNames = getResources().getStringArray(R.array.items);

        itemStates = getResources().getIntArray(R.array.itemStates);

        logListItem = new ArrayList<ItemModel>();

        int index = 0;
        for (; index<itemStates.length; ++index) {
            ItemModel itemModel;
            Log.d(TAG, "itemState " + itemStates[index] + " item " + itemNames[index]);
            if (itemStates[index] == 1) {
                Log.d(TAG, "1");
                itemModel = new ItemModel(itemNames[index], true);
                itemModel.setItemChecked(true);

            } else {
                itemModel = new ItemModel(itemNames[index], false);

            }

//            itemModel = new ItemModel(itemNames[index], false);

            logListItem.add(itemModel);
        }

        Log.d(TAG, "intial loglist " + logListItem);
        Log.d(TAG, "enable count " + enabledCount());
        logItemsAdapter = new LogItemAdapter(getActivity(), R.layout.list_item_log, logListItem);

        currentDate = (TextView) view.findViewById(R.id.currentDate);


        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy");
        currentDate.setText(sdf.format(new Date()));
//
        currentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
//
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.HOUR, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        Log.d(TAG, "year : " + year + " month: " + month + " day " + day);

        datePickerDialog =  new DatePickerDialog(getActivity(), this, year, month, day);

        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

        calendar.add(Calendar.YEAR, -1);

        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());


        listView.setAdapter(logItemsAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.d(TAG, "onItemClick ");
//                ItemModel itemModel = (ItemModel) parent.getItemAtPosition(position);
//                itemModel.setItemChecked(!itemModel.isItemChecked());
//                CheckBox checkBox = (CheckBox) view;
//                checkBox.setChecked(!checkBox.isCheck());
//                Toast.makeText(getApplicationContext(),
//                        "Clicked on Row: " + position,
//                        Toast.LENGTH_LONG).show();

            }
        });

        babySexRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.babyBoy:
                        babyPicImageView.setImageDrawable(getResources().getDrawable(R.drawable.btn_radio_boy));
                        babySex = BabySex.BOY;

                        break;
                    case R.id.babyGirl:
                        babyPicImageView.setImageDrawable(getResources().getDrawable(R.drawable.btn_radio_girl));
                        babySex = BabySex.GIRL;
                        break;
                }
            }
        });


        babyNameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                nameLen = s.length();
                setSaveBtnState();

            }
        });

    }

    private void setSaveBtnState() {

        if (nameLen>2 && enabledCount()>2) {
            saveBtn.setEnabled(true);
        } else  {
            saveBtn.setEnabled(false);
        }
    }

    private void showDatePickerDialog() {


        // Create a new instance of DatePickerDialog and return it

        datePickerDialog.show();
//        DialogFragment newFragment = new DatePickerFragment();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_launch, null);
        ButterKnife.inject(this, view);
        return view;
    }


    /**
     * @param view        The view associated with this listener.
     * @param year        The year that was set.
     * @param monthOfYear The month that was set (0-11) for compatibility
     *                    with {@link Calendar}.
     * @param dayOfMonth  The day of the month that was set.
     */
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Log.d(TAG, "dateSet " + year + " month " + monthOfYear + " day " + dayOfMonth);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth, 0, 0);

        currentDate.setText(sdf.format(new Date(calendar.getTimeInMillis())));


    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to {@link Activity#onResume() Activity.onResume} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");

        scopedBus.register(this);
    }

    /**
     * Called when the Fragment is no longer resumed.  This is generally
     * tied to {@link Activity#onPause() Activity.onPause} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onPause() {
        super.onPause();

        Log.d(TAG, "onPause");

        scopedBus.register(this);
    }

    @OnClick(R.id.save_btn)
    public void onSaveBtnClicked() {
        scopedBus.post(new ItemsSelectedEvent(logItemsAdapter.getLogListItem(), babyNameText.getText().toString(), currentDate.getText().toString()));
    }

    @Subscribe
    public void onItemCheckStateChange(ItemCheckChangeEvent event) {
        Log.d(TAG, "onItemCheckStateChange event");

        Log.d(TAG, logListItem.toString());
        Log.d(TAG, logItemsAdapter.getLogListItem().toString());
        setSaveBtnState();
    }

    public int enabledCount() {
        int enabled = 0;
        Log.d(TAG, "" + logListItem);
        for (ItemModel itemModel : logListItem) {
            if (itemModel.isItemChecked()) {
                enabled++;
            }
        }

        Log.d(TAG, "enabled count " + enabled);

        return enabled;
    }





}
