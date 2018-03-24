package com.mohamed.newsapp.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mohamed.newsapp.R;

/**
 * Created by mohamed on 13/03/18.
 */

public class NewsContentProvider extends ContentProvider {

    private Context mContext;
    private NewsDbHelper newsDbHelper;
    private static final int FAVORITES = 100;
    private static final int FAVORITE_WITH_ID = 101;
    private static final UriMatcher mUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(NewsContract.AUTHORITY,NewsContract.FAVORITE_PATH,FAVORITES);
        uriMatcher.addURI(NewsContract.AUTHORITY,NewsContract.FAVORITE_PATH+"/#",FAVORITE_WITH_ID);
        return uriMatcher;
    }


    @Override
    public boolean onCreate() {
        this.mContext = getContext();
        newsDbHelper = new NewsDbHelper(mContext);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        int match = mUriMatcher.match(uri);
        SQLiteDatabase db = newsDbHelper.getWritableDatabase();
        Cursor cursor = null;
        switch (match){
            case FAVORITES:
                cursor = db.query(NewsContract.NewsEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
                break;
            case FAVORITE_WITH_ID:
          
                cursor = db.query(NewsContract.NewsEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
        }
        if(cursor == null)
            return null;
        cursor.setNotificationUri(mContext.getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int match = mUriMatcher.match(uri);
        SQLiteDatabase db = newsDbHelper.getReadableDatabase();
        Uri retUri;
        switch (match){
            case FAVORITES:
                long id = db.insert(NewsContract.NewsEntry.TABLE_NAME,null,values);
                if(id > 0)
                    retUri = ContentUris.withAppendedId(uri,id);
                else
                    throw new SQLException(mContext.getString(R.string.failed_to_insert) + uri);
                break;
                default:
                    throw new UnsupportedOperationException(mContext.getString(R.string.unsupported_uri)+uri);
        }
        return retUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = newsDbHelper.getWritableDatabase();
        int match = mUriMatcher.match(uri);
        int rowsDel;
        switch (match){
            case FAVORITES:
                rowsDel = db.delete(NewsContract.NewsEntry.TABLE_NAME,selection,selectionArgs);
                break;
            case FAVORITE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                selection = "_id=?";
                selectionArgs = new String[]{id};
                rowsDel = db.delete(NewsContract.NewsEntry.TABLE_NAME, selection, selectionArgs);
                mContext.getContentResolver().notifyChange(uri, null);
                if (rowsDel <= 0)
                    throw new SQLException(mContext.getString(R.string.failed_to_delete) + uri);
                break;

            default:
                throw new UnsupportedOperationException(mContext.getString(R.string.unsupported_uri) + uri);
        }
        return rowsDel;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = newsDbHelper.getWritableDatabase();
        int match = mUriMatcher.match(uri);
        int rowsUpdated;
        switch (match) {
            case FAVORITES:
                rowsUpdated = db.update(NewsContract.NewsEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case FAVORITE_WITH_ID:
                rowsUpdated = db.update(NewsContract.NewsEntry.TABLE_NAME, values, selection, selectionArgs);
                if (!(rowsUpdated > 0))
                    throw new SQLException(mContext.getString(R.string.failed_to_update) + uri);
                break;

            default:
                throw new UnsupportedOperationException(mContext.getString(R.string.unsupported_uri) + uri);
        }

        return rowsUpdated;
    }
}
