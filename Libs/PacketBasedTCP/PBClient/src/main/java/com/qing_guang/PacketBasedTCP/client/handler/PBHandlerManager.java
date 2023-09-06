package com.qing_guang.PacketBasedTCP.client.handler;

import com.qing_guang.PacketBasedTCP.packet.PBHandlePriority;
import com.qing_guang.PacketBasedTCP.packet.PBPacket;
import com.qing_guang.PacketBasedTCP.packet.PBPacketType;
import com.qing_guang.PacketBasedTCP.packet.format.anno.PacketInfo;

import java.util.*;

/**
 * ���ݰ�����ϵͳ
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
     * ���һ�����ݰ�������,��IDΪ��ʶ
     * @param handler ���ݰ�������
     * @param canHandle �˴������ɴ�������ݰ�ID
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
     * ɾ��ָ�����ݰ�ID��Ӧ��ָ��������
     * @param handler ���ݰ�������
     * @param needTo ���ݰ�ID
     */
    @SuppressWarnings("unchecked")
    public void removePacketHandler(PBPacketHandler handler, Class<? extends PBPacket>... needTo){
        for(Class<? extends PBPacket> packetClass : needTo){
            handlersPacket.get(packetClass).get(handler.priority()).remove(handler);
        }
    }

    /**
     * ���һ�����ݰ�������,�����ݰ�����Ϊ��ʶ
     * @param handler ���ݰ�������
     * @param canHandle �˴������ɴ�������ݰ�����
     */
    public void addPacketHandlerType(PBPacketHandler handler,Class<? extends PBPacketType> canHandle){
        if(!handlersType.containsKey(canHandle)){
            handlersType.put(canHandle,addPriority(new HashMap<>()));
        }
        handlersType.get(canHandle).get(handler.priority()).add(handler);
    }

    /**
     * ɾ��ָ�����ݰ����Ͷ�Ӧ��ָ��������
     * @param handler ���ݰ�������
     * @param needTo ���ݰ�����
     */
    public void removePacketHandlerType(PBPacketHandler handler,Class<? extends PBPacketType> needTo){
        handlersType.get(needTo).get(handler.priority()).remove(handler);
    }

    /**
     * ���һ�����ݰ�������,�ɼ����κ����ݰ�
     * @param handler ���ݰ�������
     */
    public void addPacketHandlerAll(PBPacketHandler handler){
        handlersAll.get(handler.priority()).add(handler);
    }

    /**
     * ɾ��һ�����Լ����κ����ݰ���ָ��������
     * @param handler ���ݰ�������
     */
    public void removePacketHandlerAll(PBPacketHandler handler){
        handlersAll.get(handler.priority()).remove(handler);
    }

    /**
     * ���ݰ��¼�����
     * @param packet ���ݰ�
     * @param in ����Ϊtrue,���Ϊfalse
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