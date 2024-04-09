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

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter {
    private List<ChatModel> mData;
    private int NORMAL_TYPE = 0;
    private int HEADER_TYPE = 1;
    private int FOOTER_TYPE = 2;
    private int ROBOT_TYPE = 3;
    private int PERSON_TYPE = 4;

    @Override
    public int getItemViewType(int position) {
        if (mData == null) {
            return -1;
        }
        ChatModel chatModel = mData.get(position);
        if (chatModel.getType() == 0) {
            return ROBOT_TYPE;
        } else if (chatModel.getType() == 1) {
            return PERSON_TYPE;
        }
        return -1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == ROBOT_TYPE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chat_left_text, parent, false);
            RecyclerView.ViewHolder holder = new RobotViewHolder(view);
            return holder;
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chat_right_text, parent, false);
            RecyclerView.ViewHolder holder = new PersonViewHolder(view);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RobotViewHolder) {
            ((RobotViewHolder) holder).mTextView.setText(mData.get(position).getMessage());
        } else if (holder instanceof PersonViewHolder) {
            ((PersonViewHolder) holder).mTextView.setText(mData.get(position).getMessage());
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
