package com.volkovmedia.perfo.welloalarm.activities.mvp.implementation.alarms;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.volkovmedia.perfo.welloalarm.R;
import com.volkovmedia.perfo.welloalarm.activities.mvp.implementation.settings.SettingsActivity;
import com.volkovmedia.perfo.welloalarm.activities.wraps.WelloToolbarActivity;
import com.volkovmedia.perfo.welloalarm.general.UniqueList;
import com.volkovmedia.perfo.welloalarm.general.WelloApplication;
import com.volkovmedia.perfo.welloalarm.objects.Alarm;
import com.volkovmedia.perfo.welloalarm.views.adapters.AlarmsAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlarmsActivity extends WelloToolbarActivity implements AlarmsContract.View {

    private static final int RQ_NEW_ALARM = 101, RQ_EDIT_ALARM = 102;

    private AlarmsPresenter mPresenter;

    private boolean isLoadedWithResult;

    @BindView(R.id.act_main_recyclerview)
    RecyclerView mAlarmsRecyclerView;

    @BindView(R.id.act_main_noalarms)
    View mNoAlarmsLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarms);

        ButterKnife.bind(this);

        mPresenter = WelloApplication.getComponent().getAlarmsPresenter();
        mPresenter.attachView(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> startSettingsActivity(null));

        mAlarmsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAlarmsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAlarmsRecyclerView.setAdapter(new AlarmsAdapter(new AlarmsAdapter.Callback() {
            @Override
            public void deleteAlarm(Alarm alarm) {
                mPresenter.deleteAlarm(alarm);
            }

            @Override
            public void editAlarm(Alarm alarm) {
                startSettingsActivity(alarm);
            }

            @Override
            public void switchAlarm(Alarm alarm) {
                mPresenter.editAlarm(alarm, false);
            }
        }));

        mPresenter.viewIsReady();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isLoadedWithResult) {
            mPresenter.onInterfaceUpdateAsked();
        }

        isLoadedWithResult = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        isLoadedWithResult = true;

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RQ_NEW_ALARM:
                    mPresenter.addAlarm(data.getParcelableExtra(Alarm.KEY_ALARM));
                    break;
                case RQ_EDIT_ALARM:
                    mPresenter.editAlarm(data.getParcelableExtra(Alarm.KEY_ALARM), true);
                    break;
            }
        }
    }

    private void startSettingsActivity(Alarm alarm) {
        int requestCode;
        Intent intent = new Intent(AlarmsActivity.this, SettingsActivity.class);

        if (alarm != null) {
            requestCode = RQ_EDIT_ALARM;
            intent.putExtra(Alarm.KEY_ALARM, alarm);
        } else {
            requestCode = RQ_NEW_ALARM;
        }

        startActivityForResult(intent, requestCode);
    }

    private void switchActivityContentLayout(boolean showAlarms) {
        int visibilityPlus = showAlarms ? View.VISIBLE : View.GONE,
                visibilityMinus = showAlarms ? View.GONE : View.VISIBLE;

        mAlarmsRecyclerView.setVisibility(visibilityPlus);
        mNoAlarmsLayout.setVisibility(visibilityMinus);
    }

    @Override
    public void showNoAlarmsLayout() {
        switchActivityContentLayout(false);
    }

    @Override
    public void showAlarms(UniqueList<Alarm> alarms) {
        AlarmsAdapter adapter = (AlarmsAdapter) mAlarmsRecyclerView.getAdapter();
        adapter.setDataSource(alarms);
        switchActivityContentLayout(true);
    }

    @Override
    public void onAlarmEdited(Alarm alarm, int position) {
        mAlarmsRecyclerView.getAdapter().notifyItemChanged(position);
        mAlarmsRecyclerView.scrollToPosition(position);
        Snackbar.make(mAlarmsRecyclerView, "Alarm " + alarm.getName() + " has been edited", Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void onAlarmDeleted(Alarm alarm, int position) {
        mAlarmsRecyclerView.getAdapter().notifyItemRemoved(position);
        Snackbar.make(mAlarmsRecyclerView, "Alarm " + alarm.getName() + " has been deleted", Snackbar.LENGTH_LONG)
                .setAction(R.string.cancel, view -> mPresenter.addAlarm(alarm))
                .show();
    }

    @Override
    public boolean isNoAlarmsViewVisible() {
        return mNoAlarmsLayout.getVisibility() == View.VISIBLE;
    }

    @Override
    public void showAlarmsViews() {
        switchActivityContentLayout(true);
    }

    @Override
    public void onAlarmAdded(Alarm alarm, int position) {
        mAlarmsRecyclerView.getAdapter().notifyItemInserted(position);
        mAlarmsRecyclerView.scrollToPosition(position);
        Snackbar.make(mAlarmsRecyclerView, "Alarm " + alarm.getName() + " has been added", Snackbar.LENGTH_LONG)
                .setAction(R.string.cancel, view -> mPresenter.deleteAlarm(alarm))
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}