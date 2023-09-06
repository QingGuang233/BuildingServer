package com.qing_guang.PacketBasedTCP.packet.format.def;

import com.qing_guang.PacketBasedTCP.packet.format.DataFormatter;

/**
 * 默认的byte类型解析器
 */
public class ByteFormatter implements DataFormatter<Byte> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Byte toData(byte[] ori, int start, int length) {
        return ori[start];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] toBytes(Object data) {
        return new byte[]{(Byte) data};
    }

}
