package com.shangyizhou.schoolchat.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shangyizhou.schoolchat.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ChatFragment.ChatMessage> mList;

    public ChatAdapter(List<ChatFragment.ChatMessage> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_msg, parent, false);
        RecyclerView.ViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatFragment.ChatMessage msg = this.mList.get(position);
        if (holder instanceof MyViewHolder) {
            final MyViewHolder viewHolder = (MyViewHolder) holder;
            if (msg.getType() == msg.TYPE_RECEIVED) {
                //如果收到的消息，则显示左边的消息布局，将右边的消息布局隐藏
                viewHolder.leftLayout.setVisibility(View.VISIBLE);
                viewHolder.rightLayout.setVisibility(View.GONE);
                viewHolder.leftMsg.setText(msg.getContent());
            } else if (msg.getType() == msg.TYPE_SENT) {
                //如果是发出的消息，则显示右边的消息布局，将左边的消息布局隐藏
                viewHolder.rightLayout.setVisibility(View.VISIBLE);
                viewHolder.leftLayout.setVisibility(View.GONE);
                viewHolder.rightMsg.setText(msg.getContent());
            }
        }

    }

    @Override
    public int getItemCount() {
        return this.mList == null ? 0 : this.mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout leftLayout;
        public LinearLayout rightLayout;
        public TextView leftMsg;
        public TextView rightMsg;


        public MyViewHolder(View view) {
            super(view);
            this.leftLayout = (LinearLayout) view.findViewById(R.id.left_layout);
            this.rightLayout = (LinearLayout) view.findViewById(R.id.right_layout);;
            this.leftMsg = (TextView) view.findViewById(R.id.left_msg);
            this.rightMsg = (TextView) view.findViewById(R.id.right_msg);;
        }
    }

}
