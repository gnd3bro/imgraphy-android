package com.teamig.imgraphy.service;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ImgraphyService {

    @Headers({"Content-Type: application/json; charset=UTF-8"})
    @GET("api/list.php")
    Call<List<ImgraphyType.Graphy>> requestList(@Query("max") int max,
                                                @Query("page") int page,
                                                @Query("keyword") String keyword);

    @Headers({"Content-Type: charset=UTF-8"})
    @Multipart
    @POST("api/upload.php")
    Call<ImgraphyType.Result> uploadGraphy(@Part("tag") RequestBody tag,
                                           @Part("license") RequestBody license,
                                           @Part("uploader") RequestBody uploader,
                                           @Part MultipartBody.Part uploadfile);

    @Headers({"Content-Type: application/json; charset=UTF-8"})
    @GET("api/idgen.php")
    Call<ImgraphyType.Result> generateID(@Query("confirm") boolean confirm);

    @Headers({"Content-Type: application/json; charset=UTF-8"})
    @GET("api/vote.php")
    Call<ImgraphyType.Result> voteGraphy(@Query("uuid") String uuid,
                                         @Query("column") String column,
                                         @Query("type") String type);

    @Headers({"Content-Type: application/json; charset=UTF-8"})
    @GET("api/deprecate.php")
    Call<ImgraphyType.Result> deprecateGraphy(@Query("confirm") boolean confirm,
                                              @Query("uuid") String uuid);
}
