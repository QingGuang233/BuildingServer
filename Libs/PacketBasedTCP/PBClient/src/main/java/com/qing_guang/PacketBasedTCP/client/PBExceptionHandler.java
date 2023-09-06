package com.qing_guang.PacketBasedTCP.client;

import java.io.IOException;

/**
 * 网络传输异常处理器
 */
public interface PBExceptionHandler {

    /**
     * 当有异常出现时抛出
     * @param exception 异常对象
     * @param where 异常的出现位置
     */
    void handle(IOException exception, ExceptionWhere where);

}
