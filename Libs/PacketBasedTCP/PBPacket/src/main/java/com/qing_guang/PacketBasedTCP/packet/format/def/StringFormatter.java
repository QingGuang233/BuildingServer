package com.qing_guang.PacketBasedTCP.packet.format.def;

import com.qing_guang.PacketBasedTCP.packet.format.DataFormatter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 默认的string类型解析器
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
     * 获取此解析器正在使用的字符集
     * @return result
     */
    public Charset getCharset(){
        return charset;
    }

    /**
     * 设置此解析器使用的字符集
     * @param charset 需要更改的字符集
     */
    public void setCharset(Charset charset){
        this.charset = charset;
    }

}
