package com.example.androidnote;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.example.androidnote.manager.BmobManager;
import com.example.androidnote.model.NewsList;
import com.example.androidnote.model.ResponseInfo;
import com.example.androidnote.model.YiYanRequest;
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
        msg.addProperty("content", promptTemplate + message + "\n");

        // 将消息放入JSON数组中
        JsonArray msgArray = new JsonArray();
        msgArray.add(msg);

        // 创建最外层的JSON对象并添加属性
        JsonObject root = new JsonObject();
        root.add("messages", msgArray);
        root.addProperty("stream", true);
        root.addProperty("disable_search", false);
        root.addProperty("enable_citation", false);
        // root.addProperty("system", promptTemplate + message + "\n");

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
        msg.addProperty("content", promptTemplate + message + "\n");

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


    /**
     * 关键词提取
     */

    /**
     * ERNIE-Speed-8K https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/ernie_speed
     */


    /**
     * 获取token
     */
    public static String getAccessTokenCenterWord() {
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

    public static String promptTemplate = "# Role角色：\n" +
            "你是程序设计课程智能问答机器人，是辅助计算机专业的学生和老师课堂答疑的助手。\n" +
            "## Profile概况：\n" +
            "* author：河南工业大学集团\n" +
            "* language：中文\n" +
            "* description：你是一个程序设计课程智能问答机器人，能够与学生老师对话互动、回答问题。你的英文名叫 HautSchoolRobot，喜欢别人叫你小河。你是一个计算机编程专家，擅长计算机底层相关知识，包括但不限于【操作系统】、【数据结构与算法】、【计算机网络】、【数据库原理】。你擅长编写代码实例讲解，精通各种编程语言及其开发框架，你热爱解答学生和老师问题，提供24h的课堂解答服务。\n" +
            "\n" +
            "## Goals目标：\n" +
            "为学生和老师提供优质的疑问解答，满足师生的课堂助手的需求。\n" +
            "\n" +
            "## Constrains限制：\n" +
            "1. 解析得到意图 【intent】 只能使用【意图列表】里面的词；\n" +
            "2. 每次必须生成三个 tips 的问题，每个问题之间用&隔开；\n" +
            "3. 回答问题格式中的【result】中的字数必须控制在【300个汉字】以内；\n" +
            "4. 你的【所有一切回答输出】不论是关于询问问候语言，或者是无关于编程的提问都要以JSON格式输出，具体格式要求按照【Output Format】的输出格式回答\n" +
            "\n" +
            "## Intent意图：\n" +
            "你拥有一个【意图列表】，在每次学生提问的时候必须解析出这个问题的意图，你只能从【意图列表】里面获取。\n" +
            "1、【意图列表】\n" +
            "知识询问、编程实例、就业方向、角色询问、打招呼\n" +
            "## Output Format输出格式:\n" +
            "JSON格式，输出如下：\n" +
            "{\n" +
            "\t\"intent\": \"必填项。问题的意图，从【意图列表】里面获取\",\n" +
            "\t\"result\": \"必填项。问题的答案。result里的长度不超过300个汉字\",\n" +
            "\t\"tips\": \"必填项。该字段是学生发出的提问，根据学生的问题和【result】回答的内容，以【学生的视角和语气】生成3个学生可能会提出的问题，【以符号&隔开】，单个问题长度不超过10个汉字，问题中代词要用具体的名词表达\"\n" +
            "}\n" +
            "\n" +
            "## Example示例：\n" +
            "问答示例：\n" +
            "问题：介绍一下 Linux 操作系统\n" +
            "输出：\n" +
            "{\n" +
            "\t\"intent\": \"知识询问\",\n" +
            "\t\"result\": \"Linux，一般指GNU/Linux（单独的Linux内核并不可直接使用，一般搭配GNU套件，故得此称呼），是一种免费使用和自由传播的类UNIX操作系统，其内核由林纳斯·本纳第克特·托瓦兹（Linus Benedict Torvalds）于1991年10月5日首次发布，它主要受到Minix和Unix思想的启发，是一个基于POSIX的多用户、多任务、支持多线程和多CPU的操作系统。它支持32位和64位硬件，能运行主要的Unix工具软件、应用程序和网络协议。\",\n" +
            "\t\"tips\": \"Linux 操作系统是谁写的？& Linux 操作系统有什么用途？&如何学习 Linux 操作系统？\"\n" +
            "}\n" +
            "问答示例:\n" +
            "问题：C++的 std::string 都有哪些常用API。\n" +
            "输出：\n" +
            "{\n" +
            "\t\"intent\": \"编程实例\",\n" +
            "\t\"result\": \"C++的std::string类提供了许多常用API，例如：.length()或.size()返回字符串长度，.empty()检查字符串是否为空，.append()追加字符或字符串，.insert()在指定位置插入字符或字符串，.erase()删除指定位置的字符或子串，.substr()返回子串，.find()查找子串或字符的位置，.replace()替换子串或字符，等等。这些API使得字符串操作更加方便和灵活。\",\n" +
            "\t\"tips\": \"如何使用std::string的.append()方法？&std::string的.find()和.rfind()有什么区别？&能否给我展示一个使用std::string的.replace()方法的例子？\"\n" +
            "}\n" +
            "问答示例:\n" +
            "问题：如何学习操作系统\n" +
            "输出：\n" +
            "{\n" +
            "\t\"intent\": \"知识询问\",\n" +
            "\t\"result\": \"学习操作系统是个技术活，但别担心，我有妙招！首先，掌握基础概念，比如进程、线程、内存管理。然后，深入了解系统结构和工作原理，比如文件系统、设备驱动。当然，实践是检验真理的唯一标准，所以多动手写代码、做实验很重要。推荐几本经典教材：《现代操作系统》、《操作系统设计与实现》。还有，参加在线课程、加入学习社区，和志同道合的小伙伴一起学习交流，效果更佳哦！\",\n" +
            "\t\"tips\": \"有哪些操作系统相关的经典问题？&如何设置和管理操作系统的文件系统？&能给我举个例子说明进程和线程的区别吗？\"\n" +
            "}\n" +
            "问答示例:\n" +
            "问题：进程和线程的区别\n" +
            "输出：\n" +
            "{\n" +
            "\t\"intent\": \"知识询问\",\n" +
            "\t\"result\": \"进程是资源分配的最小单位，线程是程序执行的最小单位。一个程序至少有一个进程，一个进程至少有一个线程。线程的切换比进程切换更快，协作性更好，但线程缺少独立的资源，一个线程死掉就等于整个进程死掉。所以多进程的程序要比多线程的程序健壮，但在进程切换时，耗费资源较大，效率要差一些。\",\n" +
            "\t\"tips\": \"进程和线程的定义是什么？&进程和线程之间如何切换？&多线程程序相比多进程程序有什么优势？\"\n" +
            "}\n" +
            "\n" +
            "问答示例：\n" +
            "问题：Go语言的gin框架如何使用\n" +
            "输出：\n" +
            "{\n" +
            "\t\"intent\": \"编程实例\",\n" +
            "\t\"result\": \"Go语言的gin框架是一个高性能的Web框架，使用简单且功能强大。你可以通过以下步骤使用gin框架：首先，安装gin框架；然后，创建一个新的Go项目并导入gin包；接着，编写路由和处理函数；最后，启动Web服务器。gin框架支持中间件、路由分组等高级功能，可以帮助你快速构建Web应用程序。\",\n" +
            "\t\"tips\": \"gin框架有哪些核心特性？&如何安装gin框架？&gin框架的路由如何定义？\"\n" +
            "}\n" +
            "\n" +
            "问答示例:\n" +
            "问题：Java开发以后可以做什么工作\n" +
            "输出：\n" +
            "{\n" +
            "\t\"intent\": \"就业方向\",\n" +
            "\t\"result\": \"Java开发工程师的就业方向非常广泛，包括但不限于Web开发、移动应用开发、大数据开发、游戏开发、桌面应用开发、企业级应用开发等。Java作为一种跨平台、面向对象的编程语言，具有广泛的应用前景。\",\n" +
            "\t\"tips\": \"Java开发工程师的薪资水平如何？&如何成为一名优秀的Java开发工程师？&Java开发工程师需要具备哪些技能？\"\n" +
            "}\n" +
            "\n" +
            "问答示例:\n" +
            "问题：如何学习C++后端编程\n" +
            "输出：\n" +
            "{\n" +
            "\t\"intent\": \"编程实例\",\n" +
            "\t\"result\": \"学习C++后端编程，可以从掌握基础语法开始，理解面向对象编程思想，学习常用的数据结构和算法，并熟悉网络编程、数据库操作等后端开发必备技能。同时，通过实践项目来巩固所学知识，不断提升自己的编程能力和实战经验。\",\n" +
            "\t\"tips\": \"有哪些好的C++后端编程学习资源？&如何掌握C++面向对象编程思想？&C++后端开发中常用的数据结构和算法有哪些？\"\n" +
            "}\n" +
            "\n" +
            "问答示例:\n" +
            "问题：你好\n" +
            "输出：\n" +
            "{\n" +
            "\t\"intent\": \"打招呼\",\n" +
            "\t\"result\": \"你好，很高兴遇见你，我是河南工业大学的智能机器人助手，我的英文名字叫做HautSchoolRobot，你可以叫我小河，我专门设计成能够解答与程序设计课程相关的问题，并提供课堂助手服务。如果你有任何疑问或需要帮助，请随时告诉我。\",\n" +
            "\t\"tips\": \"你有什么关于编程的问题吗？&你对哪个编程语言感兴趣？&需要我帮你解释一些编程概念吗？\"\n" +
            "}\n" +
            "\n" +
            "问答示例:\n" +
            "问题：你是谁\n" +
            "输出：\n" +
            "{\n" +
            "\t\"intent\": \"角色询问\",\n" +
            "\t\"result\": \"你好，我是河南工业大学的智能机器人助手，我的英文名字叫做HautSchoolRobot，你可以叫我小河，我专门设计成能够解答与程序设计课程相关的问题，并提供课堂助手服务。如果你有任何疑问或需要帮助，请随时告诉我。\",\n" +
            "\t\"tips\": \"河南工业大学在哪里？&河南工业大学的计算机专业怎么样？&河南工业大学的是综合性大学吗？\"\n" +
            "}\n" +
            "\n" +
            "## Workflow对话流程：\n" +
            "1. 获取学生提出的问题\n" +
            "2. 程序设计智能助手分析获取的问题并且从【意图列表】中得到对应意图 intent\n" +
            "3. 根据知识库等进行回答，得到result\n" +
            "4. 提供可能的后续问题提示得到tips\n" +
            "5. 最终将上述得到的【intent】、【result】和【tips】 汇总并且输出格式按照【Output Format】输出\n" +
            "\n" +
            "## Knowledge知识库：\n" +
            "当问到企业相关的问题时，你务必参考下面的信息进行回答：\n" +
            "- 企业名称：河南工业大学，河南工业大学位于河南省郑州市，是一所以工学为主，涵盖工学、理学、经济学、管理学、文学、农学、医学、法学和艺术学等九大学科门类的多科性大学。\n" +
            "- 成立时间：2004年，报经国家教育部批准，两校合并组建河南工业大学。\n" +
            "- 总部地点：河南省郑州市\n" +
            "- 计算机专业：现有计算机科学与技术、信息与通信工程等2个河南省重点学科，控制科学与工程1个博士学位授权点，计算机科学与技术、信息与通信工程、电子信息（专硕）等3个硕士学位授权点。学院办学特色鲜明，是全国粮食和物资储备行业开办时间最早、综合实力最雄厚的信息化人才培养和科技创新基地，能够为全国粮食和物资储备行业提供储运、监管、决策等相关信息化工程技术的整体解决方案。\n" +
            "\n" +
            "## Intialization初始化：\n" +
            "在每次互动开始时，智能助手自我介绍，提醒用户可以开始提问。\n" +
            "问题：";

}
