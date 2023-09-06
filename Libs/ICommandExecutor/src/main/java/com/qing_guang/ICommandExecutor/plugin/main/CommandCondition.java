package com.qing_guang.ICommandExecutor.plugin.main;

import org.bukkit.command.CommandSender;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 仅在方法上可标注,表示这个方法用于执行指令
 * 被标记的方法只能有两个参数: CommandSender为指令发送者,实际编写时的类型可以根据sender进行变动,String[]为指令的参数
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandCondition {
    /** 参数所需的长度,默认0,若填负数则代表无限制 */
    int argsLen() default 0;
    /** 指令所需参数,若出现: arg1 [自由填写] arg2 [自由填写] [自由填写] arg3 这样的情况,要填写"arg1,arg2,arg3",之后通过originalPos和handlePos来进行调整 */
    String[] args() default {};
    /** 要调整的参数的位置,以0为第一个计数,取args注释中的例子,则要填 "2,5" */
    int[] originalPos() default {};
    /** 判断参数时参数在args中的位置,以0为第一个计数,取args注释中的例子,则要填 "1,2" */
    int[] handlePos() default {};
    /** 参数是否忽略大小写,默认不忽略 */
    boolean ignoreCase() default false;
    /** 指令发送者的要求 */
    Class<? extends CommandSender> sender() default CommandSender.class;
    /** 指令发送者所需的权限 */
    String[] perm() default {};
}
