package com.rorlig.babylog.ui.fragment.sleep;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.rorlig.babylog.dao.DiaperChangeDao;
import com.rorlig.babylog.dao.SleepDao;
import com.rorlig.babylog.db.BabyLoggerORMUtils;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by rorlig on 8/24/14.
 */
public class SleepLoader extends AsyncTaskLoader<List<SleepDao>> {

    private final String TAG = "SleepLoader";
    private final Context context;
    private int limit=20;
    private int offset=0;

    // We hold a reference to the Loader’s data here.
    private List<SleepDao> mData;

    public SleepLoader(Context ctx) {
        // Loaders may be used across multiple Activitys (assuming they aren't
        // bound to the LoaderManager), so NEVER hold a reference to the context
        // directly. Doing so will cause you to leak an entire Activity's context.
        // The superclass constructor will store a reference to the Application
        // Context instead, and can be retrieved with a call to getContext().

        super(ctx);

        Log.d(TAG, "SleepLoader Constructor");
        this.context = ctx;
    }


    public SleepLoader(Context ctx, int limit, int offset) {
        // Loaders may be used across multiple Activitys (assuming they aren't
        // bound to the LoaderManager), so NEVER hold a reference to the context
        // directly. Doing so will cause you to leak an entire Activity's context.
        // The superclass constructor will store a reference to the Application
        // Context instead, and can be retrieved with a call to getContext().

        super(ctx);

        Log.d(TAG, "SleepLoader Constructor");
        this.context = ctx;
        this.limit = limit;
        this.offset = offset;
    }


    /****************************************************/
    /** (1) A task that performs the asynchronous load **/
    /****************************************************/


    @Override
    public List<SleepDao> loadInBackground() {

        Log.d(TAG, " sleepLoader : loadInBackground");

        try {
            return new BabyLoggerORMUtils(context).getSleepList();
        } catch (SQLException e) {
            Log.d(TAG, "sql exception " + e);
            e.printStackTrace();
        }
        return null;
    }

    /********************************************************/
    /** (2) Deliver the results to the registered listener **/
    /********************************************************/

    @Override
    public void deliverResult(List<SleepDao> data) {
        if (isReset()) {
            // The Loader has been reset; ignore the result and invalidate the data.
            releaseResources(data);
            return;
        }

        // Hold a reference to the old data so it doesn't get garbage collected.
        // We must protect it until the new data has been delivered.
        List<SleepDao> oldData = mData;
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

//        // Begin monitoring the underlying data source.
//        if (mObserver == null) {
//            mObserver = new ContactsObserver();
//            // TODO: register the observer
//        }

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

//        // The Loader is being reset, so we should stop monitoring for changes.
//        if (mObserver != null) {
//            // TODO: unregister the observer
//            mObserver = null;
//        }
    }

    @Override
    public void onCanceled(List<SleepDao> data) {
        // Attempt to cancel the current asynchronous load.
        super.onCanceled(data);

        // The load has been canceled, so we should release the resources
        // associated with 'data'.
        releaseResources(data);
    }

    private void releaseResources(List<SleepDao> data) {
        // For a simple List, there is nothing to do. For something like a Cursor, we
        // would close it in this method. All resources associated with the Loader
        // should be released here.
    }


}
