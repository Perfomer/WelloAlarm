package com.volkovmedia.perfo.welloalarm.database.tasks._abstract;

@FunctionalInterface
public interface DatabaseTaskCallback<T> {
    void onTaskDone(T result);
}
