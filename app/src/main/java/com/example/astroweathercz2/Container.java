package com.example.astroweathercz2;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Container extends ViewModel {
    private MutableLiveData<Integer> jsonData = new MutableLiveData<>();
}
