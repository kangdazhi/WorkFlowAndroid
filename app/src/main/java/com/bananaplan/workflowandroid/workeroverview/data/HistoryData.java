package com.bananaplan.workflowandroid.workeroverview.data;

/**
 * Created by Ben on 2015/8/29.
 */
public class HistoryData extends BaseData {
    public enum STATUS {
        WORK, OFF_WORK
    }
    public STATUS status;

    public HistoryData(BaseData.TYPE type) {
        super(type);
    }
}
