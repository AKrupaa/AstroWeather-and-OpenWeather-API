package com.example.astroweathercz2;

import android.content.ContentValues;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<ContentValues> sharedData = new MutableLiveData<ContentValues>();
    private MutableLiveData<Integer> myInteger = new MutableLiveData<Integer>();

    public SharedViewModel() { }

    public void clearSharedData() {
        sharedData = null;
        sharedData = new MutableLiveData<ContentValues>();
    }

    public void setSharedData(ContentValues contentValues){
        sharedData.setValue(contentValues);
    }

    public ContentValues getSharedData() {
        return sharedData.getValue();
    }
}
