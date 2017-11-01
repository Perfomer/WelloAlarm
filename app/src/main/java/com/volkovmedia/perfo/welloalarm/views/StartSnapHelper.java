package com.volkovmedia.perfo.welloalarm.views;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class StartSnapHelper extends LinearSnapHelper {

    @Override
    public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager,
                                              @NonNull View targetView) {
        int[] out = new int[2];
        out[0] = 0;
        out[1] = ((WelloLayoutManager) layoutManager).getSnapHeight();
        return out;
    }

    @Override
    public View findSnapView(RecyclerView.LayoutManager layoutManager) {
        WelloLayoutManager custLayoutManager = (WelloLayoutManager) layoutManager;
        return custLayoutManager.findSnapView();
    }
}