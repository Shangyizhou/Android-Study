package com.example.androidnote.activity;

import static com.example.androidnote.constant.Constants.YIYAN_HANDLER_NORMAL;
import static com.example.androidnote.constant.Constants.YIYAN_HANDLER_QUERY;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidnote.R;
import com.example.androidnote.model.RobotModel;
import com.example.androidnote.net.DirectToServer;
import com.example.androidnote.net.YiYanHandler;
import com.shangyizhou.develop.base.BaseActivity;
import com.shangyizhou.develop.log.SLog;
import com.shangyizhou.develop.net.IResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseActivity extends BaseActivity {
    private static final String TAG = ParseActivity.class.getSimpleName();

    public static void startUp(Context context, Bundle bundle) {
        SLog.i(TAG, "[ParseActivity] startUp");
        Intent intent = new Intent(context, ParseActivity.class);
        if (bundle != null) {
            intent.putExtra("query", bundle.getString("query"));
        }
        context.startActivity(intent);
    }

    public static void startUp(Context context) {
        SLog.i(TAG, "[ParseActivity] startUp");
        Intent intent = new Intent(context, ParseActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreateChildren(Bundle bundle) {
        // super.onCreateChildren(bundle);
        setContentView(R.layout.activity_parse);
        Intent intent = getIntent();
        if (intent != null) {
            mQueryStr = intent.getStringExtra("query");
        }
        initView();
        getData();
    }

    private String mQueryStr;
    TextView editQuery;
    TextView editAdvice;
    TextView editBook1;
    TextView editBook2;
    TextView editBook3;

    private void initView() {
        editQuery = findViewById(R.id.edit_query);
        editAdvice = findViewById(R.id.edit_advice);
        editBook1 = findViewById(R.id.edit_book_1);
        editBook2 = findViewById(R.id.edit_book_2);
        editBook3 = findViewById(R.id.edit_book_3);
    }

    private void setUi() {
        SLog.i(TAG, "setUi");
        editQuery.setText(YiYanHandler.mQueryStr);
        editAdvice.setText(YiYanHandler.mAdviceStr);

        String books = YiYanHandler.mBookStr;
        // 正则表达式，匹配被《》包裹的任意字符
        String regex = "《([^》]+)》";

        // 编译正则表达式
        Pattern pattern = Pattern.compile(regex);

        // 使用matcher来查找字符串
        Matcher matcher = pattern.matcher(books);

        List<String> bookList = new ArrayList<>();
        List<TextView> editTexts = new ArrayList<>();
        editTexts.add(editBook1);
        editTexts.add(editBook2);
        editTexts.add(editBook3);
        // 遍历所有的匹配结果
        while (matcher.find()) {
            // group(1)是第一个括号内匹配的文本，即书籍的名字
            System.out.println(matcher.group(1));
            bookList.add(matcher.group(1));
        }
        for (int i = 0; i < bookList.size(); i++) {
            editTexts.get(i).setText(bookList.get(i));
        }
    }

    static List<Map<String, String>> messages = new ArrayList<>();
    private void getData() {
        String text = mQueryStr;
        String type = YIYAN_HANDLER_QUERY;
        DirectToServer.callYiYanERNIELiteStream(text, type, new IResponse() {
            @Override
            public void onSuccess(String originJson) {
                // 将回复的内容添加到消息中
                HashMap<String, String> assistant = new HashMap<>();
                assistant.put("role", "assistant");
                assistant.put("content", "");
                // 取出我们需要的内容,也就是result部分
                String[] answerArray = originJson.split("data: ");
                for (int i = 1; i < answerArray.length; ++i) {
                    answerArray[i] = answerArray[i].substring(0, answerArray[i].length() - 2);
                    SLog.i(TAG, "answerArray: " + answerArray[i]);
                    try {
                        assistant.put("content", assistant.get("content") + new JSONObject(answerArray[i]).getString("result"));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                messages.add(assistant);
                if (type.equals(YIYAN_HANDLER_QUERY)) {
                    YiYanHandler.processQuery(getApplication(), assistant.get("content"));
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setUi();
                    }
                });
            }

            @Override
            public void onFailure(String errorMsg) {
                SLog.e(TAG, "DirectToServer error");
            }
        });
    }



}