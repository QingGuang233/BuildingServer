package com.qing_guang.RemoteCode.server;

import java.util.HashMap;
import java.util.Map;

/**
 * �������
 */
public class IClassLoader extends ClassLoader{

    private ClassLoader parent;
    private Map<String,byte[]> classDatas = new HashMap<>();
    private Map<String,Class<?>> loaded = new HashMap<>();

    /** default */
    public IClassLoader(){}

    /**
     * �½�һ���������
     * @param parent ���������
     */
    public IClassLoader(ClassLoader parent){
        this.parent = parent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> c = loaded.get(name);
        if(c == null){
            byte[] data = classDatas.get(name);
            if(data != null){
                c = defineClass(name,data,0,data.length);
            }else{
                c = parent.loadClass(name);
            }
        }
        loaded.put(name,c);
        return c;
    }

    /**
     * ���һ�����ļ�����
     * @param className ����
     * @param data ����
     */
    public void addClassData(String className,byte[] data){
        classDatas.put(className,data);
    }

}
