package com.qing_guang.PacketBasedTCP.packet.format;

/**
 * 一种数据的处理器
 * @param <T> 要处理的数据类型
 */
public interface DataFormatter<T>{

    /**
     * 将一个byte数组中的一段转成对应的数据
     * @param ori byte数组
     * @param start 开始读取的位置
     * @param length 数据长度
     * @return result
     */
    T toData(byte[] ori,int start,int length);

    /**
     * 将一个数据转成一串byte数据
     * @param data 给定的数据
     * @return 结束存储的下一个位置,可以在这里和这后面输入数据
     */
    byte[] toBytes(Object data);

}
