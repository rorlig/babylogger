package com.rorlig.babylog.ui.fragment.feed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.andexert.library.RippleView;
import com.rorlig.babylog.R;
import com.rorlig.babylog.otto.events.other.AddItemEvent;
import com.rorlig.babylog.otto.events.other.AddItemTypes;
import com.rorlig.babylog.ui.fragment.InjectableDialogFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by rorlig on 7/16/14.
 */
public class FeedSelectFragment extends InjectableDialogFragment {
    @InjectView(R.id.nursing)
    ImageView nursingImageView;

    @InjectView(R.id.bottle)
    ImageView bottleImageView;

    @InjectView(R.id.bottle_ripple)
    RippleView bottleRippleView;

    @InjectView(R.id.nursing_ripple)
    RippleView nursingRippleView;

    public FeedSelectFragment() {
        // Empty constructor required for DialogFragment
//        nursingRippleView.onAn
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        getDialog().setTitle(getString(R.string.choose_feed_type));
        View view = inflater.inflate(R.layout.fragment_dialog_feed_select, container);

        nursingImageView = (ImageView) view.findViewById(R.id.nursing);
        bottleImageView = (ImageView) view.findViewById(R.id.bottle);

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

    @OnClick(R.id.bottle_ripple)
    public void onBottleFeedClicked(){
        getDialog().dismiss();
        scopedBus.post(new AddItemEvent(AddItemTypes.FEED_BOTTLE));
    }


    @OnClick(R.id.nursing_ripple)
    public void onNursingClicked(){
        getDialog().dismiss();
        scopedBus.post(new AddItemEvent(AddItemTypes.FEED_NURSING));
    }
}
