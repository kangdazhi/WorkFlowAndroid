package com.bananaplan.workflowandroid.data.loading.loadingworkerattendance;

import android.content.Context;
import android.os.AsyncTask;

import com.bananaplan.workflowandroid.data.loading.RestfulUtils;
import com.bananaplan.workflowandroid.data.loading.loadingactivities.ILoadingActivitiesStrategy;
import com.bananaplan.workflowandroid.data.loading.loadingworkerattendance.ILoadingWorkerAttendanceStrategy;
import com.bananaplan.workflowandroid.main.MainApplication;

import org.json.JSONArray;

/**
 * Created by daz on 10/10/15.
 */
public class LoadingWorkerAttendanceAsyncTask extends AsyncTask<Void, Void, Void> {

    public interface OnFinishLoadingDataListener {
        void onFinishLoadingData();
        void onFailLoadingData(boolean isFailCausedByInternet);
    }

    private ILoadingWorkerAttendanceStrategy mLoadingWorkerAttendanceStrategy;
    private OnFinishLoadingDataListener mOnFinishLoadingDataListener;

    private Context mContext;
    private JSONArray mResult = new JSONArray();

    public LoadingWorkerAttendanceAsyncTask(Context context,
                                            ILoadingWorkerAttendanceStrategy loadingWorkerAttendanceStrategy,
                                            OnFinishLoadingDataListener listener) {
        mContext = context;
        mOnFinishLoadingDataListener = listener;
        mLoadingWorkerAttendanceStrategy = loadingWorkerAttendanceStrategy;
    }


    public JSONArray getResult () {
        return mResult;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if (!MainApplication.sUseTestData) {
            if (RestfulUtils.isConnectToInternet(mContext)) {
                mLoadingWorkerAttendanceStrategy.load();
            } else {
                cancel(true);
            }
        }

        return null;
    }

    @Override
    protected void onCancelled(Void aVoid) {
        super.onCancelled(aVoid);
        mOnFinishLoadingDataListener.onFailLoadingData(true);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mOnFinishLoadingDataListener.onFinishLoadingData();
    }
}
