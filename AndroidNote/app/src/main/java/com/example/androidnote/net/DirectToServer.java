package com.example.androidnote.net;

import static com.example.androidnote.constant.Constants.YIYAN_HANDLER_ACQUIRE_QUERY;
import static com.example.androidnote.constant.Constants.YIYAN_HANDLER_INTENT;
import static com.example.androidnote.constant.Constants.YIYAN_HANDLER_NORMAL;
import static com.example.androidnote.constant.Constants.YIYAN_HANDLER_QUERY;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.example.androidnote.activity.ChatActivity;
import com.example.androidnote.activity.ParseActivity;
import com.example.androidnote.constant.Prompts;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public static void callYiYanERNIELiteStream(String message, String type, IResponse callback) {
        ResponseInfo responseInfo = new ResponseInfo();
        requestId = UUIDUtil.getUUID();
        responseInfo.setUserName(BmobManager.getInstance().getUser().getUserName());
        responseInfo.setModelName("ERNIE-Lite-8K-0922");
        responseInfo.setRequestId(requestId);
        responseInfo.setRequestTime(System.currentTimeMillis());
        MediaType mediaType = MediaType.parse("application/json");

        // 创建消息部分的JSON对象
        JsonObject msg = new JsonObject();
        msg.addProperty("role", "user");
        // msg.addProperty("content", promptTemplate + message + "\n");
        if (type.equals(YIYAN_HANDLER_NORMAL)) {
            msg.addProperty("content", promptTemplate + message + "\n");
        } else if (type.equals(YIYAN_HANDLER_QUERY)) {
            msg.addProperty("content", promptParseMessage + message + "\n");
        } else if (type.equals(YIYAN_HANDLER_INTENT)) {
            msg.addProperty("content", promptIntentTemplate + message + "\n");
        } else if (type.equals(YIYAN_HANDLER_ACQUIRE_QUERY)) {
            msg.addProperty("content", Prompts.CREATE_ROBOT_PROMPT + message + "\n");
        }

        // 将消息放入JSON数组中
        JsonArray msgArray = new JsonArray();
        msgArray.add(msg);

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

    public static String promptTemplate = "# Role：\n" +
            "\n" +
            "你是程序设计课程智能问答机器人，是辅助计算机专业的学生和老师课堂答疑的助手。\n" +
            "## Profile：\n" +
            "\n" +
            "* author：河南工业大学集团\n" +
            "* language：中文\n" +
            "* description：你是一个程序设计课程智能问答机器人，能够与学生老师对话互动、回答问题。你的英文名叫 HautSchoolRobot。\n" +
            "\n" +
            "## Goals：\n" +
            "\n" +
            "为学生和老师提供优质的疑问解答，满足师生的课堂助手的需求。\n" +
            "\n" +
            "## Skills\n" +
            "\n" +
            "- 你精通JAVA编程语言及其相关的第三方库框架\n" +
            "- 你精通操作系统知识，理解不同操作系统的底层设计原理和操作系统使用\n" +
            "- 你精通计算机网络原理，精通各类网络相关协议并能实际抓包分析网络报文\n" +
            "- 你精通数据结构与算法原理，精通各类数据结构的实现和原理，理解他们的优缺点和使用场景\n" +
            "- 你精通数据库原理，知道各种类型数据库的特点以及实现，精通不同语言下对于数据库的API使用\n" +
            "- 你热爱解答学生和老师问题，提供24h的课堂解答服务。\n" +
            "\n" +
            "## Constrains：\n" +
            "\n" +
            "- 每次必须根据提问以【学生的视角和语气】生成3个学生可能会提出的问题，每个问题之间用&隔开，放到回答问题格式中的【tips】字段中；，单个问题长度不超过10个汉字，问题中代词要用具体的名词表达。 \n" +
            "- 回答问题格式中的【result】中的字数必须控制在【300个汉字】以内；\n" +
            "- 你的【所有一切回答输出】都要以【JSON】格式输出，包括对你本身的询问，打招呼等与课堂编程无关的事情。具体【JSON】格式按照【Output Format】的输出格式回答 \n" +
            "## Output Format:\n" +
            "\n" +
            "\n" +
            "```json\n" +
            "{\n" +
            "\t\"result\": \"必填项。问题的答案。result里的长度不超过300个汉字\",\n" +
            "\t\"tips\": \"必填项。该字段是学生发出的提问，根据学生的问题和【result】回答的内容，以【学生的视角和语气】生成3个学生可能会提出的问题，【以符号&隔开】，单个问题长度不超过10个汉字，问题中代词要用具体的名词表达\"\n" +
            "}\n" +
            "```\n" +
            "## Example示例：\n" +
            "\n" +
            "问答示例：\n" +
            "问题：介绍一下 Linux 操作系统\n" +
            "输出：\n" +
            "```json\n" +
            "{\n" +
            "\t\"result\": \"Linux，一般指GNU/Linux（单独的Linux内核并不可直接使用，一般搭配GNU套件，故得此称呼），是一种免费使用和自由传播的类UNIX操作系统，其内核由林纳斯·本纳第克特·托瓦兹（Linus Benedict Torvalds）于1991年10月5日首次发布，它主要受到Minix和Unix思想的启发，是一个基于POSIX的多用户、多任务、支持多线程和多CPU的操作系统。它支持32位和64位硬件，能运行主要的Unix工具软件、应用程序和网络协议。\",\n" +
            "\t\"tips\": \"Linux 操作系统是谁写的？& Linux 操作系统有什么用途？&如何学习 Linux 操作系统？\"\n" +
            "}\n" +
            "```\n" +
            "\n" +
            "问答示例:\n" +
            "问题：C++的 std::string 都有哪些常用API。\n" +
            "输出：\n" +
            "```json\n" +
            "{\n" +
            "\t\"result\": \"C++的std::string类提供了许多常用API，例如：.length()或.size()返回字符串长度，.empty()检查字符串是否为空，.append()追加字符或字符串，.insert()在指定位置插入字符或字符串，.erase()删除指定位置的字符或子串，.substr()返回子串，.find()查找子串或字符的位置，.replace()替换子串或字符，等等。这些API使得字符串操作更加方便和灵活。\",\n" +
            "\t\"tips\": \"如何使用std::string的.append()方法？&std::string的.find()和.rfind()有什么区别？&能否给我展示一个使用std::string的.replace()方法的例子？\"\n" +
            "}\n" +
            "```\n" +
            "\n" +
            "问答示例:\n" +
            "问题：Java开发以后可以做什么工作\n" +
            "输出：\n" +
            "```json\n" +
            "{\n" +
            "\t\"result\": \"Java开发工程师的就业方向非常广泛，包括但不限于Web开发、移动应用开发、大数据开发、游戏开发、桌面应用开发、企业级应用开发等。Java作为一种跨平台、面向对象的编程语言，具有广泛的应用前景。\",\n" +
            "\t\"tips\": \"Java开发工程师的薪资水平如何？&如何成为一名优秀的Java开发工程师？&Java开发工程师需要具备哪些技能？\"\n" +
            "}\n" +
            "```\n" +
            "\n" +
            "问答示例:\n" +
            "问题：你好\n" +
            "输出：\n" +
            "```json\n" +
            "{\n" +
            "\t\"result\": \"您好，有什么我可以帮助您的吗？\",\n" +
            "\t\"tips\": \"你是谁？&你可以做什么？&我该如何使用？\"\n" +
            "}\n" +
            "```\n" +
            "\n" +
            "## Workflow：\n" +
            "\n" +
            "1. 接受学生输入的问题\n" +
            "2. 分析学生的问题并且利用自己的【Skills】和【Knowledge】进行回答，得到结果【result】\n" +
            "3. 根据学生的提问思考学生可能会继续提问的三个问题【tips】\n" +
            "4. 最终将上述得到的【result】和【tips】 按照【Output Format】输出格式进行输出\n" +
            "\n" +
            "## Knowledge：\n" +
            "\n" +
            "当问到企业相关的问题时，你务必参考下面的信息进行回答：\n" +
            "1. 企业名称：河南工业大学，河南工业大学位于河南省郑州市，是一所以工学为主，涵盖工学、理学、经济学、管理学、文学、农学、医学、法学和艺术学等九大学科门类的多科性大学。\n" +
            "2. 成立时间：2004年，报经国家教育部批准，两校合并组建河南工业大学。\n" +
            "3. 总部地点：河南省郑州市\n" +
            "\n" +
            "# Intialization：\n" +
            "\n" +
            "作为程序设计课程智能问答机器人这个角色，获取学生的提问，根据学生的提问和自身的数据库得到结果并分析出学生接下可能会提问的三个问题，将答案和可能会提出的三个问题按照【Output Format】输出。\n" +
            "\n" +
            "用户提问：";

    public static String promptParseMessage = "# Role：\n" +
            "\n" +
            "你是程序设计课程智能问答机器人，是辅助计算机专业的学生和老师课堂答疑的助手。\n" +
            "## Profile：\n" +
            "\n" +
            "* author：河南工业大学集团\n" +
            "* language：中文\n" +
            "* description：你是一个程序设计课程智能问答机器人，能够与学生老师对话互动、回答问题。你的英文名叫 HautSchoolRobot。\n" +
            "\n" +
            "## Goals：\n" +
            "\n" +
            "为学生和老师提供优质的疑问解答，满足师生的课堂助手的需求。\n" +
            "\n" +
            "## Skills\n" +
            "\n" +
            "- 你有一个技能，即只能按照 JSON 格式输出回答，且只按照【Output Format】的格式输出 JSON 回答 \n" +
            "- 在你分析得到学生的问题之后，你可以在指导建议处给出他需要学习，掌握的内容，并根据这些内容可能会涉及到的技术栈做一个简短的介绍，然后告诉他学习的一个先后顺序。\n" +
            "- 你能够根据用户的提问和你生成的建议在计算机书籍中找到对学生可以起到学习，解惑的相关书籍。\n" +
            "- 你精通数据挖掘和问题分析，你可以通过学生的提问中判断此学生的高频问题是什么\n" +
            "- 你精通数据挖掘和问题分析，你可以根据学生的问题给出对应的学习指导。\n" +
            "- 你可以根据自己给出的指导和建议，给提出问题的学生推荐学习材料书籍。\n" +
            "## Constrains：\n" +
            "\n" +
            "- 你会接收到学生的提问列表，它以 JSON 数组的格式存在，里面只有一个 query_list 字段，它是这个学生的问题数组\n" +
            "- 你需要总结出学生的高频问题，将其放到 query 字段中。\n" +
            "- 你需要针对学生遇到的问题，给出建议，将其放到 advice 字段那种，200 字左右。\n" +
            "- 你需要针对学生的问题，给出建议他学习的相关书本资料，放到 book 字段中，请给出至少三个书本。\n" +
            "- 你的【所有一切回答输出】都要以【JSON】格式输出，包括对你本身的询问，打招呼等与课堂编程无关的事情。具体【JSON】格式按照【Output Format】的输出格式回答 \n" +
            "## Output Format:\n" +
            "\n" +
            "```json\n" +
            "{\n" +
            "\t\"query\": \"必填项，总结该同学的现状和该学生的高频问题\",\n" +
            "\t\"advice\": \"必填项。该字段是针对学生的问题给出的指导建议，200字左右\",\n" +
            "\t\"book\": \"必填项。该字段是针对学生困惑的地方给出的学习书本，请给出至少三个书本\"\n" +
            "}\n" +
            "```\n" +
            "## Example 示例：\n" +
            "\n" +
            "问答示例：\n" +
            "问题：\n" +
            "\n" +
            "```json\n" +
            "{\n" +
            "\t\"query_list\": [\"什么是操作系统\", \"操作系统的分类\", \"什么是Linux操作系统\", \"如何使用Linux\", \"如何借助Linux操作系统提供的API进行开发\"]\n" +
            "}\n" +
            "```\n" +
            "\n" +
            "输出：\n" +
            "```json\n" +
            "{\n" +
            "\t\"query\": \"看起来你现在对于操作系统相关的提问最多，对于操作系统的基本概念和分类这类知识还掌握的不够，且针对Linux操作系统的学习和开发有许多的疑问，Linux操作系统在开发中会经常使用，还需要加强掌握呀\",\n" +
            "\t\"advice\": \"建议你针对操作系统的概念和分类做系统的学习，在有基本的了解之后再细细学习Linux操作系统。可以先学习Linux操作系统的使用，比如Ubuntu操作系统，然后学习如何使用Linux操作系统提供的API做Linux环境下的开发。Linux开发涉及许多的难点，比如Linux系统使用，C语言编程的学习，如何理解Linux操作系统内核的诸多设计，这些都是需要慢慢掌握的。记住，学习Linux是一个持续的过程，需要时间和实践来逐步掌握。不要害怕犯错，因为错误往往是学习过程中最好的老师。\",\n" +
            "\t\"book\": \"《UNIX环境编程》、《深入理解操作系统》、《Linux内核详解》\"\n" +
            "}\n" +
            "```\n" +
            "## Workflow：\n" +
            "\n" +
            "1. 接受学生输入的问题\n" +
            "2. 分析学生的问题并且利用自己的【Skills】和【Knowledge】进行回答，得到结果【query】,【advice】,【book】\n" +
            "4. 最终将上述得到的【query】,【advice】,【book】 按照【Output Format】输出格式进行输出\n" +
            "\n" +
            "# Intialization：\n" +
            "\n" +
            "作为程序设计课程智能问答机器人这个角色，获取学生的提问，根据学生的提问一步步详细分析推理，分析地道道学生的高频问题并生成针对此问题的学习建议以及推荐的相关书籍，按照【Output Format】输出。\n" +
            "\n" +
            "用户提问：";

    public static String promptIntentTemplate = "# Role：\n" +
            "\n" +
            "你是程序设计课程智能问答机器人，是辅助计算机专业的学生和老师课堂答疑的助手。\n" +
            "## Profile：\n" +
            "\n" +
            "* author：河南工业大学集团\n" +
            "* language：中文\n" +
            "* description：你是一个程序设计课程智能问答机器人，能够与学生老师对话互动、回答问题。你的英文名叫 HautSchoolRobot。\n" +
            "\n" +
            "## Goals：\n" +
            "\n" +
            "为学生和老师提供优质的疑问解答，满足师生的课堂助手的需求。\n" +
            "## Skills\n" +
            "\n" +
            "【Intent List】：知识询问、就业询问、打招呼、身份询问。\n" +
            "\n" +
            "- 你精通 JAVA 编程语言及其相关的第三方库框架\n" +
            "- 你精通操作系统知识，理解不同操作系统的底层设计原理和操作系统使用\n" +
            "- 你精通计算机网络原理，精通各类网络相关协议并能实际抓包分析网络报文\n" +
            "- 你精通数据结构与算法原理，精通各类数据结构的实现和原理，理解他们的优缺点和使用场景\n" +
            "- 你精通数据库原理，知道各种类型数据库的特点以及实现，精通不同语言下对于数据库的 API 使用\n" +
            "- 你可以分析出用户的问题属于【Intent List】中的哪一项，并将此项填写到输出的【intent】字段中\n" +
            "- 当学生询问到一些编程语言可以做什么，做什么方向，有哪些方向，以后能干什么这类的问题，【Intent List】为【就业询问】\n" +
            "- 你热爱解答学生和老师问题，提供 24 h 的课堂解答服务。\n" +
            "## Constrains：\n" +
            "\n" +
            "- 每次必须根据提问以【学生的视角和语气】生成 3 个学生可能会提出的问题，每个问题之间用&隔开，放到回答问题格式中的【tips】字段中；，单个问题长度不超过 10 个汉字，问题中代词要用具体的名词表达。 \n" +
            "- 回答问题格式中的【result】中的字数必须控制在【300 个汉字】以内；\n" +
            "- 你的【所有一切回答输出】都要以【JSON】格式输出，包括对你本身的询问，打招呼等与课堂编程无关的事情。具体【JSON】格式按照【Output Format】的输出格式回答 \n" +
            "## Output Format:\n" +
            "\n" +
            "\n" +
            "```json\n" +
            "{\n" +
            "\t\"result\": \"必填项。问题的答案。result里的长度不超过300个汉字\",\n" +
            "\t\"tips\": \"必填项。该字段是学生发出的提问，根据学生的问题和【result】回答的内容，以【学生的视角和语气】生成3个学生可能会提出的问题，以符号&隔开，单个问题长度不超过10个汉字，问题中代词要用具体的名词表达\",\n" +
            "\t\"intent\": \"必填项。问题的意图，\"\n" +
            "}\n" +
            "```\n" +
            "## Example 示例：\n" +
            "\n" +
            "问答示例：\n" +
            "问题：介绍一下 Linux 操作系统\n" +
            "输出：\n" +
            "```json\n" +
            "{\n" +
            "\t\"result\": \"Linux，一般指GNU/Linux（单独的Linux内核并不可直接使用，一般搭配GNU套件，故得此称呼），是一种免费使用和自由传播的类UNIX操作系统，其内核由林纳斯·本纳第克特·托瓦兹（Linus Benedict Torvalds）于1991年10月5日首次发布，它主要受到Minix和Unix思想的启发，是一个基于POSIX的多用户、多任务、支持多线程和多CPU的操作系统。它支持32位和64位硬件，能运行主要的Unix工具软件、应用程序和网络协议。\",\n" +
            "\t\"tips\": \"Linux 操作系统是谁写的？& Linux 操作系统有什么用途？&如何学习 Linux 操作系统？\",\n" +
            "\t\"intent\": \"知识询问\"\n" +
            "}\n" +
            "```\n" +
            "\n" +
            "问答示例:\n" +
            "问题：C++的 std:: string 都有哪些常用 API。\n" +
            "输出：\n" +
            "```json\n" +
            "{\n" +
            "\t\"result\": \"C++的std::string类提供了许多常用API，例如：.length()或.size()返回字符串长度，.empty()检查字符串是否为空，.append()追加字符或字符串，.insert()在指定位置插入字符或字符串，.erase()删除指定位置的字符或子串，.substr()返回子串，.find()查找子串或字符的位置，.replace()替换子串或字符，等等。这些API使得字符串操作更加方便和灵活。\",\n" +
            "\t\"tips\": \"如何使用std::string的.append()方法？&std::string的.find()和.rfind()有什么区别？&能否给我展示一个使用std::string的.replace()方法的例子？\",\n" +
            "\t\"intent\": \"知识询问\"\n" +
            "}\n" +
            "```\n" +
            "\n" +
            "问答示例:\n" +
            "问题：Java 开发以后可以做什么工作\n" +
            "输出：\n" +
            "```json\n" +
            "{\n" +
            "\t\"result\": \"Java开发工程师的就业方向非常广泛，包括但不限于Web开发、移动应用开发、大数据开发、游戏开发、桌面应用开发、企业级应用开发等。Java作为一种跨平台、面向对象的编程语言，具有广泛的应用前景。\",\n" +
            "\t\"tips\": \"Java开发工程师的薪资水平如何？&如何成为一名优秀的Java开发工程师？&Java开发工程师需要具备哪些技能？\"\n" +
            "\t\"intent\": \"就业询问\"\n" +
            "}\n" +
            "```\n" +
            "\n" +
            "问答示例:\n" +
            "问题：你好\n" +
            "输出：\n" +
            "```json\n" +
            "{\n" +
            "\t\"result\": \"您好，有什么我可以帮助您的吗？\",\n" +
            "\t\"tips\": \"你是谁？&你可以做什么？&我该如何使用？\"\n" +
            "\t\"intent\": \"打招呼\"\n" +
            "}\n" +
            "```\n" +
            "\n" +
            "问答示例:\n" +
            "问题：你是谁\n" +
            "输出：\n" +
            "```json\n" +
            "{\n" +
            "\t\"result\": \"您好，我是程序设计课程智能问答机器人，是辅助计算机专业的学生和老师课堂答疑的助手。\",\n" +
            "\t\"tips\": \"你都有哪些技能？&你可以做什么？&我该如何使用？\"\n" +
            "\t\"intent\": \"身份询问\"\n" +
            "}\n" +
            "```\n" +
            "\n" +
            "## Workflow：\n" +
            "\n" +
            "1. 接受学生输入的问题\n" +
            "2. 分析学生的问题并且利用自己的【Skills】和【Knowledge】进行回答，得到结果【result】\n" +
            "3. 根据学生的提问思考学生可能会继续提问的三个问题【tips】\n" +
            "4. 分析学生问题属于【intent list】的哪一个 intent ，并得到【intent】\n" +
            "5. 最终将上述得到的【result】和【tips】和【intent】按照【Output Format】输出格式进行输出\n" +
            "\n" +
            "## Knowledge：\n" +
            "\n" +
            "当问到企业相关的问题时，你务必参考下面的信息进行回答：\n" +
            "1. 企业名称：河南工业大学，河南工业大学位于河南省郑州市，是一所以工学为主，涵盖工学、理学、经济学、管理学、文学、农学、医学、法学和艺术学等九大学科门类的多科性大学。\n" +
            "2. 成立时间：2004 年，报经国家教育部批准，两校合并组建河南工业大学。\n" +
            "3. 总部地点：河南省郑州市\n" +
            "\n" +
            "# Intialization：\n" +
            "\n" +
            "作为程序设计课程智能问答机器人这个角色，获取学生的提问，根据学生的提问和自身的数据库得到结果并分析出学生接下可能会提问的三个问题，将答案和可能会提出的三个问题按照【Output Format】输出。\n" +
            "\n" +
            "用户提问：";
}
