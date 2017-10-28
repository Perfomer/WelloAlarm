package com.volkovmedia.perfo.welloalarm.views.adapters.viewholders;

import android.content.Context;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.volkovmedia.perfo.welloalarm.R;
import com.volkovmedia.perfo.welloalarm.objects.Alarm;

import static com.volkovmedia.perfo.welloalarm.general.GeneralMethods.getTrueItemsCount;
import static com.volkovmedia.perfo.welloalarm.general.GeneralMethods.getTimeText;
import static com.volkovmedia.perfo.welloalarm.general.ResourceManager.getDayName;

public class AlarmViewHolder extends RecyclerView.ViewHolder {

    private TextView mTime, mName;
    private GridLayout mDays, mWeeks;
    private Switch mSwitch;
    private View mRootView;

    public AlarmViewHolder(View itemView) {
        super(itemView);

        mRootView = itemView;

        mTime = itemView.findViewById(R.id.i_alarm_time);
        mName = itemView.findViewById(R.id.i_alarm_name);

        mDays = itemView.findViewById(R.id.i_alarm_days);
        mWeeks = itemView.findViewById(R.id.i_alarm_weeks);

        mSwitch = itemView.findViewById(R.id.i_alarm_switch);
    }

    public View getClickableView() {
        return mRootView;
    }

    public void setData(Alarm data) {
        mTime.setText(getTimeText(data.getMinutes(), data.getHours()));
        mName.setText(data.getName());
        mSwitch.setChecked(data.isEnabled());

        initGridViews(data);
    }

    private static void addViewToGrid(GridLayout grid, View view) {
        GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
        lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.setMargins(0, 0, 6, 0);

        grid.addView(view, lp);
    }

    private TextView getReadyDateView(ContextThemeWrapper ctw, int textId) {
        return getReadyDateView(ctw, mRootView.getContext().getString(textId));
    }

    private TextView getReadyDateView(ContextThemeWrapper ctw, String text) {
        TextView week = new TextView(ctw);
        week.setText(text);
        return week;
    }

    private void initGridViews(Alarm alarm) {
        Context context = mRootView.getContext();
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, R.style.DayViewMini);

        boolean[] days = alarm.getDays();
        int daysCount = getTrueItemsCount(days);

        switch (daysCount) {
            case 0:
                addViewToGrid(mDays, getReadyDateView(contextThemeWrapper, R.string.once));
                break;
            case 7:
                addViewToGrid(mDays, getReadyDateView(contextThemeWrapper, R.string.everyday));
                break;
            default:
                for (int i = 0; i < days.length; i++) {
                    if (days[i]) {
                        String text = getDayName(context.getResources(), i + 1, false);
                        addViewToGrid(mDays, getReadyDateView(contextThemeWrapper, text));
                    }
                }
        }

        if (daysCount != 0) {
            boolean[] weeks = alarm.getWeeks();

            contextThemeWrapper = new ContextThemeWrapper(context, R.style.WeekViewMini);

            if (weeks[0]) {
                addViewToGrid(mWeeks, getReadyDateView(contextThemeWrapper, R.string.ev));
            }

            if (weeks[1]) {
                addViewToGrid(mWeeks, getReadyDateView(contextThemeWrapper, R.string.od));
            }
        }
    }
}
