package com.qing_guang.PacketBasedTCP.packet.format.def;

import com.qing_guang.PacketBasedTCP.packet.format.DataFormatter;

/**
 * Ĭ�ϵ�byte���ͽ�����
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
