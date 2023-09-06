package com.qing_guang.ICommandExecutor.plugin.main;

import org.bukkit.command.CommandSender;

/**
 * ָ��ִ��ʧ�ܺ��봦����������
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
     * ָ��ִ��ʧ�ܵ�ԭ��
     * @return ֵ
     */
    public Reason getReason() {
        return reason;
    }

    /**
     * ָ����ߵĲ���
     * @return ֵ
     */
    public String[] getArgs() {
        return args;
    }

    /**
     * ָ�����
     * @return ֵ
     */
    public CommandSender getSender() {
        return sender;
    }

    /**
     * ָ��ִ��ʧ�ܵ�ԭ��
     */
    public enum Reason{
        /** δ�ҵ���������Ӧ��ָ��ִ�з��� */
        WRONG_ARGS,
        /** ָ����߲������� */
        IMPROPER_SENDER,
        /** ָ�����������Ȩ�� */
        PERMISSION_DENIED
    }

}
