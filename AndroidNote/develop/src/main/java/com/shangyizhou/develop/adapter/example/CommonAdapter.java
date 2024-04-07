package com.shangyizhou.develop.adapter.example;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shangyizhou.develop.R;

import java.util.List;

public class CommonAdapter extends RecyclerView.Adapter {
    private List<String> mData;
    private int NORMAL_TYPE = 0;
    private int HEADER_TYPE = 1;
    private int FOOTER_TYPE = 2;


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == HEADER_TYPE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_headr, parent, false);
            RecyclerView.ViewHolder holder = new HeaderViewHolder(view);
            return holder;
        } else if (viewType == FOOTER_TYPE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_headr, parent, false);
            RecyclerView.ViewHolder holder = new FooterViewHolder(view);
            return holder;
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_headr, parent, false);
            RecyclerView.ViewHolder holder = new NormalViewHolder(view);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NormalViewHolder) {
            ((FooterViewHolder) holder).mTextView.setText(mData.get(position).toString());
        } else if (holder instanceof HeaderViewHolder) {
            ((FooterViewHolder) holder).mTextView.setText(mData.get(position).toString());
        } else if (holder instanceof FooterViewHolder) {
            ((FooterViewHolder) holder).mTextView.setText(mData.get(position).toString());
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER_TYPE;
        } else if (position == mData.size() - 1) {
            return FOOTER_TYPE;
        } else {
            return NORMAL_TYPE;
        }
    }

    public void updateDataList(List<String> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    class NormalViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        ImageView mImageView;
        public NormalViewHolder(View itemView) {
            super(itemView);
            TextView mTextView;
            ImageView mImageView;
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        ImageView mImageView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            TextView mTextView;
            ImageView mImageView;
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        ImageView mImageView;
        public FooterViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mTextView = itemView.findViewById(R.id.textView);
        }
    }


}
