package com.rorlig.babyapp.ui.fragment.sleep;

import android.util.Log;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.rorlig.babyapp.otto.BusProvider;
import com.rorlig.babyapp.otto.DiaperStatsEvent;
import com.rorlig.babyapp.otto.SleepStatsEvent;
import com.rorlig.babyapp.ui.fragment.diaper.DiaperChangeStatsType;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author gaurav gupta
 */
public class SleepStatsUtility {

    private static String TAG = "DiaperStatsUtility";

    private static Bus bus = BusProvider.getInstance();

    public static void getSleepDaysofWeek(){
        Map<String, String> params = new HashMap<String, String>();

        ParseCloud.callFunctionInBackground("sleepCountByDaysofWeek",
                params, new FunctionCallback<Object>() {
                    @Override
                    public void done(Object object, ParseException e) {
                        Log.d(TAG, "sleepCountByDaysofWeek " + object + " e " + e);
                        ArrayList mp = (ArrayList) object;
                        List<String[]> list = new ArrayList<String[]>();

                        for (Object o : mp){
                            Log.d(TAG, "o " + o);
                            HashMap hp = (HashMap) o;
                            String str[] = new String [2];
                            str[0] = (String) hp.get("logCreationDate");
                            str[1] = String.valueOf(hp.get("duration"));
                            list.add(str);
//                            Iterator it = hp.entrySet().iterator();
//                            while (it.hasNext()) {
//                                Map.Entry pair = (Map.Entry)it.next();
//                                Log.d(TAG, pair.getKey() + " = " + pair.getValue());
//                                String str[] = new String [2];
//                                str[0] = (String) pair.getKey();
//                                str[1] = String.valueOf(pair.getValue());
//                                list.add(str);
//                                //                            it.remove(); // avoids a ConcurrentModificationException
//                            }

                        }
//                        List<String[]> list = new ArrayList<String[]>();
//                        Iterator it = mp.entrySet().iterator();
//                        while (it.hasNext()) {
//                            Map.Entry pair = (Map.Entry)it.next();
//                            Log.d(TAG, pair.getKey() + " = " + pair.getValue());
//                            String str[] = new String [2];
//                            str[0] = (String) pair.getKey();
//                            str[1] = String.valueOf(pair.getValue());
//                            list.add(str);
////                            it.remove(); // avoids a ConcurrentModificationException
//                        }

                        bus.post(new SleepStatsEvent(list, SleepStatsFragment.SleepStatsType.WEEKLY) );
                    }
                });
    }


    public static void getSleepByWeekofMonth(){
        ParseCloud.callFunctionInBackground("sleepCountByWeekofMonth",
                new HashMap<String, String>(), new FunctionCallback<Object>() {
                    @Override
                    public void done(Object object, ParseException e) {
                        Log.d(TAG, "sleepCountByWeekofMonth " + object  + " type " + object.getClass());
                        ArrayList mp = (ArrayList) object;
                        List<String[]> list = new ArrayList<String[]>();

                        for (Object o : mp){
                            Log.d(TAG, "o " + o);
                            HashMap hp = (HashMap) o;
                            String str[] = new String [2];
                            str[0] = (String) hp.get("logCreationDate");
                            str[1] = String.valueOf(hp.get("duration"));
                            list.add(str);
//                            Iterator it = hp.entrySet().iterator();
//                            while (it.hasNext()) {
//                                Map.Entry pair = (Map.Entry)it.next();
//                                Log.d(TAG, pair.getKey() + " = " + pair.getValue());
//                                String str[] = new String [2];
//                                str[0] = (String) pair.getKey();
//                                str[1] = String.valueOf(pair.getValue());
//                                list.add(str);
//                                //                            it.remove(); // avoids a ConcurrentModificationException
//                            }

                        }
//                        List<String[]> list = new ArrayList<String[]>();
//                        Iterator it = mp.entrySet().iterator();
//                        while (it.hasNext()) {
//                            Map.Entry pair = (Map.Entry)it.next();
//                            Log.d(TAG, pair.getKey() + " = " + pair.getValue());
//                            String str[] = new String [2];
//                            str[0] = (String) pair.getKey();
//                            str[1] = String.valueOf(pair.getValue());
//                            list.add(str);
////                            it.remove(); // avoids a ConcurrentModificationException
//                        }

                        bus.post(new SleepStatsEvent(list, SleepStatsFragment.SleepStatsType.MONTHLY) );

                    }
                });
    }


    public static void getSleepByMonthofYear(){
        ParseCloud.callFunctionInBackground("sleepCountByMonthofYear",
                new HashMap<String, String>(), new FunctionCallback<Object>() {
                    @Override
                    public void done(Object object, ParseException e) {
                        ArrayList mp = (ArrayList) object;
                        List<String[]> list = new ArrayList<String[]>();

                        for (Object o : mp){
                            Log.d(TAG, "o " + o);
                            HashMap hp = (HashMap) o;
                            String str[] = new String [2];
                            str[0] = (String) hp.get("logCreationDate");
                            str[1] = String.valueOf(hp.get("duration"));
                            list.add(str);
//                            Iterator it = hp.entrySet().iterator();
//                            while (it.hasNext()) {
//                                Map.Entry pair = (Map.Entry)it.next();
//                                Log.d(TAG, pair.getKey() + " = " + pair.getValue());
//                                String str[] = new String [2];
//                                str[0] = (String) pair.getKey();
//                                str[1] = String.valueOf(pair.getValue());
//                                list.add(str);
//                                //                            it.remove(); // avoids a ConcurrentModificationException
//                            }

                        }
//                        List<String[]> list = new ArrayList<String[]>();
//                        Iterator it = mp.entrySet().iterator();
//                        while (it.hasNext()) {
//                            Map.Entry pair = (Map.Entry)it.next();
//                            Log.d(TAG, pair.getKey() + " = " + pair.getValue());
//                            String str[] = new String [2];
//                            str[0] = (String) pair.getKey();
//                            str[1] = String.valueOf(pair.getValue());
//                            list.add(str);
////                            it.remove(); // avoids a ConcurrentModificationException
//                        }

                        bus.post(new SleepStatsEvent(list, SleepStatsFragment.SleepStatsType.YEARLY) );
                    }
                });
    }



}
