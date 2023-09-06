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
 * ���Զ����ӳ��ȵĻ����
 * ֻ�Է��������Ѻ�,���ʺ���������;
 */
public class BufferLinkedList {

    private int bufferLen;
    private Node first;
    private Node current;
    private IntFormatter intFormatter;

    /** default */
    public BufferLinkedList(){}

    /**
     * ����һ�������
     * @param bufferLen ÿ�����ӵĳ���
     * @param intFormatter ����������ʹ�õ�IntFormatter
     */
    public BufferLinkedList(int bufferLen, IntFormatter intFormatter){
        this.bufferLen = bufferLen;
        current = first = new Node(bufferLen);
        this.intFormatter = intFormatter;
    }

    /**
     * ��ȡÿ�����ӵĳ���
     * @return result
     */
    public int getBufferLen() {
        return bufferLen;
    }

    /**
     * ����ÿ�����ӵĳ���(֮ǰ�Ѿ�����Ĳ�����)
     * @param bufferLen ÿ�����ӵĳ���
     */
    public void setBufferLen(int bufferLen) {
        this.bufferLen = bufferLen;
    }

    /**
     * д��һ���ֽ�����
     * @param data ����
     */
    public void put(byte data){
        checkSwitch();
        current.arr[current.now++] = data;
    }

    /**
     * д��һ���ֽ�����
     * @param data ����
     * @param start ������ĺδ���ʼд��
     * @param end ������ĺδ�ǰͣ��
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
     * д��һ���ֽ�����
     * @param data ����
     */
    public void put(byte[] data){
        put(data,0,data.length);
    }

    /**
     * д�����洢����������(��ǰ��������ĸ��ֽڵ��ܳ�����Ϣ)
     * @param channel д����Channel
     * @param encrypt ���ܵĺ���
     * @throws IOException ��Channel��IO�쳣ʱ�׳�
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
     * ��ȡ��һ���ڵ�
     * ֻ������������Լ������������,�Ǳ�Ҫ�������
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
