package com.qing_guang.ICommandExecutor.plugin.main;

import org.bukkit.command.CommandSender;

/**
 * 指令执行失败后传入处理方法的数据
 */
public class ExecuteFailData {

    private Reason reason;
    private String[] args;
    private CommandSender sender;

    /** for some specific case */
    public ExecuteFailData(){}

    /**
     * default
     * @param reason reason
     * @param args args
     * @param sender sender
     */
    public ExecuteFailData(Reason reason, String[] args, CommandSender sender) {
        this.reason = reason;
        this.args = args;
        this.sender = sender;
    }

    /**
     * 指令执行失败的原因
     * @return 值
     */
    public Reason getReason() {
        return reason;
    }

    /**
     * 指令发送者的参数
     * @return 值
     */
    public String[] getArgs() {
        return args;
    }

    /**
     * 指令发送者
     * @return 值
     */
    public CommandSender getSender() {
        return sender;
    }

    /**
     * 指令执行失败的原因
     */
    public enum Reason{
        /** 未找到参数所对应的指令执行方法 */
        WRONG_ARGS,
        /** 指令发送者不被允许 */
        IMPROPER_SENDER,
        /** 指令发送者无所需权限 */
        PERMISSION_DENIED
    }

}
