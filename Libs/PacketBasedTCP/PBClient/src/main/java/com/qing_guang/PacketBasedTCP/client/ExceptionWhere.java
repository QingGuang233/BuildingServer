package com.qing_guang.PacketBasedTCP.client;

/**
 * 标明异常的出现位置
 */
public enum ExceptionWhere {

    /**
     * 在连接服务器时
     */
    CONNECTING,
    /**
     * 在从Channel中读取数据时
     */
    READ,
    /**
     * 在向Channel中写入数据时
     */
    WRITE

}
