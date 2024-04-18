package com.example.androidnote.manager;

import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.example.androidnote.model.ASRresponse;
import com.example.androidnote.model.AsrResponse;
import com.google.gson.Gson;

public class VoiceServiceManager implements EventListener{
    private static String TAG = VoiceServiceManager.class.getSimpleName();
    private static volatile VoiceServiceManager instance;

    private VoiceServiceManager() {

    }

    public static VoiceServiceManager getInstance() {
        if (instance == null) {
            synchronized (VoiceServiceManager.class) {
                if (instance == null) {
                    instance = new VoiceServiceManager();
                }
            }
        }
        return instance;
    }

    private EventManager asr;//语音识别核心库

    private void init() {
        asr.registerListener(this); //  EventListener 中 onEvent方法
    }

    @Override
    public void onEvent(String name, String params, byte[] data, int offset, int length) {
        if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL)) {
            // 识别相关的结果都在这里
            if (params == null || params.isEmpty()) {
                return;
            }
            if (params.contains("\"final_result\"")) {
                // 一句话的最终识别结果
                // txtResult.setText(params);

                Gson gson = new Gson();
                ASRresponse asRresponse = gson.fromJson(params, ASRresponse.class);//数据解析转实体bean

                if (asRresponse == null) return;
                // 从日志中，得出Best_result的值才是需要的，但是后面跟了一个中文输入法下的逗号，
                if (asRresponse.getBest_result().contains("，")) {// 包含逗号  则将逗号替换为空格，这个地方还会问题，还可以进一步做出来，你知道吗？
                    // 替换为空格之后，通过trim去掉字符串的首尾空格
                    String res = asRresponse.getBest_result().replace('，', ' ').trim();
                } else {// 不包含
                    String res = asRresponse.getBest_result().trim();
                }
            }
        }
    }

    public void startAsr() {
        asr.send(SpeechConstant.ASR_START, "{}", null, 0, 0);
    }

    public void stopAsr() {
        asr.send(SpeechConstant.ASR_STOP, "{}", null, 0, 0);
    }

}

