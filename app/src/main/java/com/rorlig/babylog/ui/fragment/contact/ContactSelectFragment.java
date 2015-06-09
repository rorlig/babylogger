package com.rorlig.babylog.ui.fragment.contact;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import com.rorlig.babylog.R;
import com.rorlig.babylog.model.ContactCategory;
import com.rorlig.babylog.ui.fragment.InjectableFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by rorlig on 7/31/14.
 */
public class ContactSelectFragment extends InjectableFragment {
    // Store instance variables
    private String title;
    private int page;

    @InjectView(R.id.btnSocialProvider)
    Button btnSocialProvider;

//    @Inject
//    MerchantProvider merchantProvider;

    private EventListener eventListener = new EventListener();
    private String TAG="CardsFragment";

    // newInstance constructor for creating fragment with arguments
    public static ContactSelectFragment newInstance(int page, ContactCategory title) {
        ContactSelectFragment fragmentFirst = new ContactSelectFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title.toString());
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }


    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
        Log.d(TAG,"onCreate: " + title);

//        Log.d(TAG, "title " + title);
//        merchantList = new ArrayList<Merchant>();
    }

    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);

        Log.d(TAG, "title " + getArguments().getString("someTitle"));

        btnSocialProvider.setText( "LOGIN TO " + getArguments().getString("someTitle"));

        if (title.equals(ContactCategory.FACEBOOK.toString())) {
            btnSocialProvider.setBackgroundColor(getResources().getColor(R.color.facebook_background));
        } else if (title.equals(ContactCategory.GOOGLEPLUS.toString())) {
            btnSocialProvider.setBackgroundColor(getResources().getColor(R.color.google_background));
        } else {
            btnSocialProvider.setBackgroundColor(getResources().getColor(R.color.linkedin_background));
        }

//        Log.d(TAG, "merchantProvider " + merchantProvider);
//        if (!isVisible) {
//            Log.d(TAG, "fetching data activity created");
//            fetchData();
//        }
//


//        scopedBus.post(new FragmentCreated("Profile"));

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_blank, container, false);
//        listView = (ListView) view.findViewById(R.id.list);

//        TextView tvLabel = (TextView) view.findViewB  yId(R.id.tvLabel);
//        tvLabel.setText(page + " -- " + title);
        ButterKnife.inject(this,view);
        return view;

    }


    @Override
    public void onStart(){
        Log.d(TAG, "onStart " + title);
        super.onStart();
        scopedBus.register(eventListener);
    }

    @Override
    public void onStop(){
        Log.d(TAG, "onStop");

        super.onStop();
        scopedBus.unregister(eventListener);
    }

    @Override
    public void setUserVisibleHint(final boolean visible) {
//        Log.d(TAG, " setUserVisibileHint " + visible + " title " + title);
//        super.setUserVisibleHint(visible);
//        if (visible&&getView()!=null){
//            Log.d(TAG, "fetching data");
//            isVisible = true ;
//            fetchData();
//        } else {
//            isVisible = false;
//        }
//        if (visible) {
////            merchantProvider.getMerchants(getActivity());
//
//        }
    }

//    private void fetchData(){
//        Log.d(TAG, "fetchData " + title);
//        merchantProvider = new FakeMerchantProvider();
//        merchantProvider.getMerchants(getActivity(), title );
////        Log.d(TAG, " title: " + title);
//
//
//    }

    private class EventListener {
        public EventListener() {

        }

//        @Subscribe
//        public void onMerchantsLoaded(MerchantsLoadedEvent event){
//            Log.d(TAG, "onMerchantsLoaded " + event.getMerchantArrayList().size() + " card # " + event.getMerchantCategory()
//            + " title " + title);
//
//            if (event.getMerchantCategory().equals(title)){
//                Log.d(TAG, " category and title match ");
//                merchantList.clear();
//                merchantList.addAll(event.getMerchantArrayList());
//                merchantAdapter = new MerchantAdapter(getActivity(), R.layout.list_item_merchant, merchantList);
//                listView.setAdapter(merchantAdapter);
////            Log.d(TAG, eventArrayList.toString());
//                merchantAdapter.update(merchantList);
//                listView.setAdapter(merchantAdapter);
//
//            }
//
//        }
    }

    @OnClick(R.id.btnSocialProvider)
    public void btnSocialProviderClicked() {

        Toast.makeText(getActivity(), "Not Implemented Yet", Toast.LENGTH_LONG).show();

    }


}