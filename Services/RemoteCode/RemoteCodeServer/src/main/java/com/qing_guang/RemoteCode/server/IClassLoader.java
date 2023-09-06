package com.qing_guang.RemoteCode.server;

import java.util.HashMap;
import java.util.Map;

/**
 * 类加载器
 */
public class IClassLoader extends ClassLoader{

    private ClassLoader parent;
    private Map<String,byte[]> classDatas = new HashMap<>();
    private Map<String,Class<?>> loaded = new HashMap<>();

    /** default */
    public IClassLoader(){}

    /**
     * 新建一个类加载器
     * @param parent 父类加载器
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
     * 添加一个类文件数据
     * @param className 类名
     * @param data 数据
     */
    public void addClassData(String className,byte[] data){
        classDatas.put(className,data);
    }

}
