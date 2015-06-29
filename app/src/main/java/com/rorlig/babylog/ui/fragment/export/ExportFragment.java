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
import android.widget.Toast;

import com.j256.ormlite.stmt.PreparedQuery;
import com.rorlig.babylog.R;
import com.rorlig.babylog.dagger.ForActivity;
import com.rorlig.babylog.dao.DiaperChangeDao;
import com.rorlig.babylog.dao.FeedDao;
import com.rorlig.babylog.dao.GrowthDao;
import com.rorlig.babylog.dao.MilestonesDao;
import com.rorlig.babylog.db.BabyLoggerORMUtils;
import com.rorlig.babylog.model.ItemModel;
import com.rorlig.babylog.otto.events.datetime.DateSetEvent;
import com.rorlig.babylog.otto.events.ui.FragmentCreated;
import com.rorlig.babylog.ui.adapter.ExportItemAdapter;
import com.rorlig.babylog.ui.adapter.HomeItemAdapter;
import com.rorlig.babylog.ui.fragment.InjectableFragment;
import com.rorlig.babylog.ui.fragment.datetime.DatePickerFragment;
import com.rorlig.babylog.ui.fragment.milestones.Milestones;
import com.squareup.otto.Subscribe;

import java.sql.SQLException;
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
    private BabyLoggerORMUtils babyORMLiteUtils;
    private PreparedQuery<DiaperChangeDao> queryBuilder;
//    private int LOADER_ID = 0;

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


//        String logItems = preferences.getString("logItems", "");


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
//            Log.d(TAG, "itemarraylist is not there in parambundle");
            for (String item: itemNames) {
                itemModelArrayList.add(new ItemModel(item, false));
            }
        }





        exportListAdapter = new ExportItemAdapter(getActivity(),R.layout.list_item_export, itemModelArrayList);
        exportListView.setAdapter(exportListAdapter);

        exportListView.setOnItemClickListener(this);

        babyORMLiteUtils = new BabyLoggerORMUtils(getActivity());





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
            String dayStr = day>=10? String.valueOf(day): "0" + String.valueOf(day);
            String monthStr = month>=10?String.valueOf(month): "0" + String.valueOf(month);

            Log.d(TAG, "day " + day + " month " + month + " year " + year);
            if (dateSetEvent.getLabel().equals("start")){
                dateStartYearTextView.setText("" + year);
                dateStartMonthTextView.setText(monthStr);
                dateStartDayTextView.setText(dayStr);
            } else {
                dateEndYearTextView.setText("" + year);
                dateEndMonthTextView.setText(monthStr);
                dateEndDayTextView.setText(dayStr);
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
        if (!isItemSelected()) {
            Toast.makeText(getActivity(), R.string.text_toast_no_items_selected_for_export, Toast.LENGTH_SHORT).show();
        } else {
            if (isDiaperLogSelected()) {
                try {
                    List<DiaperChangeDao> diaperChangeList = babyORMLiteUtils.getDiaperChangeList(getStartTime(), getEndTime());
                    Log.d(TAG, "number of rows : " +  diaperChangeList.size());
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
            
            if (isFeedSelected()) {
                try {
                    List<FeedDao> feedList = babyORMLiteUtils.getFeedList(getStartTime(), getEndTime());
                    Log.d(TAG, "number of rows : " +  feedList.size());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (isGrowthSelected()) {
                try {
                    List<GrowthDao> growthList = babyORMLiteUtils.getGrowthList(getStartTime(), getEndTime());
                    Log.d(TAG, "number of rows : " +  growthList.size());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (isMileStoneSelected()) {
                try {
                    List<MilestonesDao> milestonesList = babyORMLiteUtils.getMilestoneList(getStartTime(), getEndTime());
                    Log.d(TAG, "number of rows : " +  milestonesList.size());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


    }




    private Date getStartTime() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, Integer.parseInt(dateStartDayTextView.getText().toString()));
        c.set(Calendar.MONTH, Integer.parseInt(dateStartMonthTextView.getText().toString())-1);
        c.set(Calendar.YEAR, Integer.parseInt(dateStartYearTextView.getText().toString()));
        return c.getTime();


    }

    private Date getEndTime() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, Integer.parseInt(dateEndDayTextView.getText().toString()));
        c.set(Calendar.MONTH, Integer.parseInt(dateEndMonthTextView.getText().toString()) - 1);
        c.set(Calendar.YEAR, Integer.parseInt(dateEndYearTextView.getText().toString()));
        return c.getTime();
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

        //add label to indicate which date is being set...
        args.putString("label", label);

        //if the dialog is for the start date make sure the max date < end_date
        if (label.equals("start")) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
            String endDateString = dateEndMonthTextView.getText().toString() + "-" + dateEndDayTextView.getText().toString() + "-" + dateEndYearTextView.getText().toString();

            try {
                Log.d(TAG, "endDateString: " + endDateString);
                Date endDate = sdf.parse(endDateString);
                Log.d(TAG, "endDate " + endDate);
                args.putLong("max_start_date", endDate.getTime());
                args.putLong("current_date", getStartTime().getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            args.putLong("current_date", getEndTime().getTime());
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


    private boolean isItemSelected() {
        for (Parcelable model :exportListAdapter.getLogListItem()) {
            ItemModel itemModel = (ItemModel) model;
            if (itemModel.isItemChecked()) {
                return true;
            }
        }

        return false;
    }

    /*
     * @param null
     * @return boolean :  whether diaperlog is requested.
     */

    private boolean isDiaperLogSelected() {
        ItemModel itemModel = (ItemModel)exportListAdapter.getLogListItem().get(0);
        return itemModel.isItemChecked();
    }

    /*
     * @param null
     * @return boolean :  whether feedlog is requested.
     */
    private boolean isFeedSelected() {
        ItemModel itemModel = (ItemModel)exportListAdapter.getLogListItem().get(1);
        return itemModel.isItemChecked();
    }


    /*
     * @param null
     * @return boolean :  whether growthlog is requested.
     */

    private boolean isGrowthSelected() {
        ItemModel itemModel = (ItemModel)exportListAdapter.getLogListItem().get(2);
        return itemModel.isItemChecked();
    }

       /*
     * @param null
     * @return boolean :  whether growthlog is requested.
     */

    private boolean isMileStoneSelected() {
        ItemModel itemModel = (ItemModel)exportListAdapter.getLogListItem().get(3);
        return itemModel.isItemChecked();
    }

}
