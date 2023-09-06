package com.qing_guang.PacketBasedTCP.packet.format.def;

import com.qing_guang.PacketBasedTCP.packet.format.DataFormatter;

/**
 * 默认的枚举类型解析器
 */
public class EnumFormatter<T extends Enum<T>> implements DataFormatter<T> {

    private StringFormatter stringFormatter;

    /** default */
    public EnumFormatter(){}

    /**
     * 新建解析器
     * @param stringFormatter 正在使用的String解析器
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
