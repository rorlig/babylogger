package com.rorlig.babylog.db;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.rorlig.babylog.model.ContactItem;

import java.util.Comparator;
import java.util.List;

/**
 * @author Gaurav Gupta
 * Loads the contacts and arranges them by frequent and all
 * Reference - https://github.com/alexjlockwood/AppListLoader -- not used
 */
public class ContactsLoader extends AsyncTaskLoader<List<ContactItem>> {

    // We hold a reference to the Loader’s data here.
    private List<ContactItem> mData;


    private Context context;
    private String TAG= "ContactsLoader";
    private int limit = 500;
    private int offset = 0;

    public ContactsLoader(Context ctx) {
        // Loaders may be used across multiple Activitys (assuming they aren't
        // bound to the LoaderManager), so NEVER hold a reference to the context
        // directly. Doing so will cause you to leak an entire Activity's context.
        // The superclass constructor will store a reference to the Application
        // Context instead, and can be retrieved with a call to getContext().

        super(ctx);

        Log.d(TAG, "ContactsLoader Constructor");
        this.context = ctx;
    }


    public ContactsLoader(Context ctx, int limit, int offset) {
        // Loaders may be used across multiple Activitys (assuming they aren't
        // bound to the LoaderManager), so NEVER hold a reference to the context
        // directly. Doing so will cause you to leak an entire Activity's context.
        // The superclass constructor will store a reference to the Application
        // Context instead, and can be retrieved with a call to getContext().

        super(ctx);

        Log.d(TAG, "ContactsLoader Constructor");
        this.context = ctx;
        this.limit = limit;
        this.offset = offset;
    }

    /****************************************************/
    /** (1) A task that performs the asynchronous load **/
    /****************************************************/

    @Override
    public List<ContactItem> loadInBackground() {

        return ContactsQueryHelper.contactWithEmail(context);
//        cursor.moveToFirst();
//        while(cursor.moveToNext()) {
//            ContactItem contactItem = new ContactEntry(cursor.get)
//        }

//        if (cursor!=null && cursor.getCount()>0) {
//            int count = cursor.getCount();
//
//            Log.d(TAG, " count of the email " + cursor.getCount());
//            cursor.moveToFirst();
//
//            while(cursor.moveToNext()) {
//
//            }
//        }
//        List<ContactItem> callLogEntries = new ArrayList<ContactItem>();
//        List<ContactItem> rawCallLogEntries = new ArrayList<ContactItem>();
////        String recentContacts = mContext.getString(R.string.recent_contacts);
////        callLogEntries.add(new ContactSectionDivider(recentContacts));
//
//        List<ContactItem> entries = null;
//        OfflineQueryParams callLogParams = new OfflineQueryParams();
//
//        callLogParams.pageSize=800;
//        int uiSize =20;
//        rawCallLogEntries  = Phrase2ContentUtils.getCallLogsByParams(context, callLogParams, uiSize);
////        Map<String , Integer> contactMap =summaryResult.getContactMap();
//        ContactSummaryResult contactSummary = Phrase2ContentUtils.getContactSummaryByParms(context, callLogParams);
//        entries = contactSummary.getResultList();
//        Map<String,String> numberToEntries =contactSummary.getContactMap();
//        /**
//         *
//         */
//        for(ContactItem item : rawCallLogEntries)
//        {
//            ContactEntry entry =(ContactEntry)item;
//            String number = entry.getFirstNumber();
//            /**
//             * If this call log has a contact entry
//             */
//            if(numberToEntries.containsKey(number))
//                entry.setEntryId(numberToEntries.get(number));
//            /**
//             * Else set the telnumber with minus one as entry id
//             */
//            else
//            {
//                entry.setEntryId("-a"+number);
//
//            }
//        }
//        List<String> favoriteList =FavoriteContact.getFavorite(rawCallLogEntries);
//        String favorites = context.getString(R.string.favorites);
//        callLogEntries.add(new ContactSectionDivider(favorites));
//        if(favoriteList!=null)
//        {
//            for(String entryId :favoriteList)
//            {
//                boolean hasEntry =false;
//                for(ContactItem item : entries)
//                {
//                    if(((ContactEntry)item).getEntryId().equals(entryId))
//                    {
//                        ContactEntry cItem =(ContactEntry)item;
//                        // ContactEntry(String entryId, String name, List<String> numbers, String photoUri,  int type) {
//                        callLogEntries.add(new ContactEntry(cItem.getEntryId(),cItem.getName(),cItem.getNumberList(),cItem.getPictureUri(),ContactEntry.TYPE_RECENT_CONTACT));
//                        hasEntry =true;
//                        break;
//                    }
//                }
//                if(!hasEntry)
//                    if(entryId.startsWith("-a"))
//                    {
//                        String number =entryId.substring(2);
////                        ContactEntry(String entryId, String name, String number, String photoUri, String phoneLabel, int type)
//                        callLogEntries.add(new ContactEntry(entryId,"",number,null,"",ContactEntry.TYPE_RECENT_CONTACT));
//
//                    }
//            }
//        }
//        String curPhoneLabel ="-1";
//        if(( entries!=null) &&(entries.size()>0))
//        {
//            int size = entries.size();
//            String allContacts = context.getString(R.string.all_contacts);
//            callLogEntries.add(new ContactSectionDivider(allContacts));
//
//            for(int i =0;i<size;i++)
//            {
//                ((ContactEntry)entries.get(i)).setType(ContactEntry.TYPE_CONTACT);
//                String phoneLabel ="";
//                if(((ContactEntry)entries.get(i)).getName().length()>0)
//                    phoneLabel= ((ContactEntry)entries.get(i)).getName().substring(0,1);
//                if(!curPhoneLabel.equals(phoneLabel))
//                {
//                    if(phoneLabel==null)
//                        phoneLabel ="";
//                    callLogEntries.add(new ContactSectionDivider(phoneLabel));
//                    curPhoneLabel =phoneLabel;
//                }
//
//                callLogEntries.add(entries.get(i));
//
//            }
//        }
//        return callLogEntries;
//        // This method is called on a background thread and should generate a
//        // new set of data to be delivered back to the client.
//        List<ContactItem> data = new ArrayList<ContactItem>();
//
//
//        String[] PROJECTION = new String[] { "date","duration","lookup_uri","number","name","photo_id" };
//
////        String orderBy ="  date desc "+addPaginationClause(params);
////        String whereClause =buildCallLogWhereClause(params);
//        String orderBy ="  date desc "+ "limit " + offset + ", " + limit;
//
//        Cursor mmsCur = context.getContentResolver().query(
//                              CallLog.Calls.CONTENT_URI, PROJECTION, null, null,
//                              orderBy);
//
//        Log.d(TAG, "count " + mmsCur.getCount());
//
//        mmsCur.move(-1);
//
//        Map<String , Integer> contactMap = new HashMap<String,Integer>();
//
//        List<ContactItem> recentContactEntries = new ArrayList<ContactItem>();
//
//
//
//
////        String whereClause = buildCallLogWhereClause(params);
//        String allContact ="";
//        int jj =0;
//        while  (mmsCur.moveToNext()){
//            jj++;
//          //  Log.d(TAG, "mmCur looped");
//            long lastCalled = mmsCur.getLong(mmsCur.getColumnIndex(PROJECTION[0]));
//            String photoUri =  mmsCur.getString(mmsCur.getColumnIndex(PROJECTION[2]));
//            String number = mmsCur.getString(mmsCur.getColumnIndex(PROJECTION[3]));
//            String name = mmsCur.getString(mmsCur.getColumnIndex(PROJECTION[4]));
//            /**
//             * Suspect there's extra space in name from Android call log
//             */
//            if(name!=null)
//                name = name.trim();
//            if((name!=null)&&(!name.equals("")))
//                allContact =allContact+"\""+name+"\",";
//            else
//                allContact=allContact+"\""+number+"\",";
//            ContactEntry contactEntry = new ContactEntry(
//                                                        name, number, 1,
//                                                        lastCalled, 0, photoUri,
//                                                        ContactEntry.TYPE_RECENT_CONTACT);
//
//            if (!recentContactEntries.contains(contactEntry)){
//                recentContactEntries.add(contactEntry);
//            } else {
//                 for (ContactItem entry: recentContactEntries){
//                        if (entry.equals(contactEntry)){
//                            ((ContactEntry)entry).incrementCalls();
//                        }
//                 }
//            }
//            int newValue = (jj) %20;
//            if(newValue==0 )
//            {
//                Log.d("aa",allContact);
//                allContact ="";
//            }
//
//        }
//        Log.d("aa",allContact);
//
//        for(ContactItem citem : recentContactEntries)
//        {
//            ContactEntry item =(ContactEntry)citem;
//
//        }
//
//        Collections.sort(recentContactEntries, mostCalls);
//
//        //add the sectional divider at the top ..
//        recentContactEntries.add(0, new ContactSectionDivider("Frequent Contacts"));
//
//
//
//        recentContactEntries.addAll(getAllContacts());
//
//
////       recentContactEntries.addAll(allContactEntries);
//
//
//
//        return recentContactEntries;
    }


    /********************************************************/
    /** (2) Deliver the results to the registered listener **/
    /********************************************************/

    @Override
    public void deliverResult(List<ContactItem> data) {
        if (isReset()) {
            // The Loader has been reset; ignore the result and invalidate the data.
            releaseResources(data);
            return;
        }

        // Hold a reference to the old data so it doesn't get garbage collected.
        // We must protect it until the new data has been delivered.
        List<ContactItem> oldData = mData;
        mData = data;

        if (isStarted()) {
            // If the Loader is in a started state, deliver the results to the
            // client. The superclass method does this for us.
            super.deliverResult(data);
        }

        // Invalidate the old data as we don't need it any more.
        if (oldData != null && oldData != data) {
            releaseResources(oldData);
        }
    }

    /*********************************************************/
    /** (3) Implement the Loader’s state-dependent behavior **/
    /*********************************************************/

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            // Deliver any previously loaded data immediately.
            deliverResult(mData);
        }

        // Begin monitoring the underlying data source.
        if (mObserver == null) {
            mObserver = new ContactsObserver();
            // TODO: register the observer
        }

        if (takeContentChanged() || mData == null) {
            // When the observer detects a change, it should call onContentChanged()
            // on the Loader, which will cause the next call to takeContentChanged()
            // to return true. If this is ever the case (or if the current data is
            // null), we force a new load.
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        // The Loader is in a stopped state, so we should attempt to cancel the
        // current load (if there is one).
        cancelLoad();

        // Note that we leave the observer as is. Loaders in a stopped state
        // should still monitor the data source for changes so that the Loader
        // will know to force a new load if it is ever started again.
    }

    @Override
    protected void onReset() {
        // Ensure the loader has been stopped.
        onStopLoading();

        // At this point we can release the resources associated with 'mData'.
        if (mData != null) {
            releaseResources(mData);
            mData = null;
        }

        // The Loader is being reset, so we should stop monitoring for changes.
        if (mObserver != null) {
            // TODO: unregister the observer
            mObserver = null;
        }
    }

    @Override
    public void onCanceled(List<ContactItem> data) {
        // Attempt to cancel the current asynchronous load.
        super.onCanceled(data);

        // The load has been canceled, so we should release the resources
        // associated with 'data'.
        releaseResources(data);
    }

    private void releaseResources(List<ContactItem> data) {
        // For a simple List, there is nothing to do. For something like a Cursor, we
        // would close it in this method. All resources associated with the Loader
        // should be released here.
    }

    /*********************************************************************/
    /** (4) Observer which receives notifications when the data changes **/
    /*********************************************************************/

    // NOTE: Implementing an observer is outside the scope of this post (this example
    // uses a made-up "ContactsObserver" to illustrate when/where the observer should
    // be initialized).

    // The observer could be anything so long as it is able to detect content changes
    // and report them to the loader with a call to onContentChanged(). For example,
    // if you were writing a Loader which loads a list of all installed applications
    // on the device, the observer could be a BroadcastReceiver that listens for the
    // ACTION_PACKAGE_ADDED intent, and calls onContentChanged() on the particular
    // Loader whenever the receiver detects that a new application has been installed.
    // Please don’t hesitate to leave a comment if you still find this confusing! :)
    private ContactsObserver mObserver;


    private static final Comparator<ContactItem> mostCalls = new Comparator<ContactItem>() {
        @Override
        public int compare(ContactItem lhs, ContactItem rhs) {
            return 1;
//            if(((ContactEntry2)lhs).getCalls() > ((ContactEntry2)rhs).getCalls())
//                return -1;
//            else if(((ContactEntry)lhs).getCalls() < ((ContactEntry)rhs).getCalls())
//                return 1;
//            else
//                return 0;
        }
    };

    //todo implement the builder pattern for ContactsLoader...
    public static class ContactBuilder {
    }
}
