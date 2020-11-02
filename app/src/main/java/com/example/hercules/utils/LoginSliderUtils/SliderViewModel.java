package com.example.hercules.utils.LoginSliderUtils;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class SliderViewModel extends ViewModel {
    private final MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    private final LiveData<Integer> mPagerIndex =
            Transformations.map(mIndex, input -> input - 1);

    public void setIndex(int index) {
        mIndex.setValue(index);
    }

    public LiveData<Integer> getText() {
        return mPagerIndex;
    }
}
