package com.teamig.imgraphy.service;

import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Imgraphy {

    OkHttpClient okHttpClient;
    Retrofit retrofit;
    ImgraphyService service;

    public Imgraphy() {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.novang.tk/imgraphy/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(ImgraphyService.class);
    }

    private void graphyListEnqueue(MutableLiveData<List<ImgraphyType.Graphy>> graphyList, Call<List<ImgraphyType.Graphy>> graphyCall) {
        graphyCall.enqueue(new Callback<List<ImgraphyType.Graphy>>() {
            @Override
            public void onResponse(Call<List<ImgraphyType.Graphy>> call, Response<List<ImgraphyType.Graphy>> response) {
                graphyList.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<ImgraphyType.Graphy>> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    private void graphyResultEnqueue(MutableLiveData<ImgraphyType.Result> result, Call<ImgraphyType.Result> graphyCall) {
        graphyCall.enqueue(new Callback<ImgraphyType.Result>() {
            @Override
            public void onResponse(Call<ImgraphyType.Result> call, Response<ImgraphyType.Result> response) {
                result.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ImgraphyType.Result> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    public MutableLiveData<List<ImgraphyType.Graphy>> requestList(ImgraphyType.Options.List option) {
        MutableLiveData<List<ImgraphyType.Graphy>> graphyList = new MutableLiveData<>();

        Call<List<ImgraphyType.Graphy>> graphyCall = service.requestList(option.max, option.from, option.keyword);
        graphyListEnqueue(graphyList, graphyCall);

        return graphyList;
    }

    public MutableLiveData<List<ImgraphyType.Graphy>> requestRecommendList(ImgraphyType.Options.List option) {
        MutableLiveData<List<ImgraphyType.Graphy>> graphyList = new MutableLiveData<>();

        Call<List<ImgraphyType.Graphy>> graphyCall = service.requestRecommendList(option.max, option.from);
        graphyListEnqueue(graphyList, graphyCall);

        return graphyList;
    }

    public MutableLiveData<List<ImgraphyType.Graphy>> requestLikedList(ImgraphyType.Options.List option) {
        MutableLiveData<List<ImgraphyType.Graphy>> graphyList = new MutableLiveData<>();

        Call<List<ImgraphyType.Graphy>> graphyCall = service.requestLikedList(option.max, option.from, option.userid);
        graphyListEnqueue(graphyList, graphyCall);

        return graphyList;
    }

    public MutableLiveData<ImgraphyType.Result> uploadGraphy(ImgraphyType.Options.Upload option) {
        MutableLiveData<ImgraphyType.Result> result = new MutableLiveData<>();

        Call<ImgraphyType.Result> graphyCall = service.uploadGraphy(option.tag, option.license, option.uploader, option.uploadfile);
        graphyResultEnqueue(result, graphyCall);

        return result;
    }

    public MutableLiveData<ImgraphyType.Result> generateID(boolean confirm) {
        MutableLiveData<ImgraphyType.Result> result = new MutableLiveData<>();

        Call<ImgraphyType.Result> graphyCall = service.generateID(confirm);
        graphyResultEnqueue(result, graphyCall);

        return result;
    }

    public MutableLiveData<ImgraphyType.Result> voteGraphy(ImgraphyType.Options.Vote option) {
        MutableLiveData<ImgraphyType.Result> result = new MutableLiveData<>();

        Call<ImgraphyType.Result> graphyCall = service.voteGraphy(option.uuid, option.userid, option.type);
        graphyResultEnqueue(result, graphyCall);

        return result;
    }

    public MutableLiveData<ImgraphyType.Result> shareCount(String uuid) {
        MutableLiveData<ImgraphyType.Result> result = new MutableLiveData<>();

        Call<ImgraphyType.Result> graphyCall = service.shareCount(uuid);
        graphyResultEnqueue(result, graphyCall);

        return result;
    }

    public MutableLiveData<ImgraphyType.Result> checkGraphyVote(ImgraphyType.Options.Vote option) {
        MutableLiveData<ImgraphyType.Result> result = new MutableLiveData<>();

        Call<ImgraphyType.Result> graphyCall = service.checkGraphyVote(option.uuid, option.userid);
        graphyResultEnqueue(result, graphyCall);

        return result;
    }

    public MutableLiveData<ImgraphyType.Result> deprecateGraphy(boolean confirm, String uuid) {
        MutableLiveData<ImgraphyType.Result> result = new MutableLiveData<>();

        Call<ImgraphyType.Result> graphyCall = service.deprecateGraphy(confirm, uuid);
        graphyResultEnqueue(result, graphyCall);

        return result;
    }
}
