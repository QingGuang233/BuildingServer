package com.qing_guang.PacketBasedTCP.client.handler;

import com.qing_guang.PacketBasedTCP.packet.PBHandlePriority;
import com.qing_guang.PacketBasedTCP.packet.PBPacket;
import com.qing_guang.PacketBasedTCP.packet.PBPacketType;
import com.qing_guang.PacketBasedTCP.packet.format.anno.PacketInfo;

import java.util.*;

/**
 * 数据包监听系统
 */
public class PBHandlerManager {

    private static final PBHandlePriority[] PRIORITIES = PBHandlePriority.values();
    private Map<Class<? extends PBPacket>, Map<PBHandlePriority,Set<PBPacketHandler>>> handlersPacket;
    private Map<Class<? extends PBPacketType>, Map<PBHandlePriority,Set<PBPacketHandler>>> handlersType;
    private Map<PBHandlePriority,Set<PBPacketHandler>> handlersAll;

    static{
        PBHandlePriority temp;
        for(int i = PRIORITIES.length - 1;i >= 0;i--){
            for(int j = 0;j < i;j++){
                if(PRIORITIES[j].getLevel() > PRIORITIES[j + 1].getLevel()){
                    temp = PRIORITIES[j];
                    PRIORITIES[j] = PRIORITIES[j + 1];
                    PRIORITIES[j + 1] = temp;
                }
            }
        }
    }

    /** default */
    public PBHandlerManager(){
        handlersPacket = new HashMap<>();
        handlersType = new HashMap<>();
        handlersAll = addPriority(new HashMap<>());
    }

    /**
     * 添加一个数据包处理器,以ID为标识
     * @param handler 数据包处理器
     * @param canHandle 此处理器可处理的数据包ID
     */
    @SuppressWarnings("unchecked")
    public void addPacketHandler(PBPacketHandler handler, Class<?>... canHandle){
        for(Class<?> packetClass : canHandle){
            if(!handlersPacket.containsKey(packetClass)){
                handlersPacket.put((Class<? extends PBPacket>) packetClass,addPriority(new HashMap<>()));
            }
            handlersPacket.get(packetClass).get(handler.priority()).add(handler);
        }
    }

    /**
     * 删除指定数据包ID对应的指定处理器
     * @param handler 数据包处理器
     * @param needTo 数据包ID
     */
    @SuppressWarnings("unchecked")
    public void removePacketHandler(PBPacketHandler handler, Class<? extends PBPacket>... needTo){
        for(Class<? extends PBPacket> packetClass : needTo){
            handlersPacket.get(packetClass).get(handler.priority()).remove(handler);
        }
    }

    /**
     * 添加一个数据包处理器,以数据包类型为标识
     * @param handler 数据包处理器
     * @param canHandle 此处理器可处理的数据包类型
     */
    public void addPacketHandlerType(PBPacketHandler handler,Class<? extends PBPacketType> canHandle){
        if(!handlersType.containsKey(canHandle)){
            handlersType.put(canHandle,addPriority(new HashMap<>()));
        }
        handlersType.get(canHandle).get(handler.priority()).add(handler);
    }

    /**
     * 删除指定数据包类型对应的指定处理器
     * @param handler 数据包处理器
     * @param needTo 数据包类型
     */
    public void removePacketHandlerType(PBPacketHandler handler,Class<? extends PBPacketType> needTo){
        handlersType.get(needTo).get(handler.priority()).remove(handler);
    }

    /**
     * 添加一个数据包处理器,可监听任何数据包
     * @param handler 数据包处理器
     */
    public void addPacketHandlerAll(PBPacketHandler handler){
        handlersAll.get(handler.priority()).add(handler);
    }

    /**
     * 删除一个可以监听任何数据包的指定处理器
     * @param handler 数据包处理器
     */
    public void removePacketHandlerAll(PBPacketHandler handler){
        handlersAll.get(handler.priority()).remove(handler);
    }

    /**
     * 数据包事件触发
     * @param packet 数据包
     * @param in 输入为true,输出为false
     */
    public void handle(PBPacket packet,boolean in){

        Class<? extends PBPacket> packetClass = packet.getClass();
        PacketInfo info = packetClass.getAnnotation(PacketInfo.class);
        Map<PBHandlePriority,Set<PBPacketHandler>> clazz = handlersPacket.get(packetClass);
        Class<? extends PBPacketType> pktType = info.belongsTo();
        List<Class<? extends PBPacketType>> types = new LinkedList<>();
        for(Class<? extends PBPacketType> typeClass : handlersType.keySet()){
            if(typeClass.isAssignableFrom(pktType)){
                types.add(typeClass);
            }
        }

        for (PBHandlePriority priority : PRIORITIES) {
            if(clazz != null)
                clazz.get(priority).forEach(handler -> handler.accept(packet, in));
            for(Class<? extends PBPacketType> typeClass : types){
                Map<PBHandlePriority,Set<PBPacketHandler>> type = handlersType.get(typeClass);
                type.get(priority).forEach(handler -> handler.accept(packet, in));
            }
            handlersAll.get(priority).forEach(handler -> handler.accept(packet, in));
        }

    }

    private Map<PBHandlePriority,Set<PBPacketHandler>> addPriority(Map<PBHandlePriority,Set<PBPacketHandler>> handlers){
        for(PBHandlePriority priority : PBHandlePriority.values()){
            handlers.put(priority,new HashSet<>());
        }
        return handlers;
    }

}