package com.mohamed.newsapp.activity;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.mohamed.newsapp.R;
import com.mohamed.newsapp.adapter.NewsAdapter;
import com.mohamed.newsapp.db.NewsContract;
import com.mohamed.newsapp.db.NewsContract.NewsEntry;
import com.mohamed.newsapp.model.Article;

import static com.mohamed.newsapp.CategoryFragment.ARTICLE_KEY;


public class FavoritesActivity extends AppCompatActivity implements NewsAdapter.OnItemClickListener{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.favorite_news_recycler_view)
    RecyclerView newsRecyclerView;
    @BindView(R.id.no_favorites_text_view)
    TextView textView;
    private ArrayList<Article> mArticles;
    private NewsAdapter adapter;
    public static final int CURSOR_LOADER_ID = 1;
    public static final String ARTICLES_KEY = "articles_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.favorite_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsRecyclerView.setHasFixedSize(true);
        if(savedInstanceState == null)
            getLoaderManager().initLoader(CURSOR_LOADER_ID,null,mCursorLoader);
        else{
            mArticles = savedInstanceState.getParcelableArrayList(ARTICLES_KEY);
            adapter = new NewsAdapter(FavoritesActivity.this,mArticles);
            newsRecyclerView.setAdapter(adapter);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(ARTICLES_KEY,mArticles);
    }

    private LoaderManager.LoaderCallbacks mCursorLoader = new LoaderManager.LoaderCallbacks<Cursor>() {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            Uri uri = NewsContract.NewsEntry.CONTENT_URI;
            return new CursorLoader(FavoritesActivity.this,uri,null,null,null,null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            mArticles = new ArrayList<>();
            if(data.getCount() == 0){
                textView.setVisibility(View.VISIBLE);
                newsRecyclerView.setVisibility(View.GONE);
            } else {
                textView.setVisibility(View.GONE);
                newsRecyclerView.setVisibility(View.VISIBLE);
                int indexTitle = data.getColumnIndex(NewsEntry.COLUMN_TITLE);
                int indexAuthor = data.getColumnIndex(NewsEntry.COLUMN_AUTHOR);
                int indexDescritpion = data.getColumnIndex(NewsEntry.COLUMN_DESCRIPTION);
                int indexUrl = data.getColumnIndex(NewsEntry.COLUMN_URL);
                int indexUrlToImage = data.getColumnIndex(NewsEntry.COLUMN_URL_TO_IMAGE);
                int indexPublishAt = data.getColumnIndex(NewsEntry.COLUMN_PUBLISH_AT);
                int indexFavorite = data.getColumnIndex(NewsEntry.COLUMN_FAVOURITE_NEWS);

                while (data.moveToNext()) {
                    if(data.getInt(indexFavorite) == 1) {
                        String title = data.getString(indexTitle);
                        String author = data.getString(indexAuthor);
                        String description = data.getString(indexDescritpion);
                        String url = data.getString(indexUrl);
                        String urlToImage = data.getString(indexUrlToImage);
                        String publishAt = data.getString(indexPublishAt);
                        mArticles.add(new Article(null, author, title, description, url, urlToImage, publishAt));
                    }
                }
                data.close();
                adapter = new NewsAdapter(FavoritesActivity.this,mArticles);
                newsRecyclerView.setAdapter(adapter);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };


    @Override
    public void onItemClick(int position) {
        Article article = mArticles.get(position);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARTICLE_KEY,article);
        Intent intent = new Intent(this,DetailsActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
