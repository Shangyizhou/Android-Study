package com.example.androidnote.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidnote.R;
import com.example.androidnote.model.ChatModel;
import com.example.androidnote.model.Title;

import java.util.List;
import java.util.Timer;

public class NewsAdapter {
    private List<Title> mData;
    private int NORMAL_TYPE = 0;
    private int HEADER_TYPE = 1;
    private int FOOTER_TYPE = 2;


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == ROBOT_TYPE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chat_left_text, parent, false);
            RecyclerView.ViewHolder holder = new CommonAdapter.RobotViewHolder(view);
            return holder;
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chat_right_text, parent, false);
            RecyclerView.ViewHolder holder = new CommonAdapter.PersonViewHolder(view);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CommonAdapter.RobotViewHolder) {
            ((CommonAdapter.RobotViewHolder) holder).mTextView.setText(mData.get(position).getMessage());
        } else if (holder instanceof CommonAdapter.PersonViewHolder) {
            ((CommonAdapter.PersonViewHolder) holder).mTextView.setText(mData.get(position).getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void updateDataList(List<ChatModel> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    class RobotViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        ImageView mImageView;

        public RobotViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.tv_left_text);
            mImageView = itemView.findViewById(R.id.iv_left_photo);
        }
    }

    class PersonViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        ImageView mImageView;

        public PersonViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.tv_right_text);
            mImageView = itemView.findViewById(R.id.iv_right_photo);
        }
    }
}
