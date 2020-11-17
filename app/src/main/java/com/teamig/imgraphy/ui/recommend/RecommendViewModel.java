package com.teamig.imgraphy.ui.recommend;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.teamig.imgraphy.service.Imgraphy;
import com.teamig.imgraphy.service.ImgraphyType;

import java.util.List;

public class RecommendViewModel extends ViewModel {

    MutableLiveData<String> userID;

    private final Imgraphy imgraphy;

    public RecommendViewModel() {
        userID = new MutableLiveData<>();

        imgraphy = new Imgraphy();
    }

    public LiveData<List<ImgraphyType.Graphy>> getRecommend(ImgraphyType.Options.List option) {
        option.userid = userID.getValue();

        return imgraphy.requestRecommendList(option);
    }
}