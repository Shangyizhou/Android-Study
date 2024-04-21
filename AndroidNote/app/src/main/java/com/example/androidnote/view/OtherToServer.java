package com.example.androidnote.view;

import android.text.TextUtils;

import com.example.androidnote.model.TagRequest;
import com.example.androidnote.model.TagResponse;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.shangyizhou.develop.AppHolder;
import com.shangyizhou.develop.log.SLog;
import com.shangyizhou.develop.net.IResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OtherToServer {

    private static final String TAG = "OtherToServer";

    private static final OkHttpClient client = createOkHttpClient();

    private static String requestId;

    /**
     * 不能直接从BaseApplication中获取
     */
    private static OkHttpClient createOkHttpClient() {
        return AppHolder.getApp().getGlobalOkHttpClient().newBuilder().build();
    }

    private static String CLIENT_ID = "PDyYicMn3213Sbl42sFtf300";
    private static String CLIENT_SECRET = "aULHDPT2joh1aQITo1TAg2ndNOn2SdA5";

    private static String ACCESS_TOKEN = null;
    private static String REFRESH_TOKEN = null;
    private static Date CREATE_TIME = null;     // accessToken创建时间
    private static Date EXPIRATION_TIME = null; // accessToken到期时间

    final static CountDownLatch countDownLatch = new CountDownLatch(1);

    /**
     * 获取token
     */
    public static String getAccessToken() {
        if (!TextUtils.isEmpty(ACCESS_TOKEN) && EXPIRATION_TIME.getTime() > CREATE_TIME.getTime()) {
            return ACCESS_TOKEN;
        }

        MediaType mediaType = MediaType.parse("application/json");
        String url = "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET;
        RequestBody body = RequestBody.create(mediaType, "");
        Request.Builder request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json");
        client.newCall(request.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                SLog.i(TAG, "[getToken] onFailure");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                SLog.i(TAG, response.body().toString());
                if (response != null && response.body() != null) {
                    String originJson = response.body().string(); // 缓冲区读取
                    SLog.i(TAG, "[getToken] onResponse body: " + originJson);
                    // ResponseToken responseToken = new Gson().fromJson(originJson, ResponseToken.class);
                    // String token = responseToken.getAccessToken();
                    // String refreshToken = responseToken.getRefresh_token();
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(originJson);
                        String token = jsonObject.getString("access_token");
                        String refreshToken = jsonObject.getString("refresh_token");
                        if (TextUtils.isEmpty(token) || TextUtils.isEmpty(refreshToken)) {
                            SLog.e(TAG, "token || refreshToken is null");
                            return;
                        }
                        ACCESS_TOKEN = token;
                        REFRESH_TOKEN = refreshToken;
                        CREATE_TIME = new Date();
                        EXPIRATION_TIME = new Date(Long.parseLong(jsonObject.getString("expires_in")) + CREATE_TIME.getTime());
                        countDownLatch.countDown();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        try {
            countDownLatch.await();  // 等待获取token
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return ACCESS_TOKEN;
    }


    /**
     * 关键词提取 https://cloud.baidu.com/doc/NLP/s/rl9zkamiq
     * 在线调试  https://console.bce.baidu.com/tools/#/api?product=AI&project=%E8%87%AA%E7%84%B6%E8%AF%AD%E8%A8%80%E5%A4%84%E7%90%86&parent=%E9%89%B4%E6%9D%83%E8%AE%A4%E8%AF%81%E6%9C%BA%E5%88%B6&api=oauth%2F2.0%2Ftoken&method=post
     *
     * @param message
     * @param callback
     */
    public static void callKeyWords(String message, IResponse callback) {
        MediaType mediaType = MediaType.parse("application/json");

        TagRequest tagRequest = new TagRequest();
        List<String> texts = new ArrayList<>();
        texts.add("你好，我应该如何学习操作系统呢");
        texts.add("你好，我应该如何学习计算机网络呢");
        tagRequest.setNum(2);
        tagRequest.setText(texts);

        // 创建消息部分的JSON对象
        // JsonObject msg = new JsonObject();
        // msg.addProperty("role", "user");
        // msg.addProperty("content", message);
        //
        // // 将消息放入JSON数组中
        // JsonArray msgArray = new JsonArray();
        // msgArray.add(msg);
        //
        // // 创建最外层的JSON对象并添加属性
        // JsonObject root = new JsonObject();
        // root.add("messages", msgArray);
        // root.addProperty("disable_search", false);
        // root.addProperty("enable_citation", false);
        //
        // // 将JSON对象转换为字符串
        // String jsonString = root.toString();
        // SLog.i(TAG, "callYiYan json: " + jsonString);
        // RequestBody body = RequestBody.create(mediaType, jsonString);

        String bodyString = new Gson().toJson(tagRequest);
        RequestBody body = RequestBody.create(mediaType, bodyString);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rpc/2.0/nlp/v1/txt_keywords_extraction?access_token=" + getAccessToken())
                // .url("https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions?access_token=" + getAccessToken())
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.body() != null) {
                    SLog.i(TAG, "onResponse : " + response.toString());
                    String originJson = response.body().string(); // response.body().string(）仅可以执行一次
                    SLog.i(TAG, "onResponse body: " + originJson);
                    if (callback != null) {
                        callback.onSuccess(originJson);
                    }
                }
            }
        });
    }
}
