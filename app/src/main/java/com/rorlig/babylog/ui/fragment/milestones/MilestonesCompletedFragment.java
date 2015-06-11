package com.rorlig.babylog.ui.fragment.milestones;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.rorlig.babylog.R;
import com.rorlig.babylog.otto.events.other.AddItemEvent;
import com.rorlig.babylog.otto.events.other.AddItemTypes;
import com.rorlig.babylog.ui.fragment.InjectableDialogFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by rorlig on 7/16/14.
 */
public class MilestonesCompletedFragment extends InjectableDialogFragment {
//    @InjectView(R.id.nursing)
//    ImageView nursingImageView;
//
//    @InjectView(R.id.bottle)
//    ImageView bottleImageView;

    public MilestonesCompletedFragment() {
        // Empty constructor required for DialogFragment
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
}
