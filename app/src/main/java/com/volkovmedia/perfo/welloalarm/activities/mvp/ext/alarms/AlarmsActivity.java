package com.volkovmedia.perfo.welloalarm.activities.mvp.ext.alarms;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.volkovmedia.perfo.welloalarm.R;
import com.volkovmedia.perfo.welloalarm.activities.mvp.ext.settings.SettingsActivity;
import com.volkovmedia.perfo.welloalarm.activities.wraps.WelloToolbarActivity;
import com.volkovmedia.perfo.welloalarm.database.AlarmDatabaseHelper;
import com.volkovmedia.perfo.welloalarm.general.UniqueList;
import com.volkovmedia.perfo.welloalarm.objects.Alarm;
import com.volkovmedia.perfo.welloalarm.views.adapters.AlarmsAdapter;

public class AlarmsActivity extends WelloToolbarActivity implements AlarmsViewContract {

    private static final int RQ_NEW_ALARM = 101, RQ_EDIT_ALARM = 102;

    private AlarmsPresenter mPresenter;

    private RecyclerView mAlarmsRecyclerView;
    private View mNoAlarmsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarms);

        AlarmsModel model = new AlarmsModel(new AlarmDatabaseHelper(this));
        mPresenter = new AlarmsPresenter(model);
        mPresenter.attachView(this);

        mAlarmsRecyclerView = findViewById(R.id.act_main_recyclerview);
        mNoAlarmsLayout = findViewById(R.id.act_main_noalarms);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> startSettingsActivity(null));

        mAlarmsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAlarmsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAlarmsRecyclerView.setAdapter(new AlarmsAdapter(this, new AlarmsAdapter.Callback() {
            @Override
            public void deleteAlarm(Alarm alarm) {
                mPresenter.deleteAlarm(alarm);
            }

            @Override
            public void editAlarm(Alarm alarm) {
                startSettingsActivity(alarm);
            }
        }));

        mPresenter.viewIsReady();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RQ_NEW_ALARM:
                mPresenter.addAlarm(data.getParcelableExtra(Alarm.KEY_ALARM));
                break;
            case RQ_EDIT_ALARM:
                mPresenter.editAlarm(data.getParcelableExtra(Alarm.KEY_ALARM));
                break;
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
        Snackbar.make(mAlarmsRecyclerView, "Alarm " + alarm.getName() + " has been edited", Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void onAlarmDeleted(Alarm alarm, int position) {
        Snackbar.make(mAlarmsRecyclerView, "Alarm " + alarm.getName() + " has been deleted", Snackbar.LENGTH_LONG)
                .setAction(R.string.cancel, view -> mPresenter.addAlarm(alarm))
                .show();
    }

    @Override
    public void onAlarmAdded(Alarm alarm, int position) {
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