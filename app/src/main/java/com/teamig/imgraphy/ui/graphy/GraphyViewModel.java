package com.teamig.imgraphy.ui.graphy;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.teamig.imgraphy.service.Imgraphy;
import com.teamig.imgraphy.service.ImgraphyType;

import java.util.List;

public class GraphyViewModel extends ViewModel {

    private final Imgraphy imgraphy;

    public GraphyViewModel() {
        this.imgraphy = new Imgraphy();
    }

    public LiveData<List<ImgraphyType.Graphy>> getGraphy(ImgraphyType.Options.List option) {

        return imgraphy.requestList(option);
    }
}