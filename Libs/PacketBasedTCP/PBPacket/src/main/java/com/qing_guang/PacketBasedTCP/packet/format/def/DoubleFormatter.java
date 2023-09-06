package com.qing_guang.PacketBasedTCP.packet.format.def;

import com.qing_guang.PacketBasedTCP.packet.format.DataFormatter;

/**
 * 默认的double类型解析器
 */
public class DoubleFormatter implements DataFormatter<Double> {

    private LongFormatter longFormatter;

    /** default */
    public DoubleFormatter(){}

    /**
     * 新建解析器
     * @param longFormatter 正在使用的long解析器
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
