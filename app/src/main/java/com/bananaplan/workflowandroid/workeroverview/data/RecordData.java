package com.bananaplan.workflowandroid.workeroverview.data;

/**
 * Created by Ben on 2015/8/29.
 */
public class RecordData extends BaseData {
    public long reporter;
    public String description;

    public RecordData(BaseData.TYPE type) {
        super(type);
    }
}
