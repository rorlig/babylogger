package com.rorlig.babyapp.ui.fragment.export;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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

import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.dagger.ForActivity;
import com.rorlig.babyapp.model.ItemModel;
import com.rorlig.babyapp.otto.UriCreated;
import com.rorlig.babyapp.otto.events.datetime.DateSetEvent;
import com.rorlig.babyapp.otto.events.ui.FragmentCreated;
import com.rorlig.babyapp.ui.adapter.ExportItemAdapter;
import com.rorlig.babyapp.ui.fragment.InjectableFragment;
import com.rorlig.babyapp.ui.fragment.datetime.DatePickerFragment;
import com.rorlig.babyapp.utils.AppUtils;
import com.squareup.otto.Subscribe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import bolts.Continuation;
import bolts.Task;
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
    private Uri milestoneUri;
    private Uri sleepUri;
    private String[] itemParseName;
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
        itemParseName = getResources().getStringArray(R.array.items_array);
        List<Parcelable> itemModelArrayList = new ArrayList<Parcelable>();

        Parcelable[] itemList;

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
            for (int i=0; i<itemNames.length; ++i) {
                itemModelArrayList.add(new ItemModel(itemNames[i], false, itemParseName[i]));
            }
        }



        dateStartDayTextView.setText(AppUtils.getSpannable(dateStartDayTextView.getText().toString(), getResources().getColor(R.color.primary_blue)));
        dateStartMonthTextView.setText(AppUtils.getSpannable(dateStartMonthTextView.getText().toString(), getResources().getColor(R.color.primary_blue)));
        dateStartYearTextView.setText(AppUtils.getSpannable(dateStartYearTextView.getText().toString(), getResources().getColor(R.color.primary_blue)));
        dateEndDayTextView.setText(AppUtils.getSpannable(dateEndDayTextView.getText().toString(), getResources().getColor(R.color.primary_blue)));
        dateEndMonthTextView.setText(AppUtils.getSpannable(dateEndMonthTextView.getText().toString(), getResources().getColor(R.color.primary_blue)));
        dateEndYearTextView.setText(AppUtils.getSpannable(dateEndYearTextView.getText().toString(), getResources().getColor(R.color.primary_blue)));





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
        dateEndMonthTextView.setText((month < 10 ? "0" + month : "" + month));
        dateEndDayTextView.setText(day < 10 ? "0" + day : "" + day);

    }

    private void setStartDate() {

        final Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -7);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        dateStartYearTextView.setText("" + year);
        dateStartMonthTextView.setText((month < 10 ? "0" + month : "" + month));
        dateStartDayTextView.setText(day < 10 ? "0" + day : "" + day);


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

        @Subscribe
        public void onUriEvent(UriCreated event){
            Log.d(TAG, "onUriEvent ");
            sendEmail(event.getUris());

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
            ExportUtility.getExportItems(itemsSelected(), getStartTime(), getEndTime());
//            ParseQuery<ParseObject> diaperQuery = ParseQuery.getQuery("Diaper");
//            ParseQuery<ParseObject> feedQuery = ParseQuery.getQuery("Feed");
//            ParseQuery<ParseObject> sleepQuery = ParseQuery.getQuery("Sleep");
//            ParseQuery<ParseObject> milestoneQuery = ParseQuery.getQuery("Milestone");
//            ParseQuery<ParseObject> growthQuery = ParseQuery.getQuery("Growth");
//            ArrayList<Task<ParseObject>> tasks = new ArrayList<>();
//
//            List<ParseObject> list = new ArrayList<>();
//            list.add(new DiaperChange());
//            list.add(new Feed());


//            getExportableData(list);
//            fetchAllAsync(list);
//            List<ParseQuery<ParseObject>> parseQueries = new ArrayList<>();
//            if (isItemSelected(ExportItem.DIAPER)) {
//                tasks.add(fetchAsync(new DiaperChange()));
//                parseQueries.add(diaperQuery);
//            }
//            if (isItemSelected(ExportItem.FEED)) {
//                tasks.add(fetchAsync(new Feed()));
//                parseQueries.add(feedQuery);
//
//            }
//
////            parseQueries.add(feedQuery);
//            if (isItemSelected(ExportItem.SLEEP)) {
//                tasks.add(fetchAsync(new Sleep()));
//                parseQueries.add(sleepQuery);
//
//            }
//
////            parseQueries.add(sleepQuery);
//            if (isItemSelected(ExportItem.GROWTH)) {
//                tasks.add(fetchAsync(new Growth()));
//                parseQueries.add(growthQuery);
//
//            }
//
////            parseQueries.add(growthQuery);
//            if (isItemSelected(ExportItem.MILESTONE)) {
//                tasks.add(fetchAsync(new Milestones()));
//                parseQueries.add(milestoneQuery);
//
//            }
//
//            fetchAllAsync(parseQueries);
//            for (Task task: tasks)
//                    task.
//
////            parseQueries.add(milestoneQuery);
////            Bolts bolts = new Bolts();
////            bolts.
////            for (ParseQuery<ParseObject> parseObjectParseQuery: parseQueries) {
////            }
//            Task.
            Uri diaperChangeUri = null;
            Uri growthUri = null;
            Uri feedsUri = null;
//            Uri milestonesUri = null;
            if (isItemSelected(ExportItem.DIAPER)) {

                Log.d(TAG, " startTime " + getStartTime() + " endTime " + getEndTime());
//
//                ParseQuery<ParseObject> query = ParseQuery.getQuery("Diaper");
//                query.whereGreaterThanOrEqualTo("logCreationDate", getStartTime() );
//                query.whereLessThanOrEqualTo("logCreationDate", getEndTime());
//
//                query.findInBackground().onSuccess(new Continuation<List<ParseObject>, Object>() {
//
//                    @Override
//                    public Object then(Task<List<ParseObject>> task) throws Exception {
//                        Uri diaperChangeUri = createDiaperListToCSV(task.getResult());
//
//
//                        return null;
//                    }
//
//
//                });
//                query.findInBackground(new FindCallback<ParseObject>() {
//                    @Override
//                    public void done(List<ParseObject> objects, com.parse.ParseException e) {
//                        Log.d(TAG, "find diapers callback");
//                        if (objects != null) {
//                            Log.d(TAG, "object size " + objects.size());
//                            Uri diaperChangeUri = createDiaperListToCSV(objects);
//                        }
//                    }
//                });

//                    List<DiaperChangeDao> diaperChangeList = babyORMLiteUtils.getDiaperChangeList(getStartTime(), getEndTime());
//                    Log.d(TAG, "number of rows : " +  diaperChangeList.size());
//                    if (diaperChangeList.size()>0)
//                        diaperChangeUri  = createDiaperListToCSV(diaperChangeList);

            }

            if (isItemSelected(ExportItem.FEED)) {

                Log.d(TAG, " startTime " + getStartTime() + " endTime " + getEndTime());

//                ParseQuery<ParseObject> query = ParseQuery.getQuery("Feed");
//                query.whereGreaterThanOrEqualTo("logCreationDate", getStartTime() );
//                query.whereLessThanOrEqualTo("logCreationDate", getEndTime());
//                query.findInBackground(new FindCallback<ParseObject>() {
//                    @Override
//                    public void done(List<ParseObject> objects, com.parse.ParseException e) {
//                        Log.d(TAG, "find feed callback");
//                        if (objects != null) {
//                            Log.d(TAG, "object size " + objects.size());
//                            Uri f = createDiaperListToCSV(objects);
//                        }
//                    }
//                });

//                try {
//                    List<FeedDao> feedList = babyORMLiteUtils.getFeedList(getStartTime(), getEndTime());
//                    Log.d(TAG, "number of rows : " +  feedList.size());
//                    if (feedList.size()>0)
//                        feedsUri  = createFeedListToCSV(feedList);
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
            }
//
//            if (isItemSelected(ExportItem.GROWTH)) {
//                try {
//                    List<GrowthDao> growthList = babyORMLiteUtils.getGrowthList(getStartTime(), getEndTime());
//                    Log.d(TAG, "number of rows : " +  growthList.size());
//                    if (growthList.size()>0)
//                        growthUri  = createGrowthListToCSV(growthList);
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            if (isItemSelected(ExportItem.MILESTONE)) {
//                try {
//                    List<MilestonesDao> milestonesList = babyORMLiteUtils.getMilestoneList(getStartTime(), getEndTime());
//                    Log.d(TAG, "number of rows : " +  milestonesList.size());
//
//                    if (milestonesList.size()>0)
//                        milestoneUri = createMilestoneListToCSV(milestonesList);
////                    if (milestonesList.size()>0)
////                        growthUri  = createGrowthListToCSV(growthList);
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            if (isItemSelected(ExportItem.SLEEP)) {
//                try {
//                    List<SleepDao> sleepList = babyORMLiteUtils.getSleepList(getStartTime(), getEndTime());
//                    Log.d(TAG, "number of rows : " + sleepList.size());
//                    if (sleepList.size()>0)
//                        sleepUri  = createSleepListToCSV(sleepList);
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }



//            sendEmail(diaperChangeUri, feedsUri, growthUri, milestoneUri, sleepUri, "Logs");
        }


    }




    private Date getStartTime() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, Integer.parseInt(dateStartDayTextView.getText().toString()));
        c.set(Calendar.MONTH, Integer.parseInt(dateStartMonthTextView.getText().toString()) - 1);
        c.set(Calendar.YEAR, Integer.parseInt(dateStartYearTextView.getText().toString()));
        return c.getTime();


    }

    private Date getEndTime() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, Integer.parseInt(dateEndDayTextView.getText().toString()) + 1);
        c.set(Calendar.MONTH, Integer.parseInt(dateEndMonthTextView.getText().toString()) - 1);
        c.set(Calendar.YEAR, Integer.parseInt(dateEndYearTextView.getText().toString()));
        c.set(Calendar.HOUR, 0);
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


    private List<ItemModel> itemsSelected(){
        List<ItemModel> itemModelList = new ArrayList<>();
        for (Parcelable model :exportListAdapter.getLogListItem()) {
            ItemModel itemModel = (ItemModel) model;
            if (itemModel.isItemChecked()) {
                itemModelList.add(itemModel);
            }
        }
        return itemModelList;
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

    private boolean isItemSelected(ExportItem exportItem) {
        ItemModel itemModel = (ItemModel)exportListAdapter.getLogListItem().get(exportItem.ordinal());
        return itemModel.isItemChecked();
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
     * checks if the sleep logs is requested
     * @param null
     * @return boolean :  whether growthlog is requested.
     */


    private boolean isSleepSelected() {
        ItemModel itemModel = (ItemModel)exportListAdapter.getLogListItem().get(4);
        return itemModel.isItemChecked();
    }




    /*
   * creates csv file for feed change
   * @param List<MilestonesDao> list of milestonedao
   * @return Uri to the file location.
   */
//    private Uri createMilestoneListToCSV(List<
    /*
     * sendEmail with attachment
     * @param Uri uri : - uri of the file to emailed...
     * @param String subject
     */
    private void sendEmail(Uri diaperChangeUri, Uri growthUri,
                           Uri feedsUri,
                           Uri sleepUri,
                           Uri milestoneUri,
                           String subject) {
        ArrayList<Uri> uri = new ArrayList<Uri>();

        if (diaperChangeUri!=null) uri.add(diaperChangeUri);
        if (feedsUri!=null) uri.add(feedsUri);
        if (growthUri!=null) uri.add(growthUri);
        if (milestoneUri!=null) uri.add(milestoneUri);
        if (sleepUri!=null) uri.add(sleepUri);

        Intent sendIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        sendIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uri);
        sendIntent.setType("message/rfc822");
        sendIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                new String[]{});

        startActivity(Intent.createChooser(sendIntent, "Send Email"));
        getActivity().finish();
    }

    private void sendEmail(ArrayList<Uri> uris) {
        ArrayList<Uri> emailUris = new ArrayList<Uri>();

        for (Uri uri : uris){
            Log.d(TAG, "uri " + uri.getPath());
            emailUris.add(uri);
        }

//        if (diaperChangeUri!=null) uri.add(diaperChangeUri);
//        if (feedsUri!=null) uri.add(feedsUri);
//        if (growthUri!=null) uri.add(growthUri);
//        if (milestoneUri!=null) uri.add(milestoneUri);
//        if (sleepUri!=null) uri.add(sleepUri);

        Intent sendIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Logs");
        sendIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, emailUris);
        sendIntent.setType("message/rfc822");
        sendIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                new String[]{});

        startActivity(Intent.createChooser(sendIntent, "Send Email"));
        getActivity().finish();

    }

    public Task<ParseObject> fetchAsync(final ParseObject obj) {
        final Task<ParseObject>.TaskCompletionSource tcs = Task.create();
        obj.fetchInBackground(new GetCallback() {

            @Override
            public void done(Object o, Throwable throwable) {

            }

            @Override
            public void done(ParseObject object, com.parse.ParseException e) {
                if (e == null) {
                    tcs.setResult(object);
                    Log.d(TAG, "object received " + object);
                } else {
                    tcs.setError(e);
                }
            }


        });
        return tcs.getTask();
    }

    public Task<Void> fetchAllAsync(List<ParseObject> obj) {
        Log.d(TAG, "fetchAllAsync ");
        ArrayList<Task<ParseObject>> tasks = new ArrayList<Task<ParseObject>>();
        for (ParseObject query : obj)
            tasks.add(fetchAsync(query));

        return Task.whenAll(tasks);
    }

    public void getExportableData(final List<ParseObject> obj) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Baby");

        query.findInBackground().onSuccess(new Continuation<List<ParseObject>, Object>() {
            @Override
            public Object then(Task<List<ParseObject>> task) throws Exception {
                ArrayList<Task<ParseObject>> tasks = new ArrayList<Task<ParseObject>>();
                for (ParseObject query : obj)
                    tasks.add(fetchAsync(query));
                return Task.whenAll(tasks);
            }
        });
    }

}
