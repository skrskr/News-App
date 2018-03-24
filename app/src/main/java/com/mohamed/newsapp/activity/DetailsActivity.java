package com.mohamed.newsapp.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mohamed.newsapp.CategoryFragment;
import com.mohamed.newsapp.R;
import com.mohamed.newsapp.model.Article;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.mohamed.newsapp.db.NewsContract.NewsEntry;

public class DetailsActivity extends AppCompatActivity {

    public static final String ARTICLE_SAVE_KEY = "article_save_key";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.details_news_image)
    ImageView newsImage;
    @BindView(R.id.details_news_title)
    TextView newsTitleTextView;
    @BindView(R.id.details_news_author)
    TextView newsAuthorTextView;
    @BindView(R.id.details_news_desc)
    TextView newsDescTextView;
    private Article article;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.details_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(savedInstanceState == null) {
            Bundle bundle = getIntent().getExtras();
            article = bundle.getParcelable(CategoryFragment.ARTICLE_KEY);
        } else {
            article = savedInstanceState.getParcelable(ARTICLE_SAVE_KEY);
        }

        newsTitleTextView.setText(article.getmTitle());
        newsAuthorTextView.setText(article.getmAuthor());
        newsDescTextView.setText(article.getmDescription());
        if(!TextUtils.isEmpty(article.getmUrlToImage()))
            Picasso.with(this).load(article.getmUrlToImage()).into(newsImage);
        else
            Picasso.with(this).load(R.drawable.news).into(newsImage);
    }

    @OnClick(R.id.details_news_read_more)
    public void openNewsSource(View view){
        Intent intent=new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(article.getmUrl()));
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARTICLE_SAVE_KEY,article);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.details_menu,menu);
        if(!isFavoriteArticle()){
            menu.findItem(R.id.action_add_to_favorite).setTitle(getResources().getString(R.string.add_to_favorite));
        }
        else{
            menu.findItem(R.id.action_add_to_favorite).setTitle(getResources().getString(R.string.remove_from_favorite));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add_to_favorite:
                if(!isFavoriteArticle()){
                    addToFavorites();
                    menu.findItem(R.id.action_add_to_favorite).setTitle(getResources().getString(R.string.remove_from_favorite));
                }
                else{
                    removeFromFavorites();
                    menu.findItem(R.id.action_add_to_favorite).setTitle(getResources().getString(R.string.add_to_favorite));
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isFavoriteArticle(){
        Cursor cursor = getContentResolver().query(NewsEntry.CONTENT_URI,new String[]{NewsEntry.COLUMN_FAVOURITE_NEWS},"title=?",new String[]{article.getmTitle()},null);
        int indx = cursor.getColumnIndex(NewsEntry.COLUMN_FAVOURITE_NEWS);
        cursor.moveToFirst();
        boolean isFav =  cursor.getInt(indx) == 1;
        cursor.close();
        return isFav;
    }
    private void addToFavorites(){
        ContentValues values = new ContentValues();
        values.put(NewsEntry.COLUMN_FAVOURITE_NEWS,1);
        getContentResolver().update(NewsEntry.CONTENT_URI,values,"title=?",new String[]{article.getmTitle()});
        Toast.makeText(this, R.string.news_added_successfully, Toast.LENGTH_SHORT).show();
    }
    private void removeFromFavorites(){
        ContentValues values = new ContentValues();
        values.put(NewsEntry.COLUMN_FAVOURITE_NEWS,0);
        getContentResolver().update(NewsEntry.CONTENT_URI,values,"title=?",new String[]{article.getmTitle()});
        Toast.makeText(this, R.string.news_removed_successfully, Toast.LENGTH_SHORT).show();
    }
}
