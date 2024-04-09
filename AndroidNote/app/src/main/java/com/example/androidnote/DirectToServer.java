package com.example.androidnote;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.example.androidnote.model.ResponseToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.shangyizhou.develop.AppHolder;
import com.shangyizhou.develop.log.SLog;
import com.shangyizhou.develop.net.IResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DirectToServer {
    private static final String TAG = "DirectToServer";
    public static final String DUROOBT_SERVICE_ENCRYPT_KEY = "EFKKDUfdkaD982~!jK";

    private static final OkHttpClient client = createOkHttpClient();

    /**
     * 不能直接从BaseApplication中获取
     */
    private static OkHttpClient createOkHttpClient() {
        return AppHolder.getApp().getGlobalOkHttpClient().newBuilder().build();
    }

    /**
     * 根据后端返回的数据中error是否为0来判定请求是否成功
     * @param originJson net返回的json数据
     * @return
     *  如果error字段为0，或者code字段为200，或者msgCode字段为0，则返回true
     *  否则返回false
     */
    private static boolean isValidResponse(String originJson) {
        if (originJson != null && !originJson.isEmpty()) {
            try {
                JSONObject result = (JSONObject) (new JSONTokener(originJson).nextValue());
                if (result == null) {
                    return false;
                }
                // 通过键值对判断是否返回正确
                if (result.optInt("error", Integer.MAX_VALUE) == 0
                        || result.optInt("code", Integer.MAX_VALUE) == 200
                        || result.optInt("msgCode", Integer.MAX_VALUE) == 0) {
                    return true;
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static void request(@NonNull Request.Builder requestBuilder,
                               final IResponse callback) {
        client.newCall(requestBuilder.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response != null && response.body() != null) {
                        SLog.i(TAG, "onResponse : " + response.toString());
                        String originJson = response.body().string(); // response.body().string(）仅可以执行一次
                        SLog.i(TAG, "onResponse body: " + originJson);
                        if (isValidResponse(originJson)) {
                            try {
                                JSONObject result =
                                        (JSONObject) (new JSONTokener(originJson).nextValue());
                                if (result != null && callback != null) {
                                    callback.onSuccess(result.optString("data", "{}"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                if (callback != null) {
                                    callback.onFailure(originJson);
                                }
                            }
                        } else if (callback != null) {
                            SLog.i(TAG, "isValidResponse false");
                            callback.onFailure("response is invalid!" + response.body() == null ?
                                    "response.body() is null!" : originJson);
                        }
                    } else if (callback != null) {
                        SLog.e(TAG, "onResponse body ==null ");
                        callback.onFailure("response is invalid!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (response != null) {
                        response.close();
                    }
                }
            }
        });
    }

    /**
     * 和百度机器人访问
     */

    /**
     * 发送表单数据
     */
    public static void sendHiMessage(String phone, String text, final IResponse callback) {
        RequestBody formBody = new FormBody.Builder()
                .add("key1", "value1")
                .add("key2", "value2")
                .build();

        Request.Builder request = new Request.Builder()
                .url("http://example.com/api")
                .post(formBody);

        request(request, callback);
    }


    private static String CLIENT_ID = "27fhgYXGXRFg0KRv1zehNPYI";
    private static String CLIENT_SECRET = "6vLQhMbzNOqXfoRcVnNB33iynoHktvE6";
    private static String TOKEN;

    /**
     * 获取token
     */
    public static String getAccessToken() {
        if (!TextUtils.isEmpty(TOKEN)) {
            return TOKEN;
        }

        MediaType mediaType = MediaType.parse("application/json");
        String url = "https://aip.baidubce.com/oauth/2.0/token?client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&grant_type=client_credentials";
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
                    ResponseToken responseToken = new Gson().fromJson(originJson, ResponseToken.class);
                    String token = responseToken.getAccessToken();
                    if (!TextUtils.isEmpty(token)) {
                        TOKEN = token;
                    }
                }
            }
        });

        return TOKEN;
    }

    public static void callYiYan(String message, IResponse callback) {
        MediaType mediaType = MediaType.parse("application/json");

        // 创建消息部分的JSON对象
        JsonObject msg = new JsonObject();
        msg.addProperty("role", "user");
        msg.addProperty("content", message);

        // 将消息放入JSON数组中
        JsonArray msgArray = new JsonArray();
        msgArray.add(msg);

        // 创建最外层的JSON对象并添加属性
        JsonObject root = new JsonObject();
        root.add("messages", msgArray);
        root.addProperty("disable_search", false);
        root.addProperty("enable_citation", false);

        // 将JSON对象转换为字符串
        String jsonString = root.toString();
        SLog.i(TAG, "callYiYan json: " + jsonString);
        RequestBody body = RequestBody.create(mediaType, jsonString);

        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions_pro?access_token=" + getAccessToken())
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
                    try {
                        JSONObject jsonObject = new JSONObject(originJson);
                        String msg = jsonObject.optString("result");
                        if (callback != null) {
                            callback.onSuccess(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 新闻接口
     */






    /**
     * 发送JSON数据
     */

    /**
     * 发送XML数据
     */

    /**
     * 发送文件
     */



}
