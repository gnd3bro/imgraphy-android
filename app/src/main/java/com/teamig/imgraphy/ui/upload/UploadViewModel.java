package com.teamig.imgraphy.ui.upload;

import android.app.Application;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.util.ByteBufferUtil;
import com.teamig.imgraphy.service.Imgraphy;
import com.teamig.imgraphy.service.ImgraphyType;

import java.io.InputStream;
import java.nio.ByteBuffer;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UploadViewModel extends AndroidViewModel {

    MutableLiveData<String> userID;
    MutableLiveData<Uri> fileUri;
    MutableLiveData<byte[]> fileByteData;
    MutableLiveData<String> fileType;

    private final Imgraphy imgraphy;
    private final Application application;

    public UploadViewModel(Application application) {
        super(application);

        userID = new MutableLiveData<>();
        fileUri = new MutableLiveData<>();
        fileByteData = new MutableLiveData<>();
        fileType = new MutableLiveData<>();

        this.application = application;
        imgraphy = new Imgraphy();
    }

    public LiveData<ImgraphyType.Result> uploadFile(String tags, int license) {
        String ext = MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType.getValue());

        RequestBody tagsRB = RequestBody.create(MediaType.parse("multipart/form-data"), tags);
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

    public void fileToByteArray(Uri uri) {
        String type = application.getContentResolver().getType(uri);

        try {
            InputStream inputStream = application.getContentResolver().openInputStream(uri);
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