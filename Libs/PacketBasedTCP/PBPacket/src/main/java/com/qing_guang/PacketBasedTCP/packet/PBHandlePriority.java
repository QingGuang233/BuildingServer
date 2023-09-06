package com.qing_guang.PacketBasedTCP.packet;

/**
 * 数据包监听器的优先级
 * level越大的监听到数据包越晚
 * MONITOR和LOWER_THAN_LOWEST是为某些特殊的测试情况使用,正式代码请使用其他优先级
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
     * 优先级等级,为了判断方便编写
     * @return result
     */
    public int getLevel(){
        return level;
    }

}
