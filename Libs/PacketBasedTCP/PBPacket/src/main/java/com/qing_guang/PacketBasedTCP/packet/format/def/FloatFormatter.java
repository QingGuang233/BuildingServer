package com.qing_guang.PacketBasedTCP.packet.format.def;

import com.qing_guang.PacketBasedTCP.packet.format.DataFormatter;

/**
 * 默认的float类型解析器
 */
public class FloatFormatter implements DataFormatter<Float> {

    private IntFormatter intFormatter;

    /** default */
    public FloatFormatter(){}

    /**
     * 新建解析器
     * @param intFormatter 正在使用的int解析器
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
