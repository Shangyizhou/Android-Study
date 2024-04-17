package com.example.androidnote.adapter;

import android.animation.ObjectAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidnote.R;
import com.example.androidnote.model.Session;
import com.shangyizhou.develop.log.SLog;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HistoryAdapter extends RecyclerView.Adapter {
    private List<Session> mData;
    private onItemViewClickListener mOnItemClickListener;
    public void setOnItemClickListener(onItemViewClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public HistoryAdapter() {

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
        // notifyItemChanged(data.size() - 1);
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
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(itemView, getAdapterPosition());
                    }
                }
            });
        }
    }

    public interface onItemViewClickListener {
        void onItemClick(View view, int position);
    }

}
