package com.bananaplan.workflowandroid.utility;

import android.content.Context;

import com.bananaplan.workflowandroid.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Ben on 2015/8/8.
 */
public class BarChartData {
    public static int[] sColorId;

    static {
        sColorId = new int[3];
        sColorId[0] = R.color.blue1;
        sColorId[1] = R.color.orange;
        sColorId[2] = R.color.red;
    }

    private int[][] mData;
    private String[] mDate;
    public String from;

    public BarChartData(String from) {
        this.from = from;
    }

    public int[][] getData() {
        return mData;
    }

    public String[] getDates() {
        return mDate;
    }

    public void genRandomData(final Context context, int dataCount) {
        mData = new int[dataCount][7];
        for (int i = 0; i < dataCount; i++) {
            for (int j = 0; j < 7; j++) {
                mData[i][j] = (int) (Math.random() * 24 + 1);
            }
        }
        mDate = new String[7];
        Date date = getRandomDate();
        String[] axis_x_string = context.getResources().getStringArray(R.array.week);
        for (int i = 0; i < mDate.length; i++) {
            long timestamp = date.getTime() + i * 86400 * 1000;
            mDate[i] = Utils.timestamp2Date(new Date(timestamp), Utils.DATE_FORMAT_YMD) + " (" + axis_x_string[i] + ")";
        }
    }

    private Date getRandomDate() {
        int year = randBetween(2015, 2015);
        int month = randBetween(0, 11);
        GregorianCalendar gc = new GregorianCalendar(year, month, 1);
        int day = randBetween(1, gc.getActualMaximum(gc.DAY_OF_MONTH));
        gc.set(year, month, day);
        if (gc.get(Calendar.DAY_OF_WEEK) == 2) {
            return gc.getTime();
        } else {
            return getRandomDate();
        }
    }

    private int randBetween(int start, int end) {
        return start + (int) Math.round(Math.random() * (end - start));
    }

    public int getWorkingHours() {
        if (mData == null || mData.length < 1) return 0;
        int total = 0;
        for (int i = 0; i < 7; i++) {
            total += mData[0][i];
        }
        return total;
    }

    public int getOvertimeHours() {
        if (mData == null || mData.length < 2) return 0;
        int total = 0;
        for (int i = 0; i < 7; i++) {
            total += mData[1][i];
        }
        return total;
    }

    public int getIdleHours() {
        if (mData == null || mData.length < 3) return 0;
        int total = 0;
        for (int i = 0; i < 7; i++) {
            total += mData[2][i];
        }
        return total;
    }
}
