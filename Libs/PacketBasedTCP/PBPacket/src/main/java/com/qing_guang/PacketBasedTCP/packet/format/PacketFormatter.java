package com.qing_guang.PacketBasedTCP.packet.format;

import com.qing_guang.PacketBasedTCP.packet.PBPacket;
import com.qing_guang.PacketBasedTCP.packet.PBPacketType;
import com.qing_guang.PacketBasedTCP.packet.def.PBPacketTypeDef;
import com.qing_guang.PacketBasedTCP.packet.def.PBPacketVoid;
import com.qing_guang.PacketBasedTCP.packet.format.anno.ContainerOnly;
import com.qing_guang.PacketBasedTCP.packet.format.anno.DataField;
import com.qing_guang.PacketBasedTCP.packet.format.anno.PacketInfo;
import com.qing_guang.PacketBasedTCP.packet.format.anno.PacketTypeInfo;
import com.qing_guang.PacketBasedTCP.packet.format.def.*;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * ���ݰ��ʹ�������ݵĻ�ת������,���ݸ�ʽ����(����ContainerOnly)(�ܳ��ȵķ�����BufferLinkedList����,��Ҫ���ظ���):
 * �ܳ��� ���� ���ݰ������ʶ ���� ���ݰ�������
 * ID ���� ����
 * ID ���� ����
 * ...
 */
public class PacketFormatter {

    private Map<String,Class<? extends PBPacketType>> types = new HashMap<>();
    private Map<Class<? extends PBPacketType>,String> typesName = new HashMap<>();
    private Map<String,Map<String,Class<? extends PBPacket>>> packets = new HashMap<>();
    private Map<Class<? extends PBPacket>,String> packetsName = new HashMap<>();
    private Map<Class<?>, Constructor<?>> constructors = new HashMap<>();
    private Map<Class<?>,Map<Integer, Field>> fields = new HashMap<>();
    private Map<Class<?>, DataFormatter<?>> dataFormatters = new HashMap<>();

    private IntFormatter intFormatter;
    private StringFormatter stringFormatter;

    /** default */
    public PacketFormatter(){

        ByteFormatter byteFormatter = new ByteFormatter();
        ShortFormatter shortFormatter = new ShortFormatter();
        IntFormatter intFormatter = new IntFormatter();
        LongFormatter longFormatter = new LongFormatter();
        FloatFormatter floatFormatter = new FloatFormatter(intFormatter);
        DoubleFormatter doubleFormatter = new DoubleFormatter(longFormatter);
        CharFormatter charFormatter = new CharFormatter(shortFormatter);
        BooleanFormatter booleanFormatter = new BooleanFormatter();
        StringFormatter stringFormatter = new StringFormatter();
        EnumFormatter<?> enumFormatter = new EnumFormatter<>(stringFormatter);

        addDataFormatter(byte.class,byteFormatter);
        addDataFormatter(Byte.class,byteFormatter);
        addDataFormatter(short.class,shortFormatter);
        addDataFormatter(Short.class,shortFormatter);
        addDataFormatter(int.class,this.intFormatter = intFormatter);
        addDataFormatter(Integer.class,intFormatter);
        addDataFormatter(long.class,longFormatter);
        addDataFormatter(Long.class,longFormatter);
        addDataFormatter(float.class,floatFormatter);
        addDataFormatter(Float.class,floatFormatter);
        addDataFormatter(double.class,doubleFormatter);
        addDataFormatter(Double.class,doubleFormatter);
        addDataFormatter(char.class,charFormatter);
        addDataFormatter(Character.class,charFormatter);
        addDataFormatter(boolean.class,booleanFormatter);
        addDataFormatter(Boolean.class,booleanFormatter);
        addDataFormatter(String.class,this.stringFormatter = stringFormatter);
        addDataFormatter(Enum.class,enumFormatter);

        register(PBPacketTypeDef.class);

    }

    /**
     * ע��һ�����ݰ�����
     * ���յ�ʱ��,���һ�����жϱ�ʶ��ContainerOnly���ֶ�(����������ݰ���)û���ҵ��޲ι�����,������ֶλ�������ֵ(��������ݰ��ͷ���PBPacketVoid)����������
     * @param type ���ݰ�����
     * @throws IllegalArgumentException �����ݰ�����д�����������ݰ�����û��ע�����ڴ����ݰ����ͻ򱻼�⵽����û���޲ι�����ʱ�׳�
     */
    public void register(Class<? extends PBPacketType> type) throws IllegalArgumentException{
        PacketTypeInfo typeInfo = type.getAnnotation(PacketTypeInfo.class);
        Map<String,Class<? extends PBPacket>> packets = new HashMap<>();
        for(Class<? extends PBPacket> packet : typeInfo.includes()){
            PacketInfo packetInfo = packet.getAnnotation(PacketInfo.class);
            if(!packetInfo.belongsTo().equals(type)){
                throw new IllegalArgumentException(
                        "Verification failed: " +
                                packet.getName() +
                                " belongs to " +
                                packetInfo.belongsTo().getName()
                );
            }
            try{
                reflectionAdd(packet);
            }catch (NoSuchMethodException e){
                throw new IllegalArgumentException(e);
            }
            packets.put(packetInfo.name(),packet);
            packetsName.put(packet,packetInfo.name());
        }
        this.packets.put(typeInfo.name(),packets);
        typesName.put(type,typeInfo.name());
        types.put(typeInfo.name(),type);
    }

    /**
     * ���һ�����ݵĽ�����,ֻ��Ҫָ�����༴��(��Ȼ���౾��Ҳ���Ա����ҵ�������)
     * @param clazz ��������
     * @param formatter ������
     */
    public void addDataFormatter(Class<?> clazz,DataFormatter<?> formatter){
        dataFormatters.put(clazz, formatter);
    }

    /**
     * ɾ��һ�����ݵĽ�����
     * @param clazz ��������
     */
    public void removeDataFormatter(Class<?> clazz){
        dataFormatters.remove(clazz);
    }

    /**
     * ��һ�����ݰ�ת�ɴ����͵�Buffer
     * @param packet ���ݰ�
     * @return result
     */
    public BufferLinkedList toBytes(PBPacket packet){
        try{
            BufferLinkedList result = new BufferLinkedList(64,intFormatter);
            PacketInfo info = packet.getClass().getAnnotation(PacketInfo.class);
            byte[] strDt = stringFormatter.toBytes(typesName.get(info.belongsTo()));
            result.put(intFormatter.toBytes(strDt.length));
            result.put(strDt);
            strDt = stringFormatter.toBytes(info.name());
            result.put(intFormatter.toBytes(strDt.length));
            result.put(strDt);
            Map<Integer,Field> fieldMap = new HashMap<>();
            for(Class<?> superClass = packet.getClass();superClass != PBPacket.class;superClass = superClass.getSuperclass()){
                fieldMap.putAll(fields.get(superClass));
            }
            result.put(toBytes(packet,fieldMap));
            return result;
        }catch (IllegalAccessException e){
            System.err.println("����Ԥ֪�����ش�������");
            e.printStackTrace();
            return null;
        }
    }

    private byte[] toBytes(
            Object obj,
            Map<Integer,Field> fieldMap
    ) throws IllegalAccessException{
        List<byte[]> allBytes = new ArrayList<>();
        int length = 0;
        for(Map.Entry<Integer,Field> entry : fieldMap.entrySet()){
            Field field = entry.getValue();
            byte[] dt;
            Object fieldObj = field.get(obj);
            if(fieldObj == null){
                continue;
            }
            if(!isContainerOnly(field)){
                if(field.getType().isArray()){
                    dt = arrayToBytes(fieldObj,field.getType().getComponentType());
                }else{
                    DataFormatter<?> formatter = find(field.getType());
                    if(formatter != null){
                        dt = formatter.toBytes(fieldObj);
                    }else{
                        System.err.println("��������: " + field.getType().getName() + " �Ҳ������ʵĽ�����");
                        continue;
                    }
                }
            }else{
                Map<Integer,Field> newFieldMap = new HashMap<>();
                for(Class<?> superClass = field.getType();superClass != Object.class;superClass = superClass.getSuperclass()){
                    newFieldMap.putAll(fields.get(superClass));
                }
                dt = toBytes(fieldObj,newFieldMap);
            }
            allBytes.add(intFormatter.toBytes(entry.getKey()));
            allBytes.add(intFormatter.toBytes(dt.length));
            allBytes.add(dt);
            length += 8 + dt.length;
        }
        byte[] result = new byte[length];
        int now = 0;
        for (byte[] one : allBytes){
            for(int i = 0;i < one.length;i++,now++){
                result[now] = one[i];
            }
        }

        return result;
    }

    /**
     * ��һ������ת��Packet
     * ע��,��һ�����ݲ�Ӧ�����ܳ�����Ϣ,��ص��ܳ�����Ϣ������Ϊ��������
     * @param data ����
     * @return result
     */
    @SuppressWarnings("unchecked")
    public PBPacket toPacket(byte[] data){

        try{

            int now = 0;
            int len;

            len = intFormatter.toData(data,now,4);
            now += 4;
            String typeName = stringFormatter.toData(data,now,len);
            now += len;
            len = intFormatter.toData(data,now,4);
            now += 4;
            String packetName = stringFormatter.toData(data,now,len);
            now += len;
            Class<? extends PBPacket> clazz = packets.get(typeName).get(packetName);
            Constructor<PBPacket> constructor = (Constructor<PBPacket>) constructors.get(clazz);
            if(constructor == null){
                System.err.println("���ݰ�: " + typeName + ":" + packetName + " �Ҳ����޲ι�����");
                return new PBPacketVoid();
            }
            PBPacket result = constructor.newInstance();

            Map<Integer,Field> fieldMap = new HashMap<>();
            for(Class<?> superClass = clazz;superClass != PBPacket.class;superClass = superClass.getSuperclass()){
                fieldMap.putAll(fields.get(superClass));
            }
            toObj(data,now,data.length - now,result,fieldMap);

            return result;

        }catch (Exception e){
            System.err.println("����Ԥ֪�����ش�������");
            e.printStackTrace();
            return null;
        }

    }

    private void toObj(
            byte[] data,
            int now,
            int len,
            Object result,
            Map<Integer,Field> fieldMap
    ) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        int end = now + len;
        while(now < end){
            int id = intFormatter.toData(data,now,4);
            now += 4;
            len = intFormatter.toData(data,now,4);
            now += 4;
            Field field = fieldMap.get(id);
            if(!isContainerOnly(field)){
                if(field.getType().isArray()){
                    try{
                        field.set(result, bytesToArray(data, now, field.getType().getComponentType()));
                    }catch (IllegalArgumentException e){
                        System.err.println("��������: " + field.getType().getComponentType().getName() + " �Ҳ������ʵĽ�����");
                    }
                }else{
                    DataFormatter<?> formatter = find(field.getType());
                    if(formatter == null){
                        System.err.println("��������: " + field.getType().getName() + " �Ҳ������ʵĽ�����");
                    }else{
                        field.set(result, formatter.toData(data, now, len));
                    }
                }
            }else{
                Class<?> type = field.getType();
                Constructor<?> constructor = constructors.get(type);
                if (constructor == null) {
                    System.err.println("��������: " + type.getName() + " �Ҳ����޲ι�����");
                }else{
                    Map<Integer,Field> newFieldMap = new HashMap<>();
                    for(Class<?> superClass = type;superClass != Object.class;superClass = superClass.getSuperclass()){
                        newFieldMap.putAll(fields.get(superClass));
                    }
                    Object obj = constructor.newInstance();
                    toObj(data,now,len,obj,newFieldMap);
                    field.set(result,obj);
                }
            }
            now += len;
        }
    }

    /**
     * ��ȡ��ǰ����ʹ�õ�StringFormatterʹ�õ��ַ���
     * @return result
     */
    public Charset getCharset(){
        return stringFormatter.getCharset();
    }

    /**
     * ���õ�ǰ����ʹ�õ�StringFormatterʹ�õ��ַ���
     * @param newCharset ���ַ���
     */
    public void setCharset(Charset newCharset){
        stringFormatter.setCharset(newCharset);
    }

    /**
     * ����ʹ�õ�int������
     * @return result
     */
    public IntFormatter getIntFormatter(){
        return intFormatter;
    }

    /**
     * Ѱ��һ�����͵Ľ�����
     * ����������û���ҵ�������,����������͵ĸ���������,��һ���ҵ��Ľ���������ʵ��ʹ�õĽ�����
     * ÿһ��������(�����տ�ʼ),��û�ҵ�֮�󶼻�����������ʵ�ֵĽӿ��Լ����Ǽ̳еĽӿ�(�����ҵ������ĸ��ӿڵĲ��ܿ���,�������ݶ���ļ̳к�ʵ�ֹ�ϵ��Ҫ���ڸ���),���ȫ��û�ҵ�,�Ż����������
     * ����ҵ�Object֮ǰ��û���ҵ����ʵĽ�����,��ô����ֶξͻᱻ��������,����������
     * @param type ָ��������
     * @return result
     */
    public DataFormatter<?> find(Class<?> type){
        if(dataFormatters.containsKey(type)){
            return dataFormatters.get(type);
        }else if(type != Object.class && !type.isInterface()){
            for(Class<?> parent = type.getSuperclass();parent != Object.class;parent = parent.getSuperclass()){
                DataFormatter<?> find = find(parent);
                if(find != null){
                    return find;
                }
            }
        }
        for(Class<?> superInterface : type.getInterfaces()){
            DataFormatter<?> find = find(superInterface);
            if(find != null){
                return find;
            }
        }
        return null;
    }

    private void reflectionAdd(Class<?> clazz) throws NoSuchMethodException {
        constructors.put(clazz,clazz.getConstructor());
        Map<Integer,Field> fields = new HashMap<>();
        for(Field field : clazz.getDeclaredFields()){
            field.setAccessible(true);
            DataField dataFieldAnno = field.getAnnotation(DataField.class);
            if(dataFieldAnno == null){
                continue;
            }
            fields.put(dataFieldAnno.fieldID(),field);
            if(isContainerOnly(field)){
                reflectionAdd(field.getType());
            }
        }
        this.fields.put(clazz,fields);
    }

    private static boolean isContainerOnly(Field field){
        return field.getAnnotation(ContainerOnly.class) != null ||
                field.getType().getAnnotation(ContainerOnly.class) != null;
    }

    private Object bytesToArray(byte[] ori, int start, Class<?> componentType) throws IllegalArgumentException{

        int num = intFormatter.toData(ori,start,4);
        Object result = Array.newInstance(componentType,num);
        DataFormatter<?> formatter = find(componentType);
        if(formatter == null){
            throw new IllegalArgumentException();
        }

        start += 4;
        for(int i = 0;i < num;i++){
            int dtLen = intFormatter.toData(ori,start,4);
            start += 4;
            Array.set(result,i,formatter.toData(ori,start,dtLen));
            start += dtLen;
        }

        if (byte.class.equals(componentType)) {
            byte[] result0 = new byte[num];
            for(int i = 0;i < num;i++){
                result0[i] = (Byte) Array.get(result, i);
            }
            return result0;
        }else if(short.class.equals(componentType)){
            short[] result0 = new short[num];
            for(int i = 0;i < num;i++){
                result0[i] = (Short) Array.get(result, i);
            }
            return result0;
        }else if(int.class.equals(componentType)){
            int[] result0 = new int[num];
            for(int i = 0;i < num;i++){
                result0[i] = (Integer) Array.get(result, i);
            }
            return result0;
        }else if(long.class.equals(componentType)){
            long[] result0 = new long[num];
            for(int i = 0;i < num;i++){
                result0[i] = (Long) Array.get(result, i);
            }
            return result0;
        }else if(float.class.equals(componentType)){
            float[] result0 = new float[num];
            for(int i = 0;i < num;i++){
                result0[i] = (Float) Array.get(result, i);
            }
            return result0;
        }else if(double.class.equals(componentType)){
            double[] result0 = new double[num];
            for(int i = 0;i < num;i++){
                result0[i] = (Double) Array.get(result, i);
            }
            return result0;
        }else if(char.class.equals(componentType)){
            char[] result0 = new char[num];
            for(int i = 0;i < num;i++){
                result0[i] = (Character) Array.get(result, i);
            }
            return result0;
        }else if(boolean.class.equals(componentType)){
            boolean[] result0 = new boolean[num];
            for(int i = 0;i < num;i++){
                result0[i] = (Boolean) Array.get(result, i);
            }
            return result0;
        }

        return result;

    }

    private byte[] arrayToBytes(Object data, Class<?> componentType) throws IllegalArgumentException{

        DataFormatter<?> formatter = find(componentType);
        if (formatter == null){
            throw new IllegalArgumentException();
        }

        List<byte[]> allBytes = new ArrayList<>();
        int length = 4;
        int num = Array.getLength(data);
        allBytes.add(intFormatter.toBytes(num));
        for(int i = 0;i < num;i++){
            byte[] dt = formatter.toBytes(Array.get(data,i));
            byte[] lengthDt = intFormatter.toBytes(dt.length);
            allBytes.add(lengthDt);
            allBytes.add(dt);
            length += 4 + dt.length;
        }
        byte[] result = new byte[length];
        int now = 0;
        for (byte[] one : allBytes){
            for(int i = 0;i < one.length;i++,now++){
                result[now] = one[i];
            }
        }

        return result;

    }

}
