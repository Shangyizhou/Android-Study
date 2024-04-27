package com.example.androidnote.adapter;

import static com.example.androidnote.constant.Constants.DEFAULT_ROBOT_ID;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidnote.R;
import com.example.androidnote.activity.ChatActivity;
import com.example.androidnote.activity.HomeActivity;
import com.example.androidnote.db.helper.RobotHelper;
import com.example.androidnote.model.RobotModel;
import com.example.androidnote.model.Session;
import com.shangyizhou.develop.log.SLog;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HistoryAdapter extends RecyclerView.Adapter {
    private static final String TAG = HistoryAdapter.class.getSimpleName();
    private List<Session> mData;
    private onItemViewClickListener mOnItemClickListener;
    Context mContext;

    public void setOnItemClickListener(onItemViewClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public HistoryAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SLog.i("onCreateViewHolder", " + position: viewType=" + viewType);

        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_histroy, parent, false);
        RecyclerView.ViewHolder holder = new HistoryAdapter.HistoryViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HistoryAdapter.HistoryViewHolder) {
            SLog.i("onBindViewHolder", "onBindViewHolder: position=" + position);
            final Session session = mData.get(position);
            ((HistoryAdapter.HistoryViewHolder) holder).title.setText(session.getName());
            ((HistoryAdapter.HistoryViewHolder) holder).desc.setText(session.getDesc());
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void updateDataList(List<Session> data) {
        SLog.i("updateDataList", "updateDataList: data=" + data);
        this.mData = data;
        notifyDataSetChanged();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView desc;
        CircleImageView imageView;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            desc = itemView.findViewById(R.id.tv_desc);
            imageView = itemView.findViewById(R.id.iv_icon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String robotId = mData.get(getAdapterPosition()).getRobotId();
                    SLog.i("onClick", "robotId = " + robotId);
                    if (robotId.equals(DEFAULT_ROBOT_ID)) {
                        if (mOnItemClickListener != null) {
                            SLog.i("onClick", "switch to histroyfragment");
                            mOnItemClickListener.onItemClick(itemView, getAdapterPosition());
                        }
                    } else {
                        RobotModel model = RobotHelper.getInstance().takeByRobotID(robotId);
                        Bundle bundle = new Bundle();
                        bundle.putString("model", "load");
                        bundle.putSerializable("robot_data", model);
                        ChatActivity.startUp(mContext, bundle);
                    }
                }
            });
        }
    }

    public interface onItemViewClickListener {
        void onItemClick(View view, int position);
    }

}
