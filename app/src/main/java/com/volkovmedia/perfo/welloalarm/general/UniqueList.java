package com.volkovmedia.perfo.welloalarm.general;

import com.volkovmedia.perfo.welloalarm.objects.DataContainer;

import java.util.ArrayList;
import java.util.HashSet;

public class UniqueList<T extends DataContainer> {

    private ArrayList<T> mList;
    private HashSet<Integer> mKeys;

    public UniqueList() {
        mList = new ArrayList<>();
        mKeys = new HashSet<>();
    }

    public boolean add(T item) {
        if (item != null) {
            int key = item.getId();

            if (!mKeys.contains(key)) {
                mKeys.add(key);
                mList.add(item);
                return true;
            }
        }

        return false;
    }

    public void update(T item) {
        mList.remove(getByKey(item.getId()));
        mList.add(item);
    }

    public void remove(T item) {
        mList.remove(item);
        mKeys.remove(item.getId());
    }

    public T get(int index) {
        return mList.get(index);
    }

    public T getByKey(int key) {
        for (T item : mList) {
            if (item.getId() == key) {
                return item;
            }
        }

        return null;
    }

    public int getPositionByKey(int key) {
        for (int i = 0; i < mKeys.size(); i++) {
            T item = mList.get(i);

            if (item.getId() == key) {
                return i;
            }
        }

        return -1;
    }

    public boolean contains(int key) {
        return mKeys.contains(key);
    }

    public int size() {
        return mList.size();
    }

    public int indexOf(T item) {
        return mList.indexOf(item);
    }

    public ArrayList<T> toArrayList() {
        return mList;
    }

}