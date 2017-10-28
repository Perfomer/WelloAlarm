package com.volkovmedia.perfo.welloalarm.activities.mvp.ext.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.volkovmedia.perfo.welloalarm.R;
import com.volkovmedia.perfo.welloalarm.activities.wraps.WelloToolbarActivity;
import com.volkovmedia.perfo.welloalarm.database.AlarmDatabaseHelper;
import com.volkovmedia.perfo.welloalarm.objects.Alarm;
import com.volkovmedia.perfo.welloalarm.views.adapters.AlarmsAdapter;

import java.util.ArrayList;

public class SettingsActivity extends WelloToolbarActivity {

//    private AlarmsPresenter mPresenter;

    private RecyclerView mAlarmsRecyclerView;
    private View mNoAlarmsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarms);

//        AlarmsModel model = new AlarmsModel(new AlarmDatabaseHelper(this));
//        mPresenter = new AlarmsPresenter(model);
//        mPresenter.attachView(this);
//
//        mAlarmsRecyclerView = findViewById(R.id.act_main_recyclerview);
//        mNoAlarmsLayout = findViewById(R.id.act_main_noalarms);
//        FloatingActionButton fab = findViewById(R.id.fab);
//
//        mAlarmsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mAlarmsRecyclerView.setAdapter(new AlarmsAdapter());
//        mAlarmsRecyclerView.setItemAnimator(new DefaultItemAnimator());
//
//        fab.setOnClickListener(view -> {
//            Intent intent = new Intent(AlarmsActivity.this, SettingsActivity.class);
//        });
//
////        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                .setAction("Action", null).show());
//        mPresenter.viewIsReady();
    }

    private void switchActivityContentLayout(boolean showAlarms) {
        int visibilityPlus = showAlarms ? View.VISIBLE : View.GONE,
                visibilityMinus = showAlarms ? View.GONE : View.VISIBLE;

        mAlarmsRecyclerView.setVisibility(visibilityPlus);
        mNoAlarmsLayout.setVisibility(visibilityMinus);
    }

    public void showNoAlarmsLayout() {
        switchActivityContentLayout(false);
    }

    public void showAlarms(ArrayList<Alarm> alarms) {
        AlarmsAdapter adapter = (AlarmsAdapter) mAlarmsRecyclerView.getAdapter();
//        adapter.setDataSource(alarms);
        switchActivityContentLayout(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mPresenter.detachView();
    }
}