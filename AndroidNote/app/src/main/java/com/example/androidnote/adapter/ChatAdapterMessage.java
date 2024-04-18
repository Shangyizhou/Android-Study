package com.example.androidnote.adapter;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidnote.R;
import com.example.androidnote.model.Message;
import com.shangyizhou.develop.log.SLog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatAdapterMessage extends RecyclerView.Adapter {
    private List<Message> mData;
    private int NORMAL_TYPE = 0;
    private int HEADER_TYPE = 1;
    private int FOOTER_TYPE = 2;
    private int ROBOT_TYPE = 3;
    private int PERSON_TYPE = 4;
    private Map<Integer, Boolean> isDisplayed; // 用于记录每个项是否已经显示过

    public ChatAdapterMessage(List<Message> data) {
        this.isDisplayed = new HashMap<>();
        this.mData = data;
    }

    @Override
    public int getItemViewType(int position) {
        if (mData == null) {
            return -1;
        }
        Message message = mData.get(position);
        if (message.getType() == 0) {
            return ROBOT_TYPE;
        } else if (message.getType() == 1) {
            return PERSON_TYPE;
        }
        return -1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == ROBOT_TYPE) {
            SLog.i("onCreateViewHolder", "ROBOT_TYPE");
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chat_left_text_shadow, parent, false);
            RecyclerView.ViewHolder holder = new ChatAdapterMessage.RobotViewHolder(view);
            // holder.setIsRecyclable(false);
            return holder;
        } else {
            SLog.i("onCreateViewHolder", "PERSON_TYPE");
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chat_right_text_shadow, parent, false);
            RecyclerView.ViewHolder holder = new ChatAdapterMessage.PersonViewHolder(view);
            // holder.setIsRecyclable(false);
            return holder;
        }
    }
    private ObjectAnimator rotationAnimator = null;
    public static final int  LOADING = 0;
    public static final int  START_SHOW = 1;
    public static final int  HAS_SHOW = 2;
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ChatAdapterMessage.RobotViewHolder) {
            SLog.i("onBindViewHolder", "position:" + position);
            Message message = mData.get(position);
            /**
             * Loading的时候只加载图片
             * START_SHOW的时候才加入hashmap
             */
            if (message.getStatus() == LOADING) {
                SLog.i("onBindViewHolder", "LOADING");
                // 创建旋转动画
                rotationAnimator = ObjectAnimator.ofFloat(((ChatAdapterMessage.RobotViewHolder) holder).mLoading, "rotation", 0f, 360f);
                rotationAnimator.setDuration(1000);
                rotationAnimator.setRepeatCount(ObjectAnimator.INFINITE);
                // 开始动画
                rotationAnimator.start();
                ((ChatAdapterMessage.RobotViewHolder) holder).mLoading.setVisibility(View.VISIBLE);
            } else if (message.getStatus() == START_SHOW) {
                SLog.i("onBindViewHolder", "START_SHOW");
                rotationAnimator.cancel();
                ((ChatAdapterMessage.RobotViewHolder) holder).mLoading.setVisibility(View.GONE);

                // 显示动画或其他操作
                ((ChatAdapterMessage.RobotViewHolder) holder).mImageView.setImageResource(R.drawable.robot);

                String text = (mData.get(position)).getMessage();
                ValueAnimator animator = ValueAnimator.ofInt(0, text.length());
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int progress = (int) animation.getAnimatedValue();
                        ((ChatAdapterMessage.RobotViewHolder) holder).mTextView.setText(text.substring(0, progress));
                    }
                });
                animator.setDuration(1500); // 设置动画持续时间
                animator.start();
                message.setStatus(HAS_SHOW);
            } else if (message.getStatus() == HAS_SHOW) {
                SLog.i("onBindViewHolder", "HAS_SHOW");
                // 显示动画或其他操作
                ((ChatAdapterMessage.RobotViewHolder) holder).mTextView.setText(mData.get(position).getMessage());
                ((ChatAdapterMessage.RobotViewHolder) holder).mImageView.setImageResource(R.drawable.robot);
            }
            return;
        }

        if (holder instanceof ChatAdapterMessage.PersonViewHolder) {
            ((ChatAdapterMessage.PersonViewHolder) holder).mTextView.setText(mData.get(position).getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    class RobotViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        ImageView mImageView;
        ImageView mLoading;

        public RobotViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.tv_left_text);
            mImageView = itemView.findViewById(R.id.iv_left_photo);
            mLoading = itemView.findViewById(R.id.iv_left_loading);
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

    public void updateAll(List<Message> messages) {
        this.mData = messages;
        SLog.i("ChatAdapterMessage updateAll", "" + mData);
        notifyDataSetChanged();
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        SLog.i("onViewRecycled", "holder:" + holder);
        if (holder instanceof ChatAdapterMessage.RobotViewHolder) {
            // rotationAnimator.cancel();
            ((ChatAdapterMessage.RobotViewHolder) holder).mLoading.setVisibility(View.GONE);
            ((RobotViewHolder) holder).mTextView.setText("...");
        }
    }
}
