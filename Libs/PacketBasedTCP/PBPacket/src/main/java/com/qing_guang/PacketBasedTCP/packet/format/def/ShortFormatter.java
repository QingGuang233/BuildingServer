package com.qing_guang.PacketBasedTCP.packet.format.def;

import com.qing_guang.PacketBasedTCP.packet.format.DataFormatter;

/**
 * 默认的short类型解析器
 */
public class ShortFormatter implements DataFormatter<Short> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Short toData(byte[] ori, int start, int length) {
        short result = 0;
        result += (short) ((ori[start] & 0xFF) << 8);
        result += ori[start + 1] & 0xFF;
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] toBytes(Object data) {
        short dt = (short) data;
        return new byte[]{(byte) (dt >> 8 & 0xFF),(byte) (dt & 0xFF)};
    }

}
