package com.shangyizhou.develop.net;

import androidx.annotation.NonNull;

import com.shangyizhou.develop.AppHolder;
import com.shangyizhou.develop.log.SLog;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
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
