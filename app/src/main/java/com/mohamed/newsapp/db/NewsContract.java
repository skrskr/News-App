package com.mohamed.newsapp.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by mohamed on 13/03/18.
 */

public class NewsContract {

    public static final String AUTHORITY = "com.mohamed.newsapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String FAVORITE_PATH = "news";

    public static class NewsEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(FAVORITE_PATH).build();
        public static final String TABLE_NAME = "news";
        public static final String COLUMN_TITLE ="title";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_URL_TO_IMAGE = "url_to_image";
        public static final String COLUMN_PUBLISH_AT = "publish_at";
        public static final String COLUMN_FAVOURITE_NEWS = "favorite_news";
        public static final String COLUMN_CATEGORY = "category";

    }



}
