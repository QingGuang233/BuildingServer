package com.qing_guang.ICommandExecutor.plugin.main;

import org.bukkit.command.CommandSender;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ���ڷ����Ͽɱ�ע,��ʾ�����������ִ��ָ��
 * ����ǵķ���ֻ������������: CommandSenderΪָ�����,ʵ�ʱ�дʱ�����Ϳ��Ը���sender���б䶯,String[]Ϊָ��Ĳ���
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandCondition {
    /** ��������ĳ���,Ĭ��0,���������������� */
    int argsLen() default 0;
    /** ָ���������,������: arg1 [������д] arg2 [������д] [������д] arg3 ���������,Ҫ��д"arg1,arg2,arg3",֮��ͨ��originalPos��handlePos�����е��� */
    String[] args() default {};
    /** Ҫ�����Ĳ�����λ��,��0Ϊ��һ������,ȡargsע���е�����,��Ҫ�� "2,5" */
    int[] originalPos() default {};
    /** �жϲ���ʱ������args�е�λ��,��0Ϊ��һ������,ȡargsע���е�����,��Ҫ�� "1,2" */
    int[] handlePos() default {};
    /** �����Ƿ���Դ�Сд,Ĭ�ϲ����� */
    boolean ignoreCase() default false;
    /** ָ����ߵ�Ҫ�� */
    Class<? extends CommandSender> sender() default CommandSender.class;
    /** ָ����������Ȩ�� */
    String[] perm() default {};
}
