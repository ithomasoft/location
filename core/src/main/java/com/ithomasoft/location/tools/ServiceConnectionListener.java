package com.ithomasoft.location.tools;

/**
 * 用于响应来自 3rd 方服务的连接事件的回调接口。
 */
public interface ServiceConnectionListener {
    /**
     * 成功连接到第 3 方服务时的回调
     */
    void onConnected();

    /**
     * 与第 3 方服务的连接中断（网络故障、临时中断等）时的回调
     */
    void onConnectionSuspended();

    /**
     * 与第 3 方服务的连接失败时的回调（缺少库、错误的 API 密钥等）
     */
    void onConnectionFailed();
}
