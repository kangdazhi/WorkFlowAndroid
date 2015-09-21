package com.bananaplan.workflowandroid.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.bananaplan.workflowandroid.R;
import com.bananaplan.workflowandroid.data.Worker;
import com.bananaplan.workflowandroid.data.WorkingData;
import com.bananaplan.workflowandroid.main.MainActivity;
import com.bananaplan.workflowandroid.overview.TaskItemFragment;
import com.bananaplan.workflowandroid.overview.workeroverview.AttendanceStatusFragment;
import com.bananaplan.workflowandroid.overview.workeroverview.StatusFragment;
import com.bananaplan.workflowandroid.utility.TabManager;

public class DetailedWorkerActivity extends AppCompatActivity {

    private static final String TAG = "DetailWorkerActivity";

    public static final String EXTRA_WORKER_ID = "extra_worker_id";

    private static final class FragmentTag {
        public static final String TASK_SCHEDULE = "task_schedule";
        public static final String TASK_ITEM = "task_item";
        public static final String TASK_LOG = " task_log";
        public static final String WORKER_LOG = "worker_log";
    }

    private ActionBar mActionBar;
    private FragmentManager mFragmentManager;
    private TabHost mTabHost;

    private ImageView mWorkerAvatar;
    private TextView mWorkerName;
    private TextView mWorkerJobtitle;

    private Worker mWorker;
    private TabManager mTabMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_worker);
        initialize(getIntent());
    }

    private void initialize(Intent intent) {
        mWorker = WorkingData.getInstance(this).getWorkerItemById(intent.getStringExtra(EXTRA_WORKER_ID));
        mFragmentManager = getSupportFragmentManager();
        findViews();
        setupActionBar();
        setupTabs();
        setupViews();
    }

    private void findViews() {
        mTabHost = (TabHost) findViewById(R.id.detailed_worker_tab_host);
        mWorkerAvatar = (ImageView) findViewById(R.id.detailed_worker_avatar);
        mWorkerName = (TextView) findViewById(R.id.detailed_worker_name);
        mWorkerJobtitle = (TextView) findViewById(R.id.detailed_worker_jobtitle);
        mTabMgr = new TabManager(this, null, mTabHost, android.R.id.tabcontent);
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_detailed_worker_toolbar);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();

        if (mActionBar != null) {
            mActionBar.setDisplayShowTitleEnabled(false);
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupTabs() {
        mTabHost.setup();
        addTab(FragmentTag.TASK_SCHEDULE, null, TaskScheduleFragment.class);
        Bundle bundle = new Bundle();
        bundle.putString(TaskItemFragment.FROM, getClass().getSimpleName());
        addTab(FragmentTag.TASK_ITEM, bundle, TaskItemFragment.class);
        addTab(FragmentTag.TASK_LOG, null, StatusFragment.class);
        addTab(FragmentTag.WORKER_LOG, null, StatusFragment.class);
    }

    private void addTab(String tabTag, Bundle bundle, Class<?> cls) {
        TabHost.TabSpec tabSpec = mTabHost.newTabSpec(tabTag)
                .setIndicator(getTabView(tabTag));
        mTabMgr.addTab(tabSpec, cls, bundle);
    }

    private View getTabView(String tabTag) {
        View view = getLayoutInflater().inflate(R.layout.tab, null);

        int titleResId;
        switch (tabTag) {
            case FragmentTag.TASK_SCHEDULE:
                titleResId = R.string.detailed_worker_task_schedule;
                break;
            case FragmentTag.TASK_ITEM:
                titleResId = R.string.detailed_worker_task_items;
                break;
            case FragmentTag.TASK_LOG:
                titleResId = R.string.detailed_worker_task_log;
                break;
            case FragmentTag.WORKER_LOG:
                titleResId = R.string.detailed_worker_worker_log;
                break;
            default:
                titleResId = -1;
                break;
        }

        String text = titleResId != -1 ? getResources().getString(titleResId) : "";
        ((TextView) view.findViewById(R.id.tab_title)).setText(text);

        return view;
    }

    private void setupViews() {
        mWorkerAvatar.setImageDrawable(mWorker.getAvator());
        mWorkerName.setText(mWorker.name);
        mWorkerJobtitle.setText(mWorker.jobTitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}