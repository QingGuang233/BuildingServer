package com.qing_guang.PacketBasedTCP.packet.format.def;

import com.qing_guang.PacketBasedTCP.packet.format.DataFormatter;

/**
 * Ĭ�ϵ�ö�����ͽ�����
 */
public class EnumFormatter<T extends Enum<T>> implements DataFormatter<T> {

    private StringFormatter stringFormatter;

    /** default */
    public EnumFormatter(){}

    /**
     * �½�������
     * @param stringFormatter ����ʹ�õ�String������
     */
    public EnumFormatter(StringFormatter stringFormatter){
        this.stringFormatter = stringFormatter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public T toData(byte[] ori, int start, int length) {
        try {
            String[] data = stringFormatter.toData(ori, start, length).split(":");
            Class<?> enumClass = Class.forName(data[0]);
            return (T) enumClass.getMethod("valueOf",String.class).invoke(null,data[1]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] toBytes(Object data) {
        return stringFormatter.toBytes(data.getClass().getTypeName() + ":" + ((Enum<?>) data).name());
    }

}
