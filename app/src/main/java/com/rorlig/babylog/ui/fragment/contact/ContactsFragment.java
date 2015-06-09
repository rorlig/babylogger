package com.rorlig.babylog.ui.fragment.contact;

//import android.app.LoaderManager;
//import android.content.Loader;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import com.rorlig.babylog.R;
import com.rorlig.babylog.db.ContactsLoader;
import com.rorlig.babylog.model.ContactEntry2;
import com.rorlig.babylog.model.ContactItem;
import com.rorlig.babylog.otto.ContactSelectedEvent;
import com.rorlig.babylog.otto.ScopedBus;
import com.rorlig.babylog.otto.events.ui.FragmentCreated;
import com.rorlig.babylog.ui.adapter.ContactsAdapter;
import com.rorlig.babylog.ui.fragment.InjectableListFragment;
import com.rorlig.babylog.ui.widget.CustomEditText;
import com.rorlig.babylog.ui.widget.DrawableClickListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by elkintr on 4/25/2014.
 */
public class ContactsFragment extends InjectableListFragment implements TextWatcher, LoaderManager.LoaderCallbacks<List<ContactItem>> {
    private static final String TAG = "ContactsFragment";

    private ContactsAdapter adapter;
    private ListView listView;
    private int currentClickedIndex = -1;
    private int currentDetailsEntryIndex = 0;
    private DisplayMetrics metrics;
//    private ContactDetailsFragment detailsFragment;

    @Inject
    ScopedBus bus;
//    @Inject
//    ContactsClient contactsClient;

    private CustomEditText filterEditText;
    private int LOADER_ID = 1;
    private String fromAction;

//    AnalyticsReport analyticsReport = AnalyticsReport.getInstance();


    public ContactsFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(LOADER_ID, null, this);
        bus.post(new FragmentCreated("Select Contact"));

        if (getArguments()!=null) {
            Log.d(TAG,getArguments().getString("from"));
            fromAction = getArguments().getString("from");
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_addressbook_contacts, container, false);
        filterEditText = (CustomEditText)v.findViewById(R.id.filterEditText);
        listView = (ListView) v.findViewById(android.R.id.list);
        listView.setAdapter(adapter);

        filterEditText.addTextChangedListener(this);
        return v;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Configure adapter
        adapter = new ContactsAdapter(getActivity(), new ArrayList<ContactItem>());
        setListAdapter(adapter);

        metrics = getResources().getDisplayMetrics();
    }

//    private void initializeDetailFragment() {
//        ContactDetailsFragment oldFragment = detailsFragment;
//        detailsFragment = ContactDetailsFragment.newInstance();
//
//        if(oldFragment!=null)
//        {
//            getFragmentManager().beginTransaction()
//                    .replace(R.id.details_fragment, detailsFragment)
//                    .commit();
//        }
//        else
//        {
////            filterEditText.addTextChangedListener(this);
//            getFragmentManager().beginTransaction()
//                    .add(R.id.details_fragment, detailsFragment)
//                    .commit();
//
//        }
//    }

    @Override
    public void onPause() {
        super.onPause();
//        bus.unregister(adapter);
        bus.unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        bus.register(adapter);
        bus.register(this);
    }

//    @Subscribe
//    public void onSlidingPaneClosedEvent(SlidingPaneClosedEvent event) {
//        setHasOptionsMenu(false);
//    }
//
//    @Subscribe
//    public void onSlidingPaneOpenedEvent(SlidingPaneOpenedEvent event) {
//        setHasOptionsMenu(true);
//    }

    @Override
    public void onListItemClick (ListView l, View v, int position, long id) {
        Log.d(TAG, "onListItemClick " + v.getClass().getName() + " " + v.getMeasuredHeight() + " " + v.getParent().getClass().getName());

        ContactEntry2 item = (ContactEntry2) adapter.get(position);
        Log.d(TAG, "item " + item);
        scopedBus.post(new ContactSelectedEvent(item, fromAction));

//        if (item.getViewType()==ContactEntry2.TYPE_CONTACT){
//            AppConstants.incrementOfflineCallAnalytics(getActivity(), AppConstants.EXPAND_OTHER_CONTACT);
//
////            analyticsReport.trackEvent(AnalyticsStatEvent.StatsOfflineActionItems.EXPAND_OTHER_CONTACT, 1);
//        }

//        if (item.getViewType()==ContactEntry.TYPE_RECENT_CONTACT){
//            AppConstants.incrementOfflineCallAnalytics(getActivity(), AppConstants.EXPAND_RECENT_CONTACT);
//
////            analyticsReport.trackEvent(AnalyticsStatEvent.StatsOfflineActionItems.EXPAND_RECENT_CONTACT, 1);
//        }
        //If not clicked then we will click it
//        if(!item.isSelected()) {
//            //Unclick the previous clicked if possible
//            if(currentClickedIndex != -1) {
//                hideActionRow(currentClickedIndex);
//                ((ContactEntry)adapter.get(currentClickedIndex)).setSelected(false);
//                Log.d("CallLog", "Setting " + currentClickedIndex + " to not selected.");
//            }
//
//            //Set the current row to selected and then click it, update current clicked index
//            item.setSelected(true);
//            showActionRow(position);
//            currentClickedIndex = position;
//            listView.smoothScrollToPosition(position);
//
//            currentDetailsEntryIndex = position;
//            ContactEntry entry = (ContactEntry) adapter.get(position);
//            if(detailsFragment!=null)
//                detailsFragment.load(entry);
//            Log.d("CallLog", "Setting " + currentClickedIndex + " to selected.");
//        }
//        //User is double clicking so unclick the row
//        else {
//            hideActionRow(position);
//            item.setSelected(false);
//            currentClickedIndex = -1;
//            Log.d("CallLog", "Setting " + position + " to not selected.");
//        }
    }



//    private void updateDetailsFragmentIfNeeded(ContactEntry entry) {
//        ContactEntry currentEntry = null;
//        if(currentDetailsEntryIndex != -1) {
//            if(adapter.get(currentDetailsEntryIndex) instanceof  ContactEntry)
//                currentEntry = (ContactEntry) adapter.get(currentDetailsEntryIndex);
//        }
//
//        //A note was added to the currently displayed entry
//        if(currentDetailsEntryIndex == currentClickedIndex && currentDetailsEntryIndex != -1) {
//            detailsFragment.load(entry);
//        }
//        else if(currentEntry != null && currentEntry.equals(entry)) {
//            detailsFragment.load(entry);
//        }
//        else {
//            Log.d(TAG, "detailsI: " + currentDetailsEntryIndex + " clickedI: " + currentClickedIndex);
//        }
//    }


    //This method also exists in SelectionDialogFragment, maybe convert to utils?
//    private void performCall(String number) {
//        Intent i = new Intent(Intent.ACTION_CALL);
//        i.setData(Uri.parse("tel:" + number));
//        startActivity(i);
//    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        adapter.getFilter().filter(s);
        if (s!=null && s.length()>0) {
            Log.d(TAG, "s is not null and length > 0 ");

            filterEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_action_cancel), null);
            filterEditText.setDrawableClickListener(new DrawableClickListener() {
                @Override
                public void onClick(DrawablePosition target) {
                    switch (target){
                        case RIGHT:
                            Log.d(TAG, "X is clicked");
                            filterEditText.setText("");
                            break;
                    }
                }
            });

        } else {
            Log.d(TAG, "no text ");
            filterEditText.setDrawableClickListener(null);
            filterEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_action_search), null);

        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    /*
    * create the call log loader
    */
    @Override
    public Loader<List<ContactItem>> onCreateLoader(int id, Bundle args) {
//        String orderBy ="  date desc "+addPaginationClause(params);
//        String whereClause =buildCallLogWhereClause(params);
//        Cursor mmsCur = getActivity().getContentResolver().query(
//                              CallLog.Calls.CONTENT_URI, cArray, null, null,
//                              null);

        Log.d(TAG, "create Loader");

        return new ContactsLoader(getActivity());


    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<List<ContactItem>> listLoader, List<ContactItem> contactItems) {
        Log.d(TAG, "loader finished");

        Log.d(TAG, "recent contact entries received " + contactItems.size());

        adapter.update(contactItems);

    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<List<ContactItem>> listLoader) {

    }

//    @Override
//    public void onLoadFinished(Loader<List<ContactItem>> listLoader, List<ContactItem> recentContactEntries) {
//
//        Log.d(TAG, "loader finished");
//
//        Log.d(TAG, "recent contact entries received " + recentContactEntries.size());
//
//        adapter.update(recentContactEntries);
//
////        ContactDetailsFragment f =  (ContactDetailsFragment) getFragmentManager().findFragmentById(R.id.details_fragment);
//        /**
//         * FIXME: Need to double check if it's loading the right one
//         */
////        if((recentContactEntries.size()>2)&&(recentContactEntries.get(1) instanceof  ContactEntry))
////            f.setData((ContactEntry)recentContactEntries.get(1));
//
////        bus.post(new LoaderFinishedEvent());
//
//
////            filterEditText.addTextChangedListener(this);
//
//
////        listLoader.
//
////        if (recentContactEntries!=null) {
////            Log.d(TAG, "size " + recentContactEntries.size());
////        }
//
//    }

//    //todo handle this...
//    @Override
//    public void onLoaderReset(Loader<List<ContactItem>> loader) {
//
//    }
}