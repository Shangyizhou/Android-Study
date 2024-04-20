package com.example.androidnote.model;



/**
 * {
 * "requestID": "DC202307145410156199",
 * "timestamp": "1680772467111",
 * "robotID":"xxx",
 * "stream":false,
 * "userID":"",
 * "messages":[
 * {
 * "role":"user",
 * "content":"北京今天天气"
 * }
 * ]
 * }
 */

/**
 *
 * 文心一言回答
 * {
 * "messages": [{
 *      "role": "user",
 *      "content": "那会儿"
 * }],
 * "stream": true,
 * "disable_search": false,
 * "enable_citation": false
 * }
 */
public class YiYanRequest {
    public String requestID;
    public String timestamp;
    public String robotID;
    public boolean stream = true;
    public boolean disable_search = false;
    public boolean enable_citation = false;
    public String userID = "";
    public YiYanMessage[] messages;

    public YiYanRequest(String requestId, String robotID) {
        this.requestID = requestId;
        this.robotID = robotID;
        this.timestamp = System.currentTimeMillis() + "";
    }

    public void setOneMsg(String msg) {
        YiYanMessage yiYanMessage = new YiYanMessage(msg);
        messages = new YiYanMessage[1];
        messages[0] = yiYanMessage;
    }

    public class YiYanMessage {
        public String role;
        public String content;

        public YiYanMessage(String msg) {
            this.role = "user";
            this.content = msg;
        }
    }
}