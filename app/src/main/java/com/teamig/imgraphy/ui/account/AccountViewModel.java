package com.teamig.imgraphy.ui.account;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.teamig.imgraphy.service.Imgraphy;
import com.teamig.imgraphy.service.ImgraphyType;

import java.util.List;

public class AccountViewModel extends ViewModel {

    MutableLiveData<String> userID;

    private final Imgraphy imgraphy;

    public AccountViewModel() {
        userID = new MutableLiveData<>();

        imgraphy = new Imgraphy();
    }

    public LiveData<List<ImgraphyType.Graphy>> getGraphy(ImgraphyType.Options.List option) {
        option.keyword = userID.getValue();

        return imgraphy.requestList(option);
    }

    public LiveData<List<ImgraphyType.Graphy>> getLiked(ImgraphyType.Options.List option) {
        option.userid = userID.getValue();

        return imgraphy.requestLikedList(option);
    }
}