package com.qing_guang.PacketBasedTCP.packet.format.def;

import com.qing_guang.PacketBasedTCP.packet.format.DataFormatter;

/**
 * 默认的int类型解析器
 */
public class IntFormatter implements DataFormatter<Integer> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer toData(byte[] ori, int start, int length) {
        int result = 0;
        result += (ori[start] & 0xFF) << 24;
        result += (ori[start + 1] & 0xFF) << 16;
        result += (ori[start + 2] & 0xFF) << 8;
        result += ori[start + 3] & 0xFF;
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] toBytes(Object data) {
        int dt = (int) data;
        return new byte[]{
                (byte) (dt >> 24 & 0xFF),
                (byte) (dt >> 16 & 0xFF),
                (byte) (dt >> 8 & 0xFF),
                (byte) (dt & 0xFF)
        };
    }

}
