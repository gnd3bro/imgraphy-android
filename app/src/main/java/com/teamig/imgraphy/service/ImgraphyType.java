package com.teamig.imgraphy.service;

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
        public boolean deprec;
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
