package com.shangyizhou.develop.net;
import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RetryInterceptor implements Interceptor {
    private int maxRetries;
    private int retryDelayMillis;

    public RetryInterceptor(int maxRetries, int retryDelayMillis) {
        this.maxRetries = maxRetries;
        this.retryDelayMillis = retryDelayMillis;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = null;
        IOException exception = null;
        // 循环重试
        for (int i = 0; i < maxRetries; i++) {
            try {
                response = chain.proceed(request);
                if (response != null && response.isSuccessful()) {
                    return response;
                }
            } catch (IOException e) {
                exception = e;
            }
            try {
                // 等待重试间隔时间
                Thread.sleep(retryDelayMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return response;
            }
        }

        // 如果仍然失败，则抛出异常
        if (exception != null) {
            throw exception;
        } else {
            throw new IOException("Unknown error occurred");
        }
    }
}