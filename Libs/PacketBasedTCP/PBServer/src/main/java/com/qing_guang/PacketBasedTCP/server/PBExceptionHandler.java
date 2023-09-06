package com.qing_guang.PacketBasedTCP.server;

import java.io.IOException;

/**
 * 网络传输异常处理器
 */
public interface PBExceptionHandler {

    /**
     * 当有异常出现时抛出
     * @param exception 异常对象
     * @param where 异常的出现位置
     * @param who 对与谁的连接操作中出现异常(当 {@code where == ExceptionWhere.ACCEPT} 时此参数为null)
     */
    void handle(IOException exception, ExceptionWhere where, PBClient who);

}
