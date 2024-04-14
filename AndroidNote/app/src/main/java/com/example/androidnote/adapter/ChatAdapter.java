package com.example.androidnote.adapter;

import static com.example.androidnote.model.ChatModel.HAS_SHOW;
import static com.example.androidnote.model.ChatModel.LOADING;
import static com.example.androidnote.model.ChatModel.START_SHOW;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import com.example.androidnote.model.ChatModel;
import com.shangyizhou.develop.log.SLog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatAdapter extends RecyclerView.Adapter {
    private List<ChatModel> mData;
    private int NORMAL_TYPE = 0;
    private int HEADER_TYPE = 1;
    private int FOOTER_TYPE = 2;
    private int ROBOT_TYPE = 3;
    private int PERSON_TYPE = 4;
    private Map<Integer, Boolean> isDisplayed; // 用于记录每个项是否已经显示过

    public ChatAdapter() {
        this.isDisplayed = new HashMap<>();
    }

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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chat_left_text_shadow, parent, false);
            RecyclerView.ViewHolder holder = new RobotViewHolder(view);
            return holder;
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chat_right_text_shadow, parent, false);
            RecyclerView.ViewHolder holder = new PersonViewHolder(view);
            return holder;
        }
    }
    private ObjectAnimator rotationAnimator = null;
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RobotViewHolder) {
            if (!isDisplayed.containsKey(position) || !isDisplayed.get(position)) { // 判断该项是否已经显示过
                ChatModel model = mData.get(position);
                /**
                 * Loading的时候只加载图片
                 * START_SHOW的时候才加入hashmap
                 */
                if (model.getStatus() == LOADING) {
                    // 创建旋转动画
                    rotationAnimator = ObjectAnimator.ofFloat(((RobotViewHolder) holder).mLoading, "rotation", 0f, 360f);
                    rotationAnimator.setDuration(1000);
                    rotationAnimator.setRepeatCount(ObjectAnimator.INFINITE);
                    // 开始动画
                    rotationAnimator.start();
                    ((RobotViewHolder) holder).mLoading.setVisibility(View.VISIBLE);
                } else if (model.getStatus() == START_SHOW) {
                    isDisplayed.put(position, true);
                    rotationAnimator.cancel();
                    ((RobotViewHolder) holder).mLoading.setVisibility(View.GONE);

                    // 显示动画或其他操作
                    ((RobotViewHolder) holder).mImageView.setImageResource(R.drawable.robot);

                    String text = (mData.get(position)).getMessage();
                    ValueAnimator animator = ValueAnimator.ofInt(0, text.length());
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            int progress = (int) animation.getAnimatedValue();
                            ((RobotViewHolder) holder).mTextView.setText(text.substring(0, progress));
                        }
                    });
                    animator.setDuration(1500); // 设置动画持续时间
                    animator.start();
                    model.setStatus(HAS_SHOW);
                }
            } else {
                // 显示动画或其他操作
                ((RobotViewHolder) holder).mTextView.setText(mData.get(position).getMessage());
                ((RobotViewHolder) holder).mImageView.setImageResource(R.drawable.robot);
            }
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
        // notifyDataSetChanged();
        notifyItemChanged(data.size() - 1);
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
}
