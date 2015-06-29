package com.rorlig.babylog.ui.fragment.export;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import com.rorlig.babylog.ui.fragment.InjectableFragment;
import com.rorlig.babylog.ui.fragment.datetime.DatePickerFragment;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
            Uri diaperChangeUri = null;
            Uri growthUri = null;
            Uri feedsUri = null;
            Uri milestonesUri = null;
            if (isDiaperLogSelected()) {
                try {
                    List<DiaperChangeDao> diaperChangeList = babyORMLiteUtils.getDiaperChangeList(getStartTime(), getEndTime());
                    Log.d(TAG, "number of rows : " +  diaperChangeList.size());
                    if (diaperChangeList.size()>0)
                        diaperChangeUri  = createDiaperListToCSV(diaperChangeList);

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
            
            if (isFeedSelected()) {
                try {
                    List<FeedDao> feedList = babyORMLiteUtils.getFeedList(getStartTime(), getEndTime());
                    Log.d(TAG, "number of rows : " +  feedList.size());
                    if (feedList.size()>0)
                        feedsUri  = createFeedListToCSV(feedList);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (isGrowthSelected()) {
                try {
                    List<GrowthDao> growthList = babyORMLiteUtils.getGrowthList(getStartTime(), getEndTime());
                    Log.d(TAG, "number of rows : " +  growthList.size());
                    if (growthList.size()>0)
                        growthUri  = createGrowthListToCSV(growthList);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (isMileStoneSelected()) {
                try {
                    List<MilestonesDao> milestonesList = babyORMLiteUtils.getMilestoneList(getStartTime(), getEndTime());
                    Log.d(TAG, "number of rows : " +  milestonesList.size());
//                    if (milestonesList.size()>0)
//                        growthUri  = createGrowthListToCSV(growthList);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            sendEmail(diaperChangeUri, feedsUri, growthUri, "Diaper Logs");
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
     * checks if the diaper logs is requested
     * @param null
     * @return boolean :  whether diaperlog is requested.
     */

    private boolean isDiaperLogSelected() {
        ItemModel itemModel = (ItemModel)exportListAdapter.getLogListItem().get(0);
        return itemModel.isItemChecked();
    }

    /*
     * checks if the feed logs is requested
     * @param null
     * @return boolean :  whether feedlog is requested.
     */
    private boolean isFeedSelected() {
        ItemModel itemModel = (ItemModel)exportListAdapter.getLogListItem().get(1);
        return itemModel.isItemChecked();
    }


    /*
     * checks if the growth logs is requested
     * @param null
     * @return boolean :  whether growthlog is requested.
     */

    private boolean isGrowthSelected() {
        ItemModel itemModel = (ItemModel)exportListAdapter.getLogListItem().get(2);
        return itemModel.isItemChecked();
    }

       /*
     * checks if the milestones logs is requested
     * @param null
     * @return boolean :  whether growthlog is requested.
     */

    private boolean isMileStoneSelected() {
        ItemModel itemModel = (ItemModel)exportListAdapter.getLogListItem().get(3);
        return itemModel.isItemChecked();
    }


    /*
     * creates csv file for diaper change
     * @param List<DiaperChangeDao> list of diaperchangedao
     * @return Uri to the file location.
     */

    private Uri createDiaperListToCSV(List<DiaperChangeDao> diaperChangeList) {
        String header =   "\"Date\",\"Diaper Change Event Type\",\"Poop Texture\",\"Poop Color\",\"Diaper Incident\",\"Notes\"\n";
        StringBuilder stringBuilder = new StringBuilder();
        for (DiaperChangeDao diaperChangeDao: diaperChangeList) {
            stringBuilder.append("\"" + diaperChangeDao.getDate() + "\",\""
                    + diaperChangeDao.getDiaperChangeEventType() + "\",\""
                    + diaperChangeDao.getPoopTexture() + "\",\"" + diaperChangeDao.getPoopTexture() + "\",\""
                    + diaperChangeDao.getPoopColor() + "\",\"" + diaperChangeDao.getDiaperChangeIncidentType()
                    + "\",\"" + diaperChangeDao.getDiaperChangeNotes() + "\"\n");


        }
        String combinedString = header + stringBuilder.toString();
        Log.d(TAG, "combined " + combinedString);

        File file   = null;
        File root   = Environment.getExternalStorageDirectory();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");
        if (root.canWrite()){
            File dir    =   new File (root.getAbsolutePath() + "/DiaperLogs");
            dir.mkdirs();
            file   =   new File(dir, simpleDateFormat.format(getStartTime()) + " to " + simpleDateFormat.format(getEndTime()) + "_diaper"  + ".csv");
            FileOutputStream out   =   null;
            try {
                out = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                out.write(combinedString.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Uri uri  =   null;
        uri  =   Uri.fromFile(file);

        return uri;

    }


    /*
     * creates csv file for feed change
     * @param List<FeedDao> list of diaperchangedao
     * @return Uri to the file location.
     */

    private Uri createFeedListToCSV(List<FeedDao> feedList) {
        String header =   "\"Date\",\"Type\",\"Item\",\"Quantity\",\"Left Breast Time\",\"Right Breast Time\",\"Notes\"\n";
        StringBuilder stringBuilder = new StringBuilder();
        for (FeedDao feedItem: feedList) {
            stringBuilder.append("\"" + feedItem.getDate() + "\",\""
                    + feedItem.getFeedType() + "\",\""
                    + feedItem.getFeedItem() + "\",\"" + feedItem.getQuantity() + "\",\""
                    + feedItem.getLeftBreastTime() + "\",\"" + feedItem.getLeftBreastTime()
                    + "\",\"" + feedItem.getNotes() + "\"\n");


        }
        String combinedString = header + stringBuilder.toString();
        Log.d(TAG, "combined " + combinedString);

        File file   = null;
        File root   = Environment.getExternalStorageDirectory();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");

        if (root.canWrite()){
            File dir    =   new File (root.getAbsolutePath() + "/FeedLogs");
            dir.mkdirs();
            file   =   new File(dir, simpleDateFormat.format(getStartTime()) + " to " + simpleDateFormat.format(getEndTime()) + "_feed"  + ".csv");
            FileOutputStream out   =   null;
            try {
                out = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                out.write(combinedString.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Uri uri  =   null;
        uri  =   Uri.fromFile(file);

        return uri;

    }


     /*
     * creates csv file for feed change
     * @param List<GrowthDao> list of diaperchangedao
     * @return Uri to the file location.
     */

    private Uri createGrowthListToCSV(List<GrowthDao> growthList) {
        String header =   "\"Date\",\"Weight\",\"Height\",\"Head Measurement\",\"Notes\"\n";
        StringBuilder stringBuilder = new StringBuilder();
        for (GrowthDao growthItem: growthList) {
            stringBuilder.append("\"" + growthItem.getDate() + "\",\""
                    + growthItem.getWeight() + "\",\""
                    + growthItem.getHeight() + "\",\"" + growthItem.getHeadMeasurement() + "\",\""
                    + "\",\"" + growthItem.getNotes() + "\"\n");


        }
        String combinedString = header + stringBuilder.toString();
        Log.d(TAG, "combined " + combinedString);

        File file   = null;
        File root   = Environment.getExternalStorageDirectory();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");

        if (root.canWrite()){
            File dir    =   new File (root.getAbsolutePath() + "/GrowthLogs");
            dir.mkdirs();
            file   =   new File(dir, simpleDateFormat.format(getStartTime()) + " to " + simpleDateFormat.format(getEndTime()) + "_growth"  + ".csv");
            FileOutputStream out   =   null;
            try {
                out = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                out.write(combinedString.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Uri uri  =   null;
        uri  =   Uri.fromFile(file);

        return uri;

    }
    /*
     * sendEmail with attachment
     * @param Uri uri : - uri of the file to emailed...
     * @param String subject
     */
    private void sendEmail(Uri diaperChangeUri, Uri feedsUri, Uri growthUri, String subject) {
        ArrayList<Uri> uri = new ArrayList<Uri>();

        if (diaperChangeUri!=null) uri.add(diaperChangeUri);
        if (feedsUri!=null) uri.add(feedsUri);
        if (growthUri!=null) uri.add(growthUri);

        Intent sendIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        sendIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uri);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                new String[]{"guptgaurav@gmail.com"});

        startActivity(Intent.createChooser(sendIntent, "Send Email"));
        getActivity().finish();
    }


}
