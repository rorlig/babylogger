package com.rorlig.babyapp.ui.fragment.diaper;

import android.util.Log;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.rorlig.babyapp.otto.BusProvider;
import com.rorlig.babyapp.otto.DiaperStatsEvent;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author gaurav gupta
 */
public class DiaperStatsUtility {

    private static String TAG = "DiaperStatsUtility";

    private static Bus bus = BusProvider.getInstance();

    public static void getDiapersByDayofWeek(){
        Map<String, String> params = new HashMap<String, String>();

        ParseCloud.callFunctionInBackground("diaperChangesByDayofWeek",
                params, new FunctionCallback<Object>() {
                    @Override
                    public void done(Object object, ParseException e) {
                        Log.d(TAG, "diaperChangesByDayofWeek " + object + " e " + e);
                        HashMap mp = (HashMap) object;
                        List<String[]> list = new ArrayList<String[]>();
                        Iterator it = mp.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next();
                            Log.d(TAG, pair.getKey() + " = " + pair.getValue());
                            String str[] = new String [2];
                            str[0] = (String) pair.getKey();
                            str[1] = String.valueOf(pair.getValue());
                            list.add(str);
//                            it.remove(); // avoids a ConcurrentModificationException
                        }

                        bus.post(new DiaperStatsEvent(list, DiaperChangeStatsType.WEEKLY) );
                    }
                });
    }


    public static void getDiapersByWeekofMonth(){
        ParseCloud.callFunctionInBackground("diaperChangesByWeekofMonth",
                new HashMap<String, String>(), new FunctionCallback<Object>() {
                    @Override
                    public void done(Object object, ParseException e) {
                        Log.d(TAG, "diaperChangesByWeekofMonth " + object  + " type " + object.getClass());
                        HashMap mp = (HashMap) object;
                        List<String[]> list = new ArrayList<String[]>();
                        Iterator it = mp.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next();
                            Log.d(TAG, pair.getKey() + " = " + pair.getValue());
                            String str[] = new String [2];
                            str[0] = (String) pair.getKey();
                            str[1] = String.valueOf(pair.getValue());
                            list.add(str);
//                            it.remove(); // avoids a ConcurrentModificationException
                        }

                        bus.post(new DiaperStatsEvent(list, DiaperChangeStatsType.MONTHLY));

                    }
                });
    }


    public static void getDiapersByMonthofYear(){
        ParseCloud.callFunctionInBackground("diaperChangesByMonthofYear",
                new HashMap<String, String>(), new FunctionCallback<Object>() {
                    @Override
                    public void done(Object object, ParseException e) {
                        Log.d(TAG, "diaperChangesByMonthofYear" + object );
                        HashMap mp = (HashMap) object;
                        List<String[]> list = new ArrayList<String[]>();
                        Iterator it = mp.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next();
                            Log.d(TAG, pair.getKey() + " = " + pair.getValue());
                            String str[] = new String [2];
                            str[0] = (String) pair.getKey();
                            str[1] = String.valueOf(pair.getValue());
                            list.add(str);
//                            it.remove(); // avoids a ConcurrentModificationException
                        }

                        bus.post(new DiaperStatsEvent(list, DiaperChangeStatsType.YEARLY));
                    }
                });
    }



}
