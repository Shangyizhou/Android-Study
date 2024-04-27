package com.example.androidnote.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidnote.R;
import com.example.androidnote.activity.ChatActivity;
import com.example.androidnote.model.Message;
import com.shangyizhou.develop.log.SLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatAdapterMessage extends RecyclerView.Adapter {
    private List<Message> mData;
    public static List<String> mQuery = new ArrayList<>();
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
        } else if (viewType == PERSON_TYPE) {
            SLog.i("onCreateViewHolder", "PERSON_TYPE");
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chat_right_text_shadow, parent, false);
            RecyclerView.ViewHolder holder = new ChatAdapterMessage.PersonViewHolder(view);
            // holder.setIsRecyclable(false);
            return holder;
        }
        return null;
    }
    private ObjectAnimator rotationAnimator = null;
    public static final int  LOADING = 0;
    public static final int  START_SHOW = 1;
    public static final int  HAS_SHOW = 2;
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RobotViewHolder) {
            final RobotViewHolder robotViewHolder = (ChatAdapterMessage.RobotViewHolder) holder;
            Message message = mData.get(position);
            /**
             * LOADING状态：发送之后，网路数据没有到来，加载loading图片
             * START_SHOW状态：网络数据到来之后，动画加载text
             * HAS_SHOW状态：已经加载过了
             */
            if (message.getStatus() == LOADING) {
                SLog.i("onBindViewHolder", "LOADING");
                // 创建旋转动画
                rotationAnimator = ObjectAnimator.ofFloat(robotViewHolder.mLoading, "rotation", 0f, 360f);
                rotationAnimator.setDuration(1000);
                rotationAnimator.setRepeatCount(ObjectAnimator.INFINITE);
                // 开始动画
                rotationAnimator.start();
                robotViewHolder.mLoading.setVisibility(View.VISIBLE);
                robotViewHolder.mLinearLayout.setVisibility(View.GONE);
            } else if (message.getStatus() == START_SHOW) {
                if (rotationAnimator != null) {
                    rotationAnimator.cancel();
                }
                robotViewHolder.mLinearLayout.setVisibility(View.GONE);
                robotViewHolder.mImageView.setImageResource(R.drawable.robot);


                String text = (mData.get(position)).getMessage();
                ValueAnimator animator = ValueAnimator.ofInt(0, text.length());
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int progress = (int) animation.getAnimatedValue();
                        ((RobotViewHolder) holder).mTextView.setText(text.substring(0, progress));
                    }
                });

                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        // 动画结束时展示View，这里假设你想要展示的View是mImageView
                        SLog.i("onBindViewHolder", "START_SHOW mLinearLayout");
                        TextView textView1 = robotViewHolder.mLinearLayout.findViewById(R.id.tv_query_1);
                        TextView textView2 = robotViewHolder.mLinearLayout.findViewById(R.id.tv_query_2);
                        TextView textView3 = robotViewHolder.mLinearLayout.findViewById(R.id.tv_query_3);
                        if (mQuery == null || mQuery.size() < 3) {
                            return;
                        }
                        textView1.setText(mQuery.get(0));
                        textView2.setText(mQuery.get(1));
                        textView3.setText(mQuery.get(2));
                        if (position == mData.size() - 1) {
                            robotViewHolder.mLinearLayout.setVisibility(View.VISIBLE);
                        }

                        textView1.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                // 获取query
                            }
                        });

                        robotViewHolder.mLinearLayout.setVisibility(View.VISIBLE);
                    }
                });
                animator.setDuration(1500); // 设置动画持续时间
                animator.start();
                message.setStatus(HAS_SHOW);
            } else if (message.getStatus() == HAS_SHOW) {
                SLog.i("onBindViewHolder", "HAS_SHOW");
                robotViewHolder.mLinearLayout.setVisibility(View.GONE);
                // 显示动画或其他操作
                robotViewHolder.mTextView.setText(mData.get(position).getMessage());
                robotViewHolder.mImageView.setImageResource(R.drawable.robot);
                robotViewHolder.mLinearLayout.setVisibility(View.GONE);
            }
            return;
        }

        if (holder instanceof PersonViewHolder) {
            ((PersonViewHolder) holder).mTextView.setText(mData.get(position).getMessage());
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

        LinearLayout mLinearLayout; // 新增的LinearLayout成员变量

        public RobotViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.tv_left_text);
            mImageView = itemView.findViewById(R.id.iv_left_photo);
            mLoading = itemView.findViewById(R.id.iv_left_loading);
            mLinearLayout = itemView.findViewById(R.id.ll_query);
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
        // SLog.i("ChatAdapterMessage updateAll", "" + mData);
        notifyDataSetChanged();
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder instanceof ChatAdapterMessage.RobotViewHolder) {
            ((ChatAdapterMessage.RobotViewHolder) holder).mLoading.setVisibility(View.GONE);
            ((RobotViewHolder) holder).mTextView.setText("...");
        }
    }
}
