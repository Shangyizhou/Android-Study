package com.example.androidnote.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidnote.R;
import com.example.androidnote.adapter.CommentAdapter;
import com.example.androidnote.db.helper.CommentHelper;
import com.example.androidnote.manager.BmobManager;
import com.example.androidnote.model.Comment;
import com.example.androidnote.model.RobotModel;
import com.shangyizhou.develop.base.BaseActivity;
import com.shangyizhou.develop.log.SLog;

import java.util.List;

public class CommentActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = ChatActivity.class.getSimpleName();

    public static void startUp(Context context, Bundle bundle) {
        SLog.i(TAG, "[CommentActivity] startUp");
        Intent intent = new Intent(context, CommentActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent, bundle);
    }

    private String mRobotId;
    private List<Comment> mCommentList;
    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private EditText editText;
    private TextView sendBtn;
    private Button leaveBtn;
    @Override
    protected void onCreateChildren(Bundle bundle) {
        setContentView(R.layout.activity_comment);
        Intent intent = getIntent();
        if (intent != null) {
            bundle = intent.getExtras();
            mRobotId = bundle.getString("robotId", "");
        }
        initView();
        getData();
    }

    private void initView() {
        recyclerView = findViewById(R.id.recycle_view);
        commentAdapter = new CommentAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(commentAdapter);
        editText = findViewById(R.id.et_input_comment);
        leaveBtn = findViewById(R.id.leave_btn);
        sendBtn = findViewById(R.id.btn_send_msg);
        sendBtn.setOnClickListener(this);
    }

    private void getData() {
        mCommentList = CommentHelper.getInstance().getCommentByRobot(mRobotId);
        commentAdapter.updateDataList(mCommentList);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_send_msg) {
            SLog.i(TAG, "sendBtn");
            String content = editText.getText().toString();
            editText.setText("");
            sendComment(content);
        } else if (id == R.id.leave_btn) {
            finish();
        }
    }

    private void sendComment(String content) {
        Comment comment = new Comment();
        comment.setUserId(BmobManager.getInstance().getObjectId());
        comment.setRobotId(mRobotId);
        comment.setContent(content);
        comment.setCreateTime(System.currentTimeMillis());
        comment.setUpdateTime(System.currentTimeMillis());
        CommentHelper.getInstance().save(comment);
        mCommentList.add(comment);
        commentAdapter.updateDataList(mCommentList);
    }
}