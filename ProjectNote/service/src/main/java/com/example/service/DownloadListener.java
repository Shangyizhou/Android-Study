package com.example.service;

// 用于OkHttp的回调接口
public interface DownloadListener {

    // 通知下载进度
    void onProgress(int progress);

    // 下载成功事件
    void onSuccess();

    // 下载失败事件
    void onFailed();

    // 下载暂停事件
    void onPaused();

    // 取消下载事件
    void onCanceled();

}