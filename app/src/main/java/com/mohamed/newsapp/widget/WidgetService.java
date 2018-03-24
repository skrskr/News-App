package com.mohamed.newsapp.widget;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.mohamed.newsapp.CategoryFragment;
import com.mohamed.newsapp.Utils;
import com.mohamed.newsapp.db.NewsContract;
import com.mohamed.newsapp.model.Article;
import com.mohamed.newsapp.model.NewsResponse;
import com.mohamed.newsapp.network.ApiClient;
import com.mohamed.newsapp.network.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mohamed.newsapp.CategoryFragment.API_KEY;
import static com.mohamed.newsapp.CategoryFragment.CATEGORY_KEY;

/**
 * Created by mohamed on 14/03/18.
 */

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetDataProvider(this);
    }
    class WidgetDataProvider implements RemoteViewsFactory{

        private Context context;
        private List<String> collections;
        private List<Article> mArticles;


        public static final String GENERAL_CAGEGORY = "general";

        public WidgetDataProvider(Context context) {
            this.context = context;
            collections = new ArrayList<>();
            mArticles = new ArrayList<>();

        }


        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
            collections.clear();
            mArticles.clear();
            readData();
        }

        private void readData() {
            Log.e("WWW","READ DATA");
            if(Utils.isOnline(context)) {
                final ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                Call<NewsResponse> call = apiInterface.getNews(GENERAL_CAGEGORY, API_KEY);
                call.enqueue(new Callback<NewsResponse>() {
                    @Override
                    public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                        NewsResponse newsResponse = response.body();
                        mArticles = newsResponse.getmArticles();
                        int len = mArticles.size();
                        context.getContentResolver().delete(NewsContract.NewsEntry.CONTENT_URI,
                                "category=? AND favorite_news=?", new String[]{GENERAL_CAGEGORY, Integer.toString(0)});
                        for (int i = 0; i < len; i++) {
                            Article article = mArticles.get(i);
                            ContentValues values = new ContentValues();
                            values.put(NewsContract.NewsEntry.COLUMN_AUTHOR, article.getmAuthor());
                            values.put(NewsContract.NewsEntry.COLUMN_TITLE, article.getmTitle());
                            values.put(NewsContract.NewsEntry.COLUMN_DESCRIPTION, article.getmDescription());
                            values.put(NewsContract.NewsEntry.COLUMN_URL, article.getmUrl());
                            values.put(NewsContract.NewsEntry.COLUMN_URL_TO_IMAGE, article.getmUrlToImage());
                            values.put(NewsContract.NewsEntry.COLUMN_PUBLISH_AT, article.getmPublishedAt());
                            values.put(NewsContract.NewsEntry.COLUMN_CATEGORY, GENERAL_CAGEGORY);
                            context.getContentResolver().insert(NewsContract.NewsEntry.CONTENT_URI, values);
                        }
                        int cnt = mArticles.size();
                        for (int i = 0; i < cnt; i++) {
                            collections.add(mArticles.get(i).getmTitle());

                        }
                    }

                    @Override
                    public void onFailure(Call<NewsResponse> call, Throwable t) {

                    }
                });

            } else {
                final long token = Binder.clearCallingIdentity();
                Cursor data = null;
                try {
                    data = getContentResolver().query(NewsContract.NewsEntry.CONTENT_URI,null,"category=?",new String[]{CATEGORY_KEY},null);
                    if(data != null){

                        mArticles = new ArrayList<>();
                        int indexTitle = data.getColumnIndex(NewsContract.NewsEntry.COLUMN_TITLE);
                        int indexAuthor = data.getColumnIndex(NewsContract.NewsEntry.COLUMN_AUTHOR);
                        int indexDescritpion = data.getColumnIndex(NewsContract.NewsEntry.COLUMN_DESCRIPTION);
                        int indexUrl = data.getColumnIndex(NewsContract.NewsEntry.COLUMN_URL);
                        int indexUrlToImage = data.getColumnIndex(NewsContract.NewsEntry.COLUMN_URL_TO_IMAGE);
                        int indexPublishAt = data.getColumnIndex(NewsContract.NewsEntry.COLUMN_PUBLISH_AT);

                        while (data.moveToNext()) {
                            String title = data.getString(indexTitle);
                            String author = data.getString(indexAuthor);
                            String description = data.getString(indexDescritpion);
                            String url = data.getString(indexUrl);
                            String urlToImage = data.getString(indexUrlToImage);
                            String publishAt = data.getString(indexPublishAt);
                            mArticles.add(new Article(null, author, title, description, url, urlToImage, publishAt));
                        }
                        data.close();
                        int cnt = mArticles.size();
                        for (int i = 0; i < cnt; i++)
                            collections.add(mArticles.get(i).getmTitle());
                    }
                }
                    finally {
                    Binder.restoreCallingIdentity(token);
                    if(data!=null)
                        data.close();
                }
            }
        }

        @Override
        public void onDestroy() {
        }

        @Override
        public int getCount() {
            return mArticles.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), android.R.layout.simple_list_item_1);
            remoteViews.setTextColor(android.R.id.text1, Color.BLACK);
            remoteViews.setTextViewText(android.R.id.text1, collections.get(position));
            Bundle bundle = new Bundle();
            bundle.putParcelable(CategoryFragment.ARTICLE_KEY, mArticles.get(position));
            Intent fillinIntent = new Intent();
            fillinIntent.putExtras(bundle);
            remoteViews.setOnClickFillInIntent(android.R.id.text1, fillinIntent);
            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

}
