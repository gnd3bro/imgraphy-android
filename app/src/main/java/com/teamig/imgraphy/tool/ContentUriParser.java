package com.teamig.imgraphy.tool;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class ContentUriParser {

    public static Uri getExternalPath(ContentResolver contentResolver, Uri contentUri) {
        Uri imageUri;

        if (contentUri.getPath().startsWith("/storage")) {
            return contentUri;
        }

        Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.ImageColumns._ID },
                MediaStore.Images.ImageColumns._ID + "=?",
                new String[] { contentUri.getPath().split(":")[1] },
                MediaStore.Images.ImageColumns.DATE_ADDED + " DESC"
        );

        if (cursor.moveToFirst()) {
            int idColNum = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID);
            long id = cursor.getLong(idColNum);
            imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

            return imageUri;
        }
        cursor.close();

        return null;
    }
}
