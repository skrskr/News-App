package com.mohamed.newsapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.mohamed.newsapp.activity.DetailsActivity;
import com.mohamed.newsapp.adapter.NewsAdapter;
import com.mohamed.newsapp.db.NewsContract.NewsEntry;
import com.mohamed.newsapp.model.Article;
import com.mohamed.newsapp.model.NewsResponse;
import com.mohamed.newsapp.network.ApiClient;
import com.mohamed.newsapp.network.ApiInterface;


public class CategoryFragment extends Fragment implements NewsAdapter.OnItemClickListener{

    public static final String API_KEY = BuildConfig.NEWS_API_KEY;
    public static final String CATEGORY_KEY = "category_key";
    public static final String ARTICLE_KEY = "article_key";
    @BindView(R.id.news_recycler_view)
    RecyclerView newsRecyclerView;
    private List<Article> mArticles;
    private NewsAdapter adapter;
    private Unbinder unbinder;
    private Context context;

    public static CategoryFragment newInstance(String category) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putString(CATEGORY_KEY,category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_category, container, false);

        unbinder = ButterKnife.bind(this,rootView);

        String category = getArguments().getString(CATEGORY_KEY);
        setUpRecyclerView(category);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void setUpRecyclerView(final String category){
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        newsRecyclerView.setHasFixedSize(true);

        final ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<NewsResponse> call = apiInterface.getNews(category,API_KEY);
        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                NewsResponse newsResponse = response.body();
                mArticles = newsResponse.getmArticles();
                adapter = new NewsAdapter(getActivity(),mArticles);
                adapter.setOnItemClickListener(CategoryFragment.this);
                adapter.notifyDataSetChanged();
                newsRecyclerView.setAdapter(adapter);
                int len = mArticles.size();
                context.getContentResolver().delete(NewsEntry.CONTENT_URI,
                      "category=? AND favorite_news=?",new String[]{category,Integer.toString(0)});
                for(int i = 0;i<len;i++){
                    Article article = mArticles.get(i);
                    ContentValues values = new ContentValues();
                    values.put(NewsEntry.COLUMN_AUTHOR,article.getmAuthor());
                    values.put(NewsEntry.COLUMN_TITLE,article.getmTitle());
                    values.put(NewsEntry.COLUMN_DESCRIPTION,article.getmDescription());
                    values.put(NewsEntry.COLUMN_URL,article.getmUrl());
                    values.put(NewsEntry.COLUMN_URL_TO_IMAGE,article.getmUrlToImage());
                    values.put(NewsEntry.COLUMN_PUBLISH_AT,article.getmPublishedAt());
                    values.put(NewsEntry.COLUMN_CATEGORY,category);
                    context.getContentResolver().insert(NewsEntry.CONTENT_URI,values);
                }
            }
            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onItemClick(int position) {
        Article article = mArticles.get(position);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARTICLE_KEY,article);
        Intent intent = new Intent(getActivity(),DetailsActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
