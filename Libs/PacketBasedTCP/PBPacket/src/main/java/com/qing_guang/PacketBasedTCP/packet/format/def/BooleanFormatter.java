package com.qing_guang.PacketBasedTCP.packet.format.def;

import com.qing_guang.PacketBasedTCP.packet.format.DataFormatter;

/**
 * 默认的boolean类型解析器
 */
public class BooleanFormatter implements DataFormatter<Boolean> {

    private static final byte[] TRUE = {1};
    private static final byte[] FALSE = {0};

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean toData(byte[] ori, int start, int length) {
        return ori[start] != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] toBytes(Object data) {
        boolean dt = (boolean) data;
        return dt ? TRUE : FALSE;
    }

}
