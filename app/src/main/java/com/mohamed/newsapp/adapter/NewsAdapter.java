package com.mohamed.newsapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mohamed.newsapp.R;
import com.mohamed.newsapp.model.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mohamed on 09/03/18.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private Context mContext;
    private List<Article> mArticlesList;
    private LayoutInflater inflater;
    private OnItemClickListener clickListener;

    public NewsAdapter(Context context, List<Article> articles){
        this.mContext = context;
        this.mArticlesList = articles;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.news_item,parent,false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        Article article = mArticlesList.get(position);
        if(article.getmTitle() != null)
            holder.newsTitle.setText(article.getmTitle());

        if(article.getmPublishedAt() != null)
            holder.newsDate.setText(article.getmPublishedAt());

        if(!TextUtils.isEmpty(article.getmUrlToImage()))
            Picasso.with(mContext).load(article.getmUrlToImage()).into(holder.circleImageView);
        else
            Picasso.with(mContext).load(R.drawable.news).into(holder.circleImageView);
    }

    @Override
    public int getItemCount() {
        return mArticlesList.size();
    }

    public void setOnItemClickListener(OnItemClickListener clickListener){
        this.clickListener = clickListener;
    }

    class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.news_image)
        CircleImageView circleImageView;
        @BindView(R.id.news_title_text_view)
        TextView newsTitle;
        @BindView(R.id.news_date_text_view)
        TextView newsDate;

        public NewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(clickListener!=null)
                clickListener.onItemClick(getAdapterPosition());
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
}
