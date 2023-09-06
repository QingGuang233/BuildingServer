package com.qing_guang.PacketBasedTCP.packet.format.def;

import com.qing_guang.PacketBasedTCP.packet.format.DataFormatter;

/**
 * 默认的char类型解析器
 */
public class CharFormatter implements DataFormatter<Character> {

    private ShortFormatter shortFormatter;

    /** default */
    public CharFormatter(){}

    /**
     * 新建解析器
     * @param shortFormatter 正在使用的short解析器
     */
    public CharFormatter(ShortFormatter shortFormatter){
        this.shortFormatter = shortFormatter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Character toData(byte[] ori, int start, int length) {
        return (char)shortFormatter.toData(ori, start, length).shortValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("all")
    public byte[] toBytes(Object data) {
        return shortFormatter.toBytes(((Character)data).charValue());
    }

}
