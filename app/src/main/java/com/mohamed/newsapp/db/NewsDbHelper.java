package com.mohamed.newsapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mohamed on 13/03/18.
 */

public class NewsDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "news_db.db";
    private static final int DATABASE_VERSION = 4;
    private final String SQL_CREATE_TABLE = "CREATE TABLE "+ NewsContract.NewsEntry.TABLE_NAME + " ( " +
            NewsContract.NewsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            NewsContract.NewsEntry.COLUMN_TITLE+" TEXT, "+ NewsContract.NewsEntry.COLUMN_AUTHOR+" TEXT, "+
            NewsContract.NewsEntry.COLUMN_DESCRIPTION+" TEXT, "+ NewsContract.NewsEntry.COLUMN_URL+" TEXT,"+
            NewsContract.NewsEntry.COLUMN_URL_TO_IMAGE+" TEXT, "+ NewsContract.NewsEntry.COLUMN_PUBLISH_AT+" TEXT, "+
            NewsContract.NewsEntry.COLUMN_CATEGORY+" TEXT, "+
            NewsContract.NewsEntry.COLUMN_FAVOURITE_NEWS+" INTEGER DEFAULT 0 );";

    private final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS "+ NewsContract.NewsEntry.TABLE_NAME;

    public NewsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

            db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);
    }
}
