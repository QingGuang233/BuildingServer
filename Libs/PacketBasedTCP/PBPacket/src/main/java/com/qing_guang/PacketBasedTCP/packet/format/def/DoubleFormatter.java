package com.qing_guang.PacketBasedTCP.packet.format.def;

import com.qing_guang.PacketBasedTCP.packet.format.DataFormatter;

/**
 * Ĭ�ϵ�double���ͽ�����
 */
public class DoubleFormatter implements DataFormatter<Double> {

    private LongFormatter longFormatter;

    /** default */
    public DoubleFormatter(){}

    /**
     * �½�������
     * @param longFormatter ����ʹ�õ�long������
     */
    public DoubleFormatter(LongFormatter longFormatter){
        this.longFormatter = longFormatter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double toData(byte[] ori, int start, int length) {
        return Double.longBitsToDouble(longFormatter.toData(ori, start, length));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] toBytes(Object data) {
        return longFormatter.toBytes(Double.doubleToLongBits((double) data));
    }

}
