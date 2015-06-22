package com.rorlig.babylog.ui.fragment.milestones;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;

import com.gc.materialdesign.views.Button;
import com.rorlig.babylog.R;
import com.rorlig.babylog.otto.MilestoneCancelEvent;
import com.rorlig.babylog.otto.MilestoneResetEvent;
import com.rorlig.babylog.otto.MilestoneSaveEvent;
import com.rorlig.babylog.ui.fragment.InjectableDialogFragment;

import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by gaurav gupta
 */
public class MilestonesCompletedFragment extends InjectableDialogFragment {


    private int position = 0;
    @InjectView(R.id.btn_save)
    Button saveBtn;

    @InjectView(R.id.btn_cancel)
    Button cancelBtn;

    @InjectView(R.id.btn_reset)
    Button resetBtn;

    @InjectView(R.id.datepicker_milestone)
    DatePicker datePicker;

    private String TAG = "MilestonesCompletedFragment";


    public MilestonesCompletedFragment() {
        // Empty constructor required for DialogFragment
    }


    static MilestonesCompletedFragment newInstance(int position) {
        MilestonesCompletedFragment f = new MilestonesCompletedFragment();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("position", position);
        f.setArguments(args);
        return f;
    }


    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);
        Log.d(TAG, "arguments " + getArguments());

        position = getArguments().getInt("position");

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        getDialog().setTitle("FEEDING METHOD");
        View view = inflater.inflate(R.layout.custom_dialog_date_picker, container);


//        nursingImageView = (ImageView) view.findViewById(R.id.nursing);
//        bottleImageView = (ImageView) view.findViewById(R.id.bottle);
//
//        nursingImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getDialog().dismiss();
//                scopedBus.post(new AddItemEvent(AddItemTypes.FEED_NURSING));
//            }
//        });
//
//        bottleImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getDialog().dismiss();
//                scopedBus.post(new AddItemEvent(AddItemTypes.FEED_BOTTLE));
//
//            }
//        });

        ButterKnife.inject(this, view);
        return view;
    }


    @OnClick(R.id.btn_save)
    public void saveBtnClicked() {
        Log.d(TAG, "save btn clicked position: " + position);
        Log.d(TAG, "date " + datePicker.getYear() + " month " + datePicker.getMonth() + " day " + datePicker.getDayOfMonth());
        scopedBus.post(new MilestoneSaveEvent(position, datePicker.getYear(), datePicker.getMonth()+1, datePicker.getDayOfMonth()));

        dismiss();

    }

    @OnClick(R.id.btn_reset)
    public void resetBtnClicked() {
        Log.d(TAG, "reset btn clicked");
        Log.d(TAG, "date " + datePicker.getYear() + " month " + datePicker.getMonth() + " day " + datePicker.getDayOfMonth());
        scopedBus.post(new MilestoneResetEvent(position, datePicker.getYear(), datePicker.getMonth()+1, datePicker.getDayOfMonth()));
        dismiss();

    }

    @OnClick(R.id.btn_cancel)
    public void cancelBtnClicked() {
        Log.d(TAG, "cancel btn clicked");
        Log.d(TAG, "date " + datePicker.getYear() + " month " + datePicker.getMonth() + " day " + datePicker.getDayOfMonth());
        scopedBus.post(new MilestoneCancelEvent(position, datePicker.getYear(), datePicker.getMonth()+1, datePicker.getDayOfMonth()));
        dismiss();
    }


//    /*
//   * Register to events...
//   */
//    @Override
//    public void onStart(){
//
//
//        super.onStart();
//        Log.d(TAG, "onStart");
//        scopedBus.register(eventListener);
//        getLoaderManager().restartLoader(LOADER_ID, null, this);
//
//    }
//
//    /*
//     * Unregister from events ...
//     */
//    @Override
//    public void onStop(){
//        super.onStop();
//        Log.d(TAG, "onStop");
//        scopedBus.unregister(eventListener);
//
//    }





}
