package com.qing_guang.PacketBasedTCP.packet.format;

import com.qing_guang.PacketBasedTCP.packet.format.def.IntFormatter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

/**
 * 可自动增加长度的缓存库
 * 只对发送数据友好,不适合于其他用途
 */
public class BufferLinkedList {

    private int bufferLen;
    private Node first;
    private Node current;
    private IntFormatter intFormatter;

    /** default */
    public BufferLinkedList(){}

    /**
     * 创建一个缓存库
     * @param bufferLen 每次增加的长度
     * @param intFormatter 服务器正在使用的IntFormatter
     */
    public BufferLinkedList(int bufferLen, IntFormatter intFormatter){
        this.bufferLen = bufferLen;
        current = first = new Node(bufferLen);
        this.intFormatter = intFormatter;
    }

    /**
     * 获取每次增加的长度
     * @return result
     */
    public int getBufferLen() {
        return bufferLen;
    }

    /**
     * 设置每次增加的长度(之前已经扩充的不更改)
     * @param bufferLen 每次增加的长度
     */
    public void setBufferLen(int bufferLen) {
        this.bufferLen = bufferLen;
    }

    /**
     * 写入一个字节数据
     * @param data 数据
     */
    public void put(byte data){
        checkSwitch();
        current.arr[current.now++] = data;
    }

    /**
     * 写入一组字节数据
     * @param data 数据
     * @param start 从数组的何处开始写入
     * @param end 到数组的何处前停下
     */
    public void put(byte[] data, int start, int end){
        checkSwitch();
        end = Math.min(end, data.length);
        int nowRemains = current.arr.length - current.now;
        Node last = current;
        if(end - start > nowRemains){
            int needExpand = (end - start - nowRemains) / bufferLen + 1;
            for(int i = 0;i < needExpand;i++){
                last.next = new Node(bufferLen);
                last = last.next;
            }
        }
        for(;current != null;current = current.next){
            for(;current.now < current.arr.length && start < end;current.now++,start++){
                current.arr[current.now] = data[start];
            }
        }
        current = last;
    }

    /**
     * 写入一组字节数据
     * @param data 数据
     */
    public void put(byte[] data){
        put(data,0,data.length);
    }

    /**
     * 写出所存储的所有数据(最前方会加上四个字节的总长度信息)
     * @param channel 写出的Channel
     * @param encrypt 加密的函数
     * @throws IOException 当Channel有IO异常时抛出
     */
    public void write(WritableByteChannel channel, Function<byte[],byte[]> encrypt) throws IOException {
        int oriLen = 0;
        List<byte[]> allBytes = new LinkedList<>();
        for(Node node = first;node != null;node = node.next){
            if(node != current){
                allBytes.add(node.arr);
            }else{
                allBytes.add(Arrays.copyOfRange(node.arr,0,node.now));
            }
        }
        for(byte[] one : allBytes){
            oriLen += one.length;
        }
        byte[] oriData = new byte[oriLen];
        int now = 0;
        for(byte[] one : allBytes){
            for(int i = 0;i < one.length;i++,now++){
                oriData[now] = one[i];
            }
        }
        byte[] encrypted = encrypt.apply(oriData);
        ByteBuffer buffer = ByteBuffer.allocate(encrypted.length + 4);
        buffer.put(intFormatter.toBytes(encrypted.length));
        buffer.put(encrypted);
        buffer.flip();
        while(buffer.hasRemaining()){
            channel.write(buffer);
        }
    }

    /**
     * 获取第一个节点
     * 只用于特殊测试以及其他特殊情况,非必要建议忽略
     * @return result
     */
    public Node getFirst(){
        return first;
    }

    /**
     * {@inheritDoc}
     */
    public String toString(){
        StringBuilder builder = new StringBuilder("BufferLinkedList,nowMax=" + bufferLen + "{\n");
        for(Node node = first;node != null;node = node.next){
            builder.append("\t").append(Arrays.toString(node.arr)).append(" ,stored=").append(node.now).append(",max=").append(node.arr.length).append("\n");
        }
        builder.append('}');
        return builder.toString();
    }

    private void checkSwitch(){
        if(current.now >= current.arr.length){
            current.next = new Node(bufferLen);
            current = current.next;
        }
    }

    private static class Node{

        byte[] arr;
        int now = 0;
        Node next;

        private Node(int bufferLen){
            arr = new byte[bufferLen];
        }

    }

}
