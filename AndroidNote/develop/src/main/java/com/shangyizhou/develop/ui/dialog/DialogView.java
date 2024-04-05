package com.shangyizhou.develop.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.shangyizhou.develop.R;

public class DialogView extends Dialog {
    private View mContentView;
    private Context mContext;
    private TextView mTitle;
    private TextView mContent;
    private TextView mBtnCancel;
    private TextView mBtnOk;
    private OperationListener mListener;

    public DialogView(Context context) {
        super(context);
        mContext = context;
        // 获取布局文件
        mContentView = View.inflate(context, R.layout.dialog_view, null);
        // 设置对话框的布局
        setContentView(mContentView);
        initView();
    }

    private void initView() {
        WindowManager windowManager =
                (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        Point size = new Point();
        display.getSize(size);
        getWindow().setAttributes(lp);
        mBtnCancel = findViewById(R.id.btn_cancel);
        mBtnOk = findViewById(R.id.btn_ok);
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onCancel();
                }
            }
        });
        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onOk();
                }
            }
        });
    }

    public View getmContentView() {
        return mContentView;
    }

    public void setMessage(String message) {
        // 获取消息框控件
        TextView textView = findViewById(R.id.message);
        if (textView!= null) {
            textView.setText(message);
        }
    }

    public interface OperationListener {
        void onCancel();

        void onOk();
    }

    public void setOperation(OperationListener listener) {
        mListener = listener;
    }
}
