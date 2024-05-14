package com.example.androidnote.adapter;

import static com.example.androidnote.constant.Constants.DEFAULT_ROBOT_ID;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidnote.R;
import com.example.androidnote.model.Comment;
import com.shangyizhou.develop.log.SLog;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter{
    private static final String TAG = CommentAdapter.class.getSimpleName();
    Context mContext;
    private List<Comment> mData;

    public CommentAdapter(Context context) {
        this.mContext = context;
    }


    /**
     * viewType--分别为item以及空view
     */
    public static final int VIEW_TYPE_ITEM = 1;
    public static final int VIEW_TYPE_EMPTY = 0;
    @Override
    public int getItemViewType(int position) {
        if (mData.size() == 0) {
            return VIEW_TYPE_EMPTY;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SLog.i("onCreateViewHolder", " + position: viewType=" + viewType);

        View view = null;
        if (viewType == VIEW_TYPE_EMPTY) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty, parent, false);
            return new RecyclerView.ViewHolder(view) {};
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
            RecyclerView.ViewHolder holder = new CommentViewHolder(view);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SLog.i("onBindViewHolder", "onBindViewHolder: position=" + position);
        if (holder instanceof CommentViewHolder) {
            final Comment comment = mData.get(position);
            final CommentViewHolder commentViewHolder = (CommentViewHolder) holder;
            // if (!imUser.getPhoto().equals("")) {
            //     Bitmap bitmap = BitmapFactory.decodeFile(imUser.getPhoto());
            //     iv_me_photo.setImageBitmap(bitmap);
            // }
            // commentViewHolder.setImageView();
            // commentViewHolder.sendTime();
            // commentViewHolder.setContent();
            // commentViewHolder.setImageView();
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void updateDataList(List<Comment> data) {
        SLog.i("updateDataList", "updateDataList: data=" + data);
        this.mData = data;
        notifyDataSetChanged();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView name;
        TextView content;
        TextView sendTime;

        public CommentViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.user_photo);
            name = itemView.findViewById(R.id.user_name);
            content = itemView.findViewById(R.id.content);
            sendTime = itemView.findViewById(R.id.send_time);
        }

        public CircleImageView getImageView() {
            return imageView;
        }

        public void setImageView(CircleImageView imageView) {
            this.imageView = imageView;
        }

        public TextView getName() {
            return name;
        }

        public void setName(TextView name) {
            this.name = name;
        }

        public TextView getContent() {
            return content;
        }

        public void setContent(TextView content) {
            this.content = content;
        }

        public TextView getSendTime() {
            return sendTime;
        }

        public void setSendTime(TextView sendTime) {
            this.sendTime = sendTime;
        }
    }

    public interface onItemViewClickListener {
        void onItemClick(View view, int position);
    }
}
