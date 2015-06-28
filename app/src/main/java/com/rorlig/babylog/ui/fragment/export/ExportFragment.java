package com.rorlig.babylog.ui.fragment.export;

import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rorlig.babylog.R;
import com.rorlig.babylog.dagger.ForActivity;
import com.rorlig.babylog.model.ItemModel;
import com.rorlig.babylog.otto.events.datetime.DateSetEvent;
import com.rorlig.babylog.otto.events.ui.FragmentCreated;
import com.rorlig.babylog.ui.adapter.ExportItemAdapter;
import com.rorlig.babylog.ui.adapter.HomeItemAdapter;
import com.rorlig.babylog.ui.fragment.InjectableFragment;
import com.rorlig.babylog.ui.fragment.datetime.DatePickerFragment;
import com.squareup.otto.Subscribe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * Created by gaurav
 * Growth element..
 */
public class ExportFragment extends InjectableFragment implements AdapterView.OnItemClickListener {

    @ForActivity
    @Inject
    Context context;

    @InjectView(R.id.date_range_start)
    RelativeLayout dateRangeStart;

    @InjectView(R.id.date_range_end)
    RelativeLayout dateRangeEnd;

    @InjectView(R.id.export_activities_list)
    ListView exportListView;

    TextView dateStartDayTextView;


    TextView dateStartMonthTextView;

    TextView dateStartYearTextView;


    TextView dateEndDayTextView;


    TextView dateEndMonthTextView;

    TextView dateEndYearTextView;


    private String TAG = "ExportFragment";
    private EventListener eventListener = new EventListener();

    @Inject
    SharedPreferences preferences;
    private String[] itemNames;
    private ExportItemAdapter exportListAdapter;

    private static final String EXPORT_LIST = "list";
    private static final String START_DATE_DAY = "start_day";
    private static final String START_DATE_MONTH = "start_month";
    private static final String START_DATE_YEAR = "start_year";

    private static final String END_DATE_DAY = "end_day";
    private static final String END_DATE_MONTH = "end_month";
    private static final String END_DATE_YEAR = "end_year";

    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);


        scopedBus.post(new FragmentCreated("Export Fragment"));


        dateStartDayTextView = (TextView) dateRangeStart.findViewById(R.id.day);
        dateStartMonthTextView = (TextView) dateRangeStart.findViewById(R.id.month);
        dateStartYearTextView = (TextView) dateRangeStart.findViewById(R.id.year);

        dateEndDayTextView = (TextView) dateRangeEnd.findViewById(R.id.day);
        dateEndMonthTextView = (TextView) dateRangeEnd.findViewById(R.id.month);
        dateEndYearTextView = (TextView) dateRangeEnd.findViewById(R.id.year);


        String logItems = preferences.getString("logItems", "");


        itemNames = getResources().getStringArray(R.array.items);
        List<Parcelable> itemModelArrayList = new ArrayList<Parcelable>();

        Parcelable[] itemList = null;

        if (paramBundle!=null) {
            itemList = paramBundle.getParcelableArray(EXPORT_LIST);
            itemModelArrayList = Arrays.asList(itemList);
            dateStartDayTextView.setText(paramBundle.getString(START_DATE_DAY));
            dateStartMonthTextView.setText(paramBundle.getString(START_DATE_MONTH));
            dateStartYearTextView.setText(paramBundle.getString(START_DATE_YEAR));
            dateEndDayTextView.setText(paramBundle.getString(END_DATE_DAY));
            dateEndMonthTextView.setText(paramBundle.getString(END_DATE_MONTH));
            dateEndYearTextView.setText(paramBundle.getString(END_DATE_YEAR));
        } else {
            setStartDate();
            setEndDate();
            Log.d(TAG, "itemarraylist is not there in parambundle");
            for (String item: itemNames) {
                itemModelArrayList.add(new ItemModel(item, false));
            }
        }





        exportListAdapter = new ExportItemAdapter(getActivity(),R.layout.list_item_export, itemModelArrayList);
        exportListView.setAdapter(exportListAdapter);

        exportListView.setOnItemClickListener(this);




    }

    private void setEndDate() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        dateEndYearTextView.setText("" + year);
        dateEndMonthTextView.setText((month<10? "0" + month: "" + month));
        dateEndDayTextView.setText("" + day);

    }

    private void setStartDate() {

        final Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -7);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        dateStartYearTextView.setText("" + year);
        dateStartMonthTextView.setText((month<10? "0" + month: "" + month));
        dateStartDayTextView.setText("" + day);


    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_export, null);
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
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        final ItemModel item = (ItemModel) exportListAdapter.getLogListItem().get(position);
        item.setItemChecked(!item.isItemChecked());
        exportListAdapter.notifyDataSetChanged();

        if (view!=null) {
            CheckBox checkBox = (CheckBox) view.findViewById(R.id.item_checkbox);
            checkBox.setChecked(!checkBox.isChecked());
        }


    }

    private class EventListener {
        public EventListener() {

        }

        @Subscribe
        public void onDateChanged(DateSetEvent dateSetEvent){
            Log.d(TAG, "dateSetEvent " + dateSetEvent);
            int day = dateSetEvent.getCalendar().get(Calendar.DAY_OF_MONTH);
            int month = dateSetEvent.getCalendar().get(Calendar.MONTH) + 1;
            int year = dateSetEvent.getCalendar().get(Calendar.YEAR);
            if (dateSetEvent.getLabel().equals("start")){
                dateStartYearTextView.setText("" + year);
                dateStartMonthTextView.setText((month<10? "0" + month: "" + month));
                dateStartDayTextView.setText("" + day);
            } else {
                dateEndYearTextView.setText("" + year);
                dateEndMonthTextView.setText((month<10? "0" + month: "" + month));
                dateEndDayTextView.setText("" + day);
            }
        }

    }

    @OnClick(R.id.date_range_start)
    public void onDateRangeStart(){
        Log.d(TAG, "onDateRangeStart");
        showDatePickerDialog("start");
    }

    @OnClick(R.id.date_range_end)
    public void onDateRangeEnd(){
        Log.d(TAG, "onDateRangeEnd");
        showDatePickerDialog("end");
    }

    @OnClick(R.id.btn_export)
    public void onBtnExportClicked(){
        Log.d(TAG, "export buttom clicked");

        for (Parcelable itemModel :exportListAdapter.getLogListItem()) {
            Log.d(TAG, itemModel.toString());
        }

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

    public void showDatePickerDialog(String label) {
        DialogFragment newFragment = new DatePickerFragment();

        Bundle args = new Bundle();
        args.putString("label", label);
        if (label.equals("start")) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
            String endDateString = dateEndMonthTextView.getText().toString() + "-" + dateEndDayTextView.getText().toString() + "-" + dateEndYearTextView.getText().toString();

            try {
                Log.d(TAG, "endDateString: " + endDateString);
                Date endDate = sdf.parse(endDateString);
                Log.d(TAG, "endDate " + endDate);
                args.putLong("max_start_date", endDate.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        newFragment.setArguments(args);
        newFragment.show(getFragmentManager(), "datepicker");
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArray("list", exportListAdapter.getLogListItem().toArray(new Parcelable[exportListAdapter.getLogListItem().size()]));
        outState.putString(START_DATE_DAY, dateStartDayTextView.getText().toString());
        outState.putString(START_DATE_MONTH, dateStartMonthTextView.getText().toString());
        outState.putString(START_DATE_YEAR, dateStartYearTextView.getText().toString());
        outState.putString(END_DATE_DAY, dateEndDayTextView.getText().toString());
        outState.putString(END_DATE_MONTH, dateEndMonthTextView.getText().toString());
        outState.putString(END_DATE_YEAR, dateEndYearTextView.getText().toString());
    }


}
