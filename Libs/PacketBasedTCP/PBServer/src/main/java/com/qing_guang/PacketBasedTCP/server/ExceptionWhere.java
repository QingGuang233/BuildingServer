package com.qing_guang.PacketBasedTCP.server;

/**
 * 标明异常的出现位置
 */
public enum ExceptionWhere {

    /**
     * 在接受连接请求时
     */
    ACCEPT,
    /**
     * 在从Channel中读取数据时
     */
    READ,
    /**
     * 在向Channel中写入数据时
     */
    WRITE
}
