package com.volkovmedia.perfo.welloalarm.activities.wraps;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.volkovmedia.perfo.welloalarm.R;
import com.volkovmedia.perfo.welloalarm.activities.mvp.impl.MvpView;

@SuppressLint("Registered")
public class WelloToolbarActivity extends AppCompatActivity {

    private TextView mActivityName;
    private View mApplicationIcon;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);

        mActivityName = findViewById(R.id.wello_actbar_activityname);
        mApplicationIcon = findViewById(R.id.wello_actbar_logo);

        initHeader();
    }

    protected void displayArrowBack() {
        View arrowBack = findViewById(R.id.wello_actbar_arrowback);
        arrowBack.setVisibility(View.VISIBLE);

        arrowBack.setOnClickListener(view -> onBackPressed());
    }

    private void initHeader() {
        PackageManager packageManager = getPackageManager();
        ActivityInfo info;

        try {
            info = packageManager.getActivityInfo(getComponentName(), 0);
        } catch (PackageManager.NameNotFoundException ex) {
            switchHeader(true);
            return;
        }

        try {
            mActivityName.setText(getResources().getString(info.labelRes));
            switchHeader(false);
        } catch (Resources.NotFoundException ex) {
            switchHeader(true);
        }
    }

    private void switchHeader(boolean showIcon) {
        int visibilityPlus = showIcon ? View.VISIBLE : View.GONE,
            visibilityMinus = showIcon ? View.GONE : View.VISIBLE;

        mActivityName.setVisibility(visibilityMinus);
        mApplicationIcon.setVisibility(visibilityPlus);
    }
}
