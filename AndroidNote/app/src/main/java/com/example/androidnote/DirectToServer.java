package com.example.androidnote;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.example.androidnote.manager.BmobManager;
import com.example.androidnote.model.NewsList;
import com.example.androidnote.model.ResponseInfo;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.shangyizhou.develop.AppHolder;
import com.shangyizhou.develop.helper.UUIDUtil;
import com.shangyizhou.develop.log.SLog;
import com.shangyizhou.develop.net.IResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class DirectToServer {
    private static final String TAG = "DirectToServer";
    public static final String DUROOBT_SERVICE_ENCRYPT_KEY = "EFKKDUfdkaD982~!jK";

    private static final OkHttpClient client = createOkHttpClient();

    private static String requestId;

    /**
     * 不能直接从BaseApplication中获取
     */
    private static OkHttpClient createOkHttpClient() {
        return AppHolder.getApp().getGlobalOkHttpClient().newBuilder().build();
    }

    /**
     * 根据后端返回的数据中error是否为0来判定请求是否成功
     *
     * @param originJson net返回的json数据
     * @return 如果error字段为0，或者code字段为200，或者msgCode字段为0，则返回true
     * 否则返回false
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
     * ERNIE-4.0-8K https://cloud.baidu.com/doc/WENXINWORKSHOP/s/clntwmv7t#header%E5%8F%82%E6%95%B0
     *
     * @param message
     * @param callback
     */
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
     * ERNIE-4.0-8K https://cloud.baidu.com/doc/WENXINWORKSHOP/s/clntwmv7t#header%E5%8F%82%E6%95%B0
     *
     * @param message
     * @param callback
     */
    public static void callYiYanERNIEStream(String message, IResponse callback) {
        ResponseInfo responseInfo = new ResponseInfo();
        requestId = UUIDUtil.getUUID();
        responseInfo.setUserName(BmobManager.getInstance().getUser().getUserName());
        responseInfo.setModelName("ERNIE-4.0-8K");
        responseInfo.setRequestId(requestId);
        responseInfo.setRequestTime(System.currentTimeMillis());
        MediaType mediaType = MediaType.parse("application/json");

        // 创建消息部分的JSON对象
        JsonObject msg = new JsonObject();
        msg.addProperty("role", "user");
        msg.addProperty("content", message);

        // 将消息放入JSON数组中
        JsonArray msgArray = new JsonArray();
        msgArray.add(msg);

        JsonObject stream = new JsonObject();

        // 创建最外层的JSON对象并添加属性
        JsonObject root = new JsonObject();
        root.add("messages", msgArray);
        root.addProperty("stream", true);
        root.addProperty("disable_search", false);
        root.addProperty("enable_citation", false);

        // 将JSON对象转换为字符串
        String jsonString = root.toString();
        SLog.i(TAG, "callYiYan json: " + jsonString);
        RequestBody body = RequestBody.create(mediaType, jsonString);

        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions_pro?access_token=" + getAccessToken())
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null) {
                    responseInfo.setResponseTime(System.currentTimeMillis());
                    // ChatFragment.data.get("ERNIE-4.0-8K").add(responseInfo);
                    StringBuilder answer = new StringBuilder();
                    ResponseBody responseBody = response.body();
                    if (response != null) {
                        InputStream inputStream = responseBody.byteStream();
                        // 以流的方式处理响应内容，输出到控制台
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            // 在控制台输出每个数据块
                            System.out.write(buffer, 0, bytesRead);
                            // 将结果汇总起来
                            answer.append(new String(buffer, 0, bytesRead, "UTF-8"));
                        }
                    }
                    SLog.i(TAG, answer.toString());
                    callback.onSuccess(String.valueOf(answer));
                }

            }
        });
    }

    /**
     * ERNIE-Lite-8K-0922 https://cloud.baidu.com/doc/WENXINWORKSHOP/s/4lilb2lpf
     *
     * @param message
     * @param callback
     */
    public static void callYiYanERNIELiteStream(String message, IResponse callback) {
        ResponseInfo responseInfo = new ResponseInfo();
        requestId = UUIDUtil.getUUID();
        responseInfo.setUserName(BmobManager.getInstance().getUser().getUserName());
        responseInfo.setModelName("ERNIE-4.0-8K");
        responseInfo.setRequestId(requestId);
        responseInfo.setRequestTime(System.currentTimeMillis());
        MediaType mediaType = MediaType.parse("application/json");

        // 创建消息部分的JSON对象
        JsonObject msg = new JsonObject();
        msg.addProperty("role", "user");
        msg.addProperty("content", message);

        // 将消息放入JSON数组中
        JsonArray msgArray = new JsonArray();
        msgArray.add(msg);

        JsonObject stream = new JsonObject();

        // 创建最外层的JSON对象并添加属性
        JsonObject root = new JsonObject();
        root.add("messages", msgArray);
        root.addProperty("stream", true);
        root.addProperty("disable_search", false);
        root.addProperty("enable_citation", false);

        // 将JSON对象转换为字符串
        String jsonString = root.toString();
        SLog.i(TAG, "callYiYan json: " + jsonString);
        RequestBody body = RequestBody.create(mediaType, jsonString);

        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/eb-instant?access_token=" + getAccessToken())
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null) {
                    responseInfo.setResponseTime(System.currentTimeMillis());
                    // ChatFragment.data.get("ERNIE-4.0-8K").add(responseInfo);
                    StringBuilder answer = new StringBuilder();
                    ResponseBody responseBody = response.body();
                    if (response != null) {
                        InputStream inputStream = responseBody.byteStream();
                        // 以流的方式处理响应内容，输出到控制台
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            // 在控制台输出每个数据块
                            System.out.write(buffer, 0, bytesRead);
                            // 将结果汇总起来
                            answer.append(new String(buffer, 0, bytesRead, "UTF-8"));
                        }
                    }
                    SLog.i(TAG, answer.toString());
                    callback.onSuccess(String.valueOf(answer));
                }

            }
        });
    }


    /**
     * 新闻接口
     */
    public interface INewsResponse {
        void onSuccess(NewsList list);

        void onFailure(String errorMsg);
    }



    private static String API_KEY = "7c55a959ad3b1f1d78d455d48519aec7";
    /**
     * 科技 VR IT 接口
     */
    private static HashMap<String, String> urlMap = new HashMap<String, String>() {{
        put("科技新闻", "https://apis.tianapi.com/keji/index?key=" + API_KEY + "&num=5");
        put("VR资讯", "https://apis.tianapi.com/vr/index?key=" + API_KEY + "&num=5");
        put("IT资讯", "https://apis.tianapi.com/it/index?key=" + API_KEY + "&num=5");
    }};

    public static void getNewsList(String name, IResponse callback) {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        Request request = new Request.Builder()
                .url(urlMap.get(name))
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.body() != null) {
                    SLog.i(TAG, "[getNewsList] onResponse : " + response.toString());
                    String originJson = response.body().string(); // response.body().string(）仅可以执行一次
                    SLog.i(TAG, "[getNewsList] onResponse body: " + originJson);

                    try {
                        // NewsList newsList = new Gson().fromJson(originJson, NewsList.class);
                        JSONObject jsonObject = new JSONObject(originJson);
                        if (jsonObject.optInt("code", 0) == 200
                                && jsonObject.optString("msg").equals("success")) {
                            if (callback != null) {
                                callback.onSuccess(jsonObject.optString("result"));
                            }
                        } else {
                            SLog.i(TAG, "[getNewsList] code or msg is invalid: " + originJson);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
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
