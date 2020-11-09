package com.teamig.imgraphy.ui.graphy;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.teamig.imgraphy.service.Imgraphy;
import com.teamig.imgraphy.service.ImgraphyType;

import java.util.List;

public class GraphyViewModel extends ViewModel {

    MutableLiveData<String> userID;
    MutableLiveData<String> keyword;

    private final Imgraphy imgraphy;

    public GraphyViewModel() {
        userID = new MutableLiveData<>();
        keyword = new MutableLiveData<>();

        imgraphy = new Imgraphy();
    }

    public LiveData<List<ImgraphyType.Graphy>> getGraphy(ImgraphyType.Options.List option) {

        return imgraphy.requestList(option);
    }
}