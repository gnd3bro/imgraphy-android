package com.teamig.imgraphy.ui.upload;

import android.content.ContentResolver;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.util.ByteBufferUtil;
import com.teamig.imgraphy.service.Imgraphy;
import com.teamig.imgraphy.service.ImgraphyType;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UploadViewModel extends ViewModel {

    MutableLiveData<String> userID;
    MutableLiveData<List<String>> inputTagList;
    MutableLiveData<Uri> fileUri;
    MutableLiveData<byte[]> fileByteData;
    MutableLiveData<String> fileType;

    private final Imgraphy imgraphy;

    public UploadViewModel() {
        userID = new MutableLiveData<>();
        inputTagList = new MutableLiveData<>(new ArrayList<>());
        fileUri = new MutableLiveData<>();
        fileByteData = new MutableLiveData<>();
        fileType = new MutableLiveData<>();

        imgraphy = new Imgraphy();
    }

    public LiveData<ImgraphyType.Result> uploadFile(int license) {
        String ext = MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType.getValue());
        StringBuilder tags = new StringBuilder();

        for (String s : inputTagList.getValue()) {
            tags.append(s).append(";");
        }

        RequestBody tagsRB = RequestBody.create(MediaType.parse("multipart/form-data"), tags.toString());
        RequestBody licenseRB = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(license));
        RequestBody uploaderRB = RequestBody.create(MediaType.parse("multipart/form-data"), userID.getValue());

        RequestBody requestBody = RequestBody.create(MediaType.parse(fileType.getValue()), fileByteData.getValue());
        MultipartBody.Part requestFile = MultipartBody.Part.createFormData(
                ImgraphyType.Options.Upload.UPLOAD_FILE,
                String.format("%s.%s", userID.getValue(), ext),
                requestBody
        );

        ImgraphyType.Options.Upload option = new ImgraphyType.Options.Upload(
                tagsRB,
                licenseRB,
                uploaderRB,
                requestFile
        );

        return imgraphy.uploadGraphy(option);
    }

    public void fileToByteArray(ContentResolver contentResolver, Uri uri) {
        String type = contentResolver.getType(uri);

        try {
            InputStream inputStream = contentResolver.openInputStream(uri);
            ByteBuffer byteBuffer = ByteBufferUtil.fromStream(inputStream);

            byte[] bytesArray = new byte[byteBuffer.remaining()];
            byteBuffer.get(bytesArray, 0, bytesArray.length);

            inputStream.close();

            fileByteData.postValue(bytesArray);
            fileType.postValue(type);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}