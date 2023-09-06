package com.qing_guang.PacketBasedTCP.packet.format.def;

import com.qing_guang.PacketBasedTCP.packet.format.DataFormatter;

/**
 * 默认的long类型解析器
 */
public class LongFormatter implements DataFormatter<Long> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Long toData(byte[] ori, int start, int length) {
        long result = 0;
        result += (((long) ori[start]) & 0xFF) << 56;
        result += (((long) ori[start + 1]) & 0xFF) << 48;
        result += (((long) ori[start + 2]) & 0xFF) << 40;
        result += (((long) ori[start + 3]) & 0xFF) << 32;
        result += (long) (ori[start + 4] & 0xFF) << 24;
        result += (long) (ori[start + 5] & 0xFF) << 16;
        result += (long) (ori[start + 6] & 0xFF) << 8;
        result += ori[start + 7] & 0xFF;
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] toBytes(Object data) {
        long dt = (long) data;
        return new byte[]{
                (byte) (dt >> 56 & 0xFF),
                (byte) (dt >> 48 & 0xFF),
                (byte) (dt >> 40 & 0xFF),
                (byte) (dt >> 32 & 0xFF),
                (byte) (dt >> 24 & 0xFF),
                (byte) (dt >> 16 & 0xFF),
                (byte) (dt >> 8 & 0xFF),
                (byte) (dt & 0xFF)
        };
    }

}
