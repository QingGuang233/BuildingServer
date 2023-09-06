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
 * 数据包和传输的数据的互转工具类,数据格式如下(包括ContainerOnly)(总长度的发送由BufferLinkedList负责,不要发重复了):
 * 总长度 长度 数据包分类标识 长度 数据包类型名
 * ID 长度 数据
 * ID 长度 数据
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
     * 注册一个数据包类型
     * 接收的时候,如果一个被判断标识了ContainerOnly的字段(或者这个数据包都)没有找到无参构造器,那这个字段会跳过赋值(如果是数据包就返回PBPacketVoid)并发出警报
     * @param type 数据包类型
     * @throws IllegalArgumentException 当数据包类型写明包含的数据包中有没有注明属于此数据包类型或被检测到的类没有无参构造器时抛出
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
     * 添加一类数据的解析器,只需要指定父类即可(当然父类本身也可以被查找到解析器)
     * @param clazz 数据类型
     * @param formatter 解析器
     */
    public void addDataFormatter(Class<?> clazz,DataFormatter<?> formatter){
        dataFormatters.put(clazz, formatter);
    }

    /**
     * 删除一类数据的解析器
     * @param clazz 数据类型
     */
    public void removeDataFormatter(Class<?> clazz){
        dataFormatters.remove(clazz);
    }

    /**
     * 将一个数据包转成待发送的Buffer
     * @param packet 数据包
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
            System.err.println("不可预知的严重错误发生了");
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
                        System.err.println("数据类型: " + field.getType().getName() + " 找不到合适的解析器");
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
     * 将一段数据转成Packet
     * 注意,这一段数据不应包含总长度信息,请截掉总长度信息后再作为参数传入
     * @param data 数据
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
                System.err.println("数据包: " + typeName + ":" + packetName + " 找不到无参构造器");
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
            System.err.println("不可预知的严重错误发生了");
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
                        System.err.println("数据类型: " + field.getType().getComponentType().getName() + " 找不到合适的解析器");
                    }
                }else{
                    DataFormatter<?> formatter = find(field.getType());
                    if(formatter == null){
                        System.err.println("数据类型: " + field.getType().getName() + " 找不到合适的解析器");
                    }else{
                        field.set(result, formatter.toData(data, now, len));
                    }
                }
            }else{
                Class<?> type = field.getType();
                Constructor<?> constructor = constructors.get(type);
                if (constructor == null) {
                    System.err.println("数据类型: " + type.getName() + " 找不到无参构造器");
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
     * 获取当前正在使用的StringFormatter使用的字符集
     * @return result
     */
    public Charset getCharset(){
        return stringFormatter.getCharset();
    }

    /**
     * 设置当前正在使用的StringFormatter使用的字符集
     * @param newCharset 新字符集
     */
    public void setCharset(Charset newCharset){
        stringFormatter.setCharset(newCharset);
    }

    /**
     * 正在使用的int解析器
     * @return result
     */
    public IntFormatter getIntFormatter(){
        return intFormatter;
    }

    /**
     * 寻找一种类型的解析器
     * 如果这个类型没有找到解析器,沿着这个类型的父类往上找,第一个找到的解析器就是实际使用的解析器
     * 每一次往上找(包括刚开始),在没找到之后都会继续找这个类实现的接口以及它们继承的接口(具体找到的是哪个接口的不受控制,所以数据对象的继承和实现关系不要过于复杂),如果全都没找到,才会继续向上找
     * 如果找到Object之前都没有找到合适的解析器,那么这个字段就会被放弃解析,并发出警告
     * @param type 指定的类型
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
