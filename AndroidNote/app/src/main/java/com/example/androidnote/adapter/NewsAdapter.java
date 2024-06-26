package com.example.androidnote.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidnote.R;
import com.example.androidnote.activity.news.ContentActivity;
import com.example.androidnote.model.News;
import com.google.gson.Gson;
import com.shangyizhou.develop.helper.GlideUtil;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<News> mData;
    private int NORMAL_TYPE = 0;
    private int HEADER_TYPE = 1;
    private int FOOTER_TYPE = 2;

    public NewsAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        RecyclerView.ViewHolder holder = new NewsViewHolder(view);
        return holder;
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,  int position) {
        if (holder instanceof NewsViewHolder) {
            // ((NewsViewHolder) holder).desc.setText(mData.get(position).getDescription());
            ((NewsViewHolder) holder).time.setText(mData.get(position).getTime());
            ((NewsViewHolder) holder).title.setText(mData.get(position).getTitle());
            GlideUtil.loadUrl(mContext, mData.get(position).getPicUrl(), ((NewsViewHolder) holder).mImageView);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    News news = mData.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putString("News", new Gson().toJson(news));
                    ContentActivity.startUp(mContext, bundle);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void updateDataList(List<News> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView desc;
        TextView time;
        TextView title;
        ImageView mImageView;

        public NewsViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_text);
            time = itemView.findViewById(R.id.time);
            // desc = itemView.findViewById(R.id.descr_text);
            mImageView = itemView.findViewById(R.id.title_pic);
        }
    }


}
