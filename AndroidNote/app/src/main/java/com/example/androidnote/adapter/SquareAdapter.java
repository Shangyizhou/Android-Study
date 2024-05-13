package com.example.androidnote.adapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidnote.R;
import com.example.androidnote.activity.ChatActivity;
import com.example.androidnote.db.helper.RobotHelper;
import com.example.androidnote.db.helper.SessionHelper;
import com.example.androidnote.manager.SessionManager;
import com.example.androidnote.model.ChatModel;
import com.example.androidnote.model.RobotModel;
import com.example.androidnote.model.Session;
import com.shangyizhou.develop.log.SLog;

import java.util.List;

public class SquareAdapter extends RecyclerView.Adapter {
    private List<RobotModel> mData;
    private OnItemViewClickListener mOnItemClickListener;

    public SquareAdapter(OnItemViewClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_square_item, parent, false);
        RecyclerView.ViewHolder holder = new RobotViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RobotViewHolder) {
            ((RobotViewHolder) holder).mImageView.setImageResource(R.drawable.ai_image_2);
            ((RobotViewHolder) holder).title.setText(mData.get(position).getTitle());
            ((RobotViewHolder) holder).desc.setText(mData.get(position).getDesc());
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void updateDataList(List<RobotModel> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    class RobotViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView desc;
        ImageView mImageView;

        public RobotViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.model_title);
            desc = itemView.findViewById(R.id.model_desc);
            mImageView = itemView.findViewById(R.id.model_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // if (mOnItemClickListener != null) {
                    //     mOnItemClickListener.onItemClick(itemView, getAdapterPosition());
                    // }
                    RobotModel model = mData.get(getAdapterPosition());
                    Session session = SessionManager.getInstance().getSessionByRobotId(model.getRobotId());
                    if (session == null) {
                        session = model.createSession();
                        SessionHelper.getInstance().save(session);
                        SessionManager.getInstance().addNewSession(session);
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString("model", "load");
                    bundle.putSerializable("robot_data", model);
                    ChatActivity.startUp(itemView.getContext(), bundle);
                }
            });
        }
    }

    public void updateAll(List<RobotModel> data) {
        this.mData = data;
        notifyDataSetChanged();
    }
}
