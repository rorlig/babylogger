package com.rorlig.babyapp.ui.fragment.export;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.rorlig.babyapp.dao.FeedDao;
import com.rorlig.babyapp.dao.GrowthDao;
import com.rorlig.babyapp.dao.MilestonesDao;
import com.rorlig.babyapp.dao.SleepDao;
import com.rorlig.babyapp.model.ItemModel;
import com.rorlig.babyapp.otto.BusProvider;
import com.rorlig.babyapp.otto.DiaperStatsEvent;
import com.rorlig.babyapp.otto.ScopedBus;
import com.rorlig.babyapp.otto.UriCreated;
import com.rorlig.babyapp.parse_dao.DiaperChange;
import com.rorlig.babyapp.parse_dao.Feed;
import com.rorlig.babyapp.parse_dao.Growth;
import com.rorlig.babyapp.parse_dao.Milestones;
import com.rorlig.babyapp.parse_dao.Sleep;
import com.rorlig.babyapp.ui.fragment.diaper.DiaperChangeStatsType;
import com.rorlig.babyapp.utils.AppConstants;
import com.squareup.otto.Bus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author gaurav gupta
 */
public class ExportUtility {

    private static String TAG = "ExportUtility";

    private static Date startTime;

    private static Date endTime;

    private static Bus bus = BusProvider.getInstance();

    /*
     * @param List<ItemModel> listItemModel -- list of items required to export..
     */
    public static void getExportItems(List<ItemModel> listItemModel, Date startTime, Date endTime){
        Map<String, Object> params = new HashMap<>();

        for(ItemModel itemModel: listItemModel) {
            Log.d(TAG, "itemModel " + itemModel.getItemParseName());
            params.put(itemModel.getItemParseName(), "yes");
        }

        params.put("startTime", startTime);
        params.put("endTime", endTime);

        ExportUtility.startTime = new Date(startTime.getTime());
        ExportUtility.endTime = new Date(endTime.getTime());



        ParseCloud.callFunctionInBackground(AppConstants.EXPORT_DATA_VIA_EMAIL,
                params, new FunctionCallback<Object>() {
                    @Override
                    public void done(Object object, ParseException e) {
                        Log.d(TAG, "exportDataViaEmail " + object + " e " + e);
                        //now need to parse this
                        ArrayList<Uri> uris = new ArrayList<Uri>();

                        if (e==null) {
                            HashMap mp = (HashMap) object;
                            Set keySets = mp.keySet();
                            for (Object key: keySets){
                                Log.d(TAG, key.toString());
                                ArrayList<ParseObject> list = (ArrayList<ParseObject>) mp.get(key.toString());
                                if (list.size()>0)
                                uris.add(createUri(list));


                            }
                            bus.post(new UriCreated(uris));

                        }
             }
        });
    }

    private static Uri createUri(ArrayList<ParseObject> list) {
       if (list.get(0).getClassName().equals("Diaper")) {
           return createDiaperChangeUri(list);
       } else if (list.get(0).getClassName().equals("Feed")) {
           return createFeedUri(list);
       } else if (list.get(0).getClassName().equals("Growth")) {
           return createGrowthListToCSV(list);
       } else if (list.get(0).getClassName().equals("Sleep")){
           return createSleepListToCSV(list);
       } else {
           return createMilestoneListToCSV(list);
       }
    }


    private static Uri createDiaperChangeUri(ArrayList<ParseObject> list) {

        String header =   "\"Date\",\"Diaper Change Event Type\",\"Poop Texture\",\"Poop Color\",\"Diaper Incident\",\"Notes\"\n";
        StringBuilder stringBuilder = new StringBuilder();
        for (ParseObject object: list) {
            DiaperChange diaperChange = (DiaperChange) object;
            stringBuilder.append("\"" + diaperChange.getLogCreationDate() + "\",\""
                    + diaperChange.getDiaperChangeEventType() + "\",\""
                    + diaperChange.getPoopTexture() + "\",\"" + diaperChange.getPoopTexture() + "\",\""
                    + diaperChange.getPoopColor() + "\",\"" + diaperChange.getDiaperChangeIncidentType()
                    + "\",\"" + diaperChange.getDiaperChangeNotes() + "\"\n");


        }
        String combinedString = header + stringBuilder.toString();
        Log.d(TAG, "combined " + combinedString);

        File file   = null;
        File root   = Environment.getExternalStorageDirectory();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");
        if (root.canWrite()){
            File dir    =   new File (root.getAbsolutePath() + "/DiaperLogs");
            dir.mkdirs();
            file   =   new File(dir, simpleDateFormat.format(ExportUtility.startTime) + " to " + simpleDateFormat.format(ExportUtility.endTime) + "_diaper"  + ".csv");
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
        Uri uri;
        uri  =   Uri.fromFile(file);

        return uri;
//        for (ParseObject parseObject: list) {
//            DiaperChange diaperChange = (DiaperChange) parseObject;
//        }
    }



     /*
     * creates csv file for diaper change
     * @param List<DiaperChangeDao> list of diaperchangedao
     * @return Uri to the file location.
     */

    private static Uri createDiaperListToCSV(List<ParseObject> diaperChangeList) {
        String header =   "\"Date\",\"Diaper Change Event Type\",\"Poop Texture\",\"Poop Color\",\"Diaper Incident\",\"Notes\"\n";
        StringBuilder stringBuilder = new StringBuilder();
        for (ParseObject object: diaperChangeList) {
            DiaperChange diaperChange = (DiaperChange) object;
            stringBuilder.append("\"" + diaperChange.getLogCreationDate() + "\",\""
                    + diaperChange.getDiaperChangeEventType() + "\",\""
                    + diaperChange.getPoopTexture() + "\",\"" + diaperChange.getPoopTexture() + "\",\""
                    + diaperChange.getPoopColor() + "\",\"" + diaperChange.getDiaperChangeIncidentType()
                    + "\",\"" + diaperChange.getDiaperChangeNotes() + "\"\n");


        }
        String combinedString = header + stringBuilder.toString();
        Log.d(TAG, "combined " + combinedString);

        File file   = null;
        File root   = Environment.getExternalStorageDirectory();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");
        if (root.canWrite()){
            File dir    =   new File (root.getAbsolutePath() + "/DiaperLogs");
            dir.mkdirs();
            file   =   new File(dir, simpleDateFormat.format(ExportUtility.startTime) + " to " + simpleDateFormat.format(ExportUtility.endTime) + "_diaper"  + ".csv");
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
        Uri uri;
        uri  =   Uri.fromFile(file);

        return uri;

    }


     /*
     * creates csv file for feed change
     * @param List<FeedDao> list of diaperchangedao
     * @return Uri to the file location.
     */

    private static Uri createFeedUri(ArrayList<ParseObject> feedList) {
        String header =   "\"Date\",\"Type\",\"Item\",\"Quantity\",\"Left Breast Time (min) \",\"Right Breast Time (min)\",\"Notes\"\n";
        StringBuilder stringBuilder = new StringBuilder();
        for (ParseObject parseObject: feedList) {
            Feed feedItem = (Feed) parseObject;
            stringBuilder.append("\"" + feedItem.getLogCreationDate() + "\",\""
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
            file   =   new File(dir, simpleDateFormat.format(ExportUtility.startTime) + " to " + simpleDateFormat.format(ExportUtility.endTime) + "_feed"  + ".csv");
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

        return Uri.fromFile(file);

    }


     /*
     * creates csv file for feed change
     * @param List<GrowthDao> list of diaperchangedao
     * @return Uri to the file location.
     */

    private static Uri createGrowthListToCSV(ArrayList<ParseObject> growthList) {
        String header =   "\"Date\",\"Weight\",\"Height\",\"Head Measurement\",\"Notes\"\n";
        StringBuilder stringBuilder = new StringBuilder();
        for (ParseObject parseObject: growthList) {
            Growth growthItem = (Growth) parseObject;
            stringBuilder.append("\"" + growthItem.getLogCreationDate() + "\",\""
                    + growthItem.getWeight() + "\",\""
                    + growthItem.getHeight() + "\",\"" + growthItem.getHeadMeasurement() + "\",\""
                    + growthItem.getNotes() + "\"\n");


        }
        String combinedString = header + stringBuilder.toString();
        Log.d(TAG, "combined " + combinedString);

        File file   = null;
        File root   = Environment.getExternalStorageDirectory();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");

        if (root.canWrite()){
            File dir    =   new File (root.getAbsolutePath() + "/GrowthLogs");
            dir.mkdirs();
            file   =   new File(dir, simpleDateFormat.format(startTime) + " to " + simpleDateFormat.format(endTime) + "_growth"  + ".csv");
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
   * @param List<MilestonesDao> list of milestonedao
   * @return Uri to the file location.
   */
    private static Uri createMilestoneListToCSV(ArrayList<ParseObject> milestonesList) {
        String header =   "\"Date\",\"Milestone\"\n";
        StringBuilder stringBuilder = new StringBuilder();
        for (ParseObject parseObject: milestonesList) {
            Milestones milestones = (Milestones) parseObject;
            stringBuilder.append("\"" + milestones.getLogCreationDate() + "\",\""
                    + milestones.getTitle() + "\"\n");
        }
        String combinedString = header + stringBuilder.toString();
        Log.d(TAG, "combined " + combinedString);

        File file   = null;
        File root   = Environment.getExternalStorageDirectory();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");

        if (root.canWrite()){
            File dir    =   new File (root.getAbsolutePath() + "/MileStoneLogs");
            dir.mkdirs();
            file   =   new File(dir, simpleDateFormat.format(startTime) + " to " + simpleDateFormat.format(endTime) + "_milestones"  + ".csv");
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


        return  Uri.fromFile(file);

    }


     /*
     * creates csv file for feed change
     * @param List<FeedDao> list of diaperchangedao
     * @return Uri to the file location.
     */

    private static Uri createSleepListToCSV(ArrayList<ParseObject> sleepDaoList) {
        String header =   "\"Date\",\"Start Time\",\"Duration\"\n";
        StringBuilder stringBuilder = new StringBuilder();
        for (ParseObject parseObject: sleepDaoList) {
            Sleep sleepItem = (Sleep) parseObject;
            stringBuilder.append("\"" + sleepItem.getLogCreationDate() + "\",\""
                    + sleepItem.getLogCreationDate() + "\",\""
                    + sleepItem.getSleepStartTime() + "\",\"" + sleepItem.getDuration() + "\",\"n");


        }
        String combinedString = header + stringBuilder.toString();
        Log.d(TAG, "combined " + combinedString);

        File file   = null;
        File root   = Environment.getExternalStorageDirectory();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");

        if (root.canWrite()){
            File dir    =   new File (root.getAbsolutePath() + "/SleepLogs");
            dir.mkdirs();
            file   =   new File(dir, simpleDateFormat.format(startTime) + " to " + simpleDateFormat.format(endTime) + "_sleep"  + ".csv");
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

        return Uri.fromFile(file);

    }
}
