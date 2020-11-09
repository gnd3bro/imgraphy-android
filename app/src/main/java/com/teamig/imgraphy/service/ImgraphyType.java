package com.teamig.imgraphy.service;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ImgraphyType {

    public static final String SUCCESS = "success";
    public static final String ERROR = "error";

    public static class Graphy {

        @SerializedName("uuid")
        public String uuid;

        @SerializedName("date")
        public long date;

        @SerializedName("ext")
        public String ext;

        @SerializedName("tag")
        public String tag;

        @SerializedName("favcnt")
        public int favcnt;

        @SerializedName("shrcnt")
        public int shrcnt;

        @SerializedName("license")
        public int license;

        @SerializedName("uploader")
        public String uploader;

        @SerializedName("deprec")
        public int deprec;
    }

    public static class ParcelableGraphy implements Parcelable {

        public Graphy graphy;

        protected ParcelableGraphy(Parcel in) {
            graphy.uuid = in.readString();
            graphy.date = in.readLong();
            graphy.ext = in.readString();
            graphy.tag = in.readString();
            graphy.favcnt = in.readInt();
            graphy.shrcnt = in.readInt();
            graphy.license = in.readInt();
            graphy.uploader = in.readString();
            graphy.deprec = in.readInt();
        }

        public ParcelableGraphy(Graphy in) {
            graphy = in;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(graphy.uuid);
            dest.writeLong(graphy.date);
            dest.writeString(graphy.ext);
            dest.writeString(graphy.tag);
            dest.writeInt(graphy.favcnt);
            dest.writeInt(graphy.shrcnt);
            dest.writeInt(graphy.license);
            dest.writeString(graphy.uploader);
            dest.writeInt(graphy.deprec);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<ParcelableGraphy> CREATOR = new Creator<ParcelableGraphy>() {
            @Override
            public ParcelableGraphy createFromParcel(Parcel in) {
                return new ParcelableGraphy(in);
            }

            @Override
            public ParcelableGraphy[] newArray(int size) {
                return new ParcelableGraphy[size];
            }
        };
    }

    public static class Result {
        @SerializedName("code")
        public String code;

        @SerializedName("log")
        public String log;
    }

    public static class Options {

        public static class List {

            public int count_per_page, page;
            public String keyword;

            public List() {}

            public List(int count_per_page, int page) {
                this.count_per_page = count_per_page;
                this.page = page;
                this.keyword = "";
            }

            public List(int count_per_page, int page, String keyword) {
                this.count_per_page = count_per_page;
                this.page = page;
                this.keyword = keyword;
            }
        }

        public static class Upload {

            public static final String UPLOAD_FILE = "uploadfile";

            public RequestBody tag, uploader, license;
            public MultipartBody.Part uploadfile;

            public Upload() {}

            public Upload(RequestBody tag, RequestBody license, RequestBody uploader, MultipartBody.Part uploadfile) {
                this.tag = tag;
                this.license = license;
                this.uploader = uploader;
                this.uploadfile = uploadfile;
            }
        }

        public static class Vote {

            public String uuid, column, type;

            public Vote() {}

            public Vote(String uuid, String column, String type) {
                this.uuid = uuid;
                this.column = column;
                this.type = type;
            }
        }
    }
}
