package com.bananaplan.workflowandroid.data.worker.status;

/**
 * Created by Ben on 2015/8/29.
 */
public class HistoryData extends BaseData {
    public enum STATUS {
        WORK, OFF_WORK
    }
    public STATUS status;

    public String description = "";
    public HistoryData(BaseData.TYPE type) {
        super(type);
    }
}
