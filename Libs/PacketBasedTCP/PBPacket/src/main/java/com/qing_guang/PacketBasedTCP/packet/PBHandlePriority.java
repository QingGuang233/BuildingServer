package com.qing_guang.PacketBasedTCP.packet;

/**
 * ���ݰ������������ȼ�
 * levelԽ��ļ��������ݰ�Խ��
 * MONITOR��LOWER_THAN_LOWEST��ΪĳЩ����Ĳ������ʹ��,��ʽ������ʹ���������ȼ�
 */
public enum PBHandlePriority {

    /** @see PBHandlePriority */
    MONITOR(7),
    /** @see PBHandlePriority */
    HIGHEST(6),
    /** @see PBHandlePriority */
    HIGHER(5),
    /** @see PBHandlePriority */
    HIGH(4),
    /** @see PBHandlePriority */
    DEFAULT(3),
    /** @see PBHandlePriority */
    LOW(2),
    /** @see PBHandlePriority */
    LOWER(1),
    /** @see PBHandlePriority */
    LOWEST(0),
    /** @see PBHandlePriority */
    LOWER_THAN_LOWEST(-1);

    private final int level;

    PBHandlePriority(int level){
        this.level = level;
    }

    /**
     * ���ȼ��ȼ�,Ϊ���жϷ����д
     * @return result
     */
    public int getLevel(){
        return level;
    }

}
