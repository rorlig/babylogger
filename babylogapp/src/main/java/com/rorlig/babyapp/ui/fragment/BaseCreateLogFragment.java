package com.rorlig.babyapp.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.dagger.ForApplication;
import com.rorlig.babyapp.otto.ItemDeleted;
import com.rorlig.babyapp.otto.events.growth.ItemCreatedOrChanged;
import com.rorlig.babyapp.ui.widget.DateTimeHeaderFragment;
import com.rorlig.babyapp.utils.AppConstants;
import com.rorlig.babyapp.utils.AppUtils;

import javax.inject.Inject;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

/**
 * @author gaurav gupta
 */
public abstract class BaseCreateLogFragment extends InjectableFragment{

    @ForApplication
    @Inject
    Context context;

    private final String parseClassName;
    private String TAG = "BaseCreateLogFragment";

    protected DateTimeHeaderFragment dateTimeHeader;

    protected int position=-1;


    public BaseCreateLogFragment(String parseClassName) {
        this.parseClassName = parseClassName;
    }

    public abstract void createOrEdit();

    public void delete(String id) {
        Log.d(TAG, "delete btn clicked");
        ParseQuery<ParseObject> query = ParseQuery.getQuery(parseClassName);
        query.fromLocalDatastore();
        query.getInBackground(id, new GetCallback<ParseObject>() {

                    @Override
                    public void done(ParseObject object, ParseException e) {
                        Log.d(TAG, "done finding growth item to delete");
                        object.deleteInBackground(new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
//                                AppUtils.invalidateDiaperChangeCaches(context);
                                scopedBus.post(new ItemCreatedOrChanged(parseClassName));
                            }
                        });

                    }
                }
        );

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // half second between each showcase view


        dateTimeHeader = (DateTimeHeaderFragment)(getChildFragmentManager().findFragmentById(R.id.header));

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(getActivity(), AppConstants.SHOWCASE_ID_DATE_TIME);

        sequence.setConfig(config);

        sequence.addSequenceItem(dateTimeHeader.getView().findViewById(R.id.currentDate),
                "Change Date Here ", "NEXT");

        sequence.addSequenceItem(dateTimeHeader.getView().findViewById(R.id.currentTime),
                "Change Time Here ", "GOT IT");

        sequence.start();

    }

    public void delete(ParseObject parseObject){

//        AppUtils.invalidateParseCache(parseClassName, context);
        parseObject.deleteEventually(new DeleteCallback() {
            @Override
            public void done(ParseException e) {

            }
        });
        scopedBus.post(new ItemDeleted(parseClassName, position));

    }
}
