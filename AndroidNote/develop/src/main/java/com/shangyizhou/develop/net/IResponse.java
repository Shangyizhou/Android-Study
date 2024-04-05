package com.shangyizhou.develop.net;

/**
 * Created by zhouguangfu on 22/03/2017.
 */

public interface IResponse {
    void onSuccess(String originJson);
    void onFailure(String errorMsg);
}
