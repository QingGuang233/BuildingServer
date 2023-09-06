package com.qing_guang.PacketBasedTCP.packet.format.def;

import com.qing_guang.PacketBasedTCP.packet.format.DataFormatter;

/**
 * Ĭ�ϵ�char���ͽ�����
 */
public class CharFormatter implements DataFormatter<Character> {

    private ShortFormatter shortFormatter;

    /** default */
    public CharFormatter(){}

    /**
     * �½�������
     * @param shortFormatter ����ʹ�õ�short������
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
