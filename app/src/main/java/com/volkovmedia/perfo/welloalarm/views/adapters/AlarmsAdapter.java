package com.volkovmedia.perfo.welloalarm.views.adapters;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.volkovmedia.perfo.welloalarm.R;
import com.volkovmedia.perfo.welloalarm.general.UniqueList;
import com.volkovmedia.perfo.welloalarm.objects.Alarm;
import com.volkovmedia.perfo.welloalarm.views.adapters.viewholders.AlarmViewHolder;

public class AlarmsAdapter extends RecyclerView.Adapter<AlarmViewHolder> {

    private UniqueList<Alarm> mAlarms;
    private Callback mCallback;
    private Context mContext;

    public AlarmsAdapter(Context context, Callback callback) {
        mAlarms = new UniqueList<>();
        this.mCallback = callback;
        this.mContext = context;
    }

    @Override
    public AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm, parent, false);
        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AlarmViewHolder viewHolder, int position) {
        final Alarm currentAlarm = mAlarms.get(position);
        View rootView = viewHolder.getRootView();
        Switch switchView = viewHolder.getSwitch();

        PopupMenu menu = createPopup(currentAlarm, switchView);
        rootView.setOnLongClickListener(view -> {
            menu.show();
            return false;
        });

        rootView.setOnClickListener(view -> mCallback.editAlarm(currentAlarm));
        switchView.setOnCheckedChangeListener((compoundButton, b) -> {
            if (currentAlarm.isEnabled() != b) {
                currentAlarm.setEnabled(b);
                mCallback.switchAlarm(currentAlarm);
            }
        });

        viewHolder.setData(currentAlarm);
    }

    @Override
    public int getItemCount() {
        return mAlarms.size();
    }

    public void setDataSource(UniqueList<Alarm> alarms) {
        this.mAlarms = alarms;
        notifyDataSetChanged();
    }

    private PopupMenu createPopup(Alarm alarm, View anchor) {
        PopupMenu popupMenu = new PopupMenu(mContext, anchor);
        popupMenu.inflate(R.menu.menu_alarm);

        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_edit:
                    mCallback.editAlarm(alarm);
                    break;
                case R.id.action_delete:
                    mCallback.deleteAlarm(alarm);
                    break;
            }
            return false;
        });

        return popupMenu;
    }

    public interface Callback {
        void deleteAlarm(Alarm alarm);
        void editAlarm(Alarm alarm);
        void switchAlarm(Alarm alarm);
    }
}
