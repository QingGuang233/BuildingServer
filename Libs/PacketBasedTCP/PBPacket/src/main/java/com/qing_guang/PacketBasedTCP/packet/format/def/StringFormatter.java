package com.qing_guang.PacketBasedTCP.packet.format.def;

import com.qing_guang.PacketBasedTCP.packet.format.DataFormatter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Ĭ�ϵ�string���ͽ�����
 */
public class StringFormatter implements DataFormatter<String> {

    private Charset charset = StandardCharsets.UTF_8;

    /**
     * {@inheritDoc}
     */
    @Override
    public String toData(byte[] ori, int start, int length) {
        return new String(ori,start,length,charset);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] toBytes(Object data) {
        return ((String)data).getBytes(charset);
    }

    /**
     * ��ȡ�˽���������ʹ�õ��ַ���
     * @return result
     */
    public Charset getCharset(){
        return charset;
    }

    /**
     * ���ô˽�����ʹ�õ��ַ���
     * @param charset ��Ҫ���ĵ��ַ���
     */
    public void setCharset(Charset charset){
        this.charset = charset;
    }

}
