package com.qing_guang.PacketBasedTCP.packet.format.def;

import com.qing_guang.PacketBasedTCP.packet.format.DataFormatter;

/**
 * Ĭ�ϵ�float���ͽ�����
 */
public class FloatFormatter implements DataFormatter<Float> {

    private IntFormatter intFormatter;

    /** default */
    public FloatFormatter(){}

    /**
     * �½�������
     * @param intFormatter ����ʹ�õ�int������
     */
    public FloatFormatter(IntFormatter intFormatter){
        this.intFormatter = intFormatter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Float toData(byte[] ori, int start, int length) {
        return Float.intBitsToFloat(intFormatter.toData(ori, start, length));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] toBytes(Object data) {
        return intFormatter.toBytes(Float.floatToIntBits((float) data));
    }

}
