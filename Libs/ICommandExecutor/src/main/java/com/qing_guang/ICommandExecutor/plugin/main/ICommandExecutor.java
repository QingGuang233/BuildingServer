package com.qing_guang.ICommandExecutor.plugin.main;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * ָ�����
 */
public class ICommandExecutor implements CommandExecutor {

    private Set<Class<?>> classes = new HashSet<>();
    private Consumer<ExecuteFailData> handler = (ignored) -> {};

    /**
     * ���һ������ָ���������,ֻ��⹫���Ĳ����Ǿ�̬�ķ���
     * ��һ��{@code CommandCondition}��ǵķ�����originalPos��handlePos�ĳ��Ȳ�ͬ,��˷����ᱻ����
     * @param clazz Ҫ��ӵ���
     */
    public void addClass(Class<?> clazz){
        classes.add(clazz);
    }

    /**
     * ɾ��һ����
     * @param clazz Ҫɾ������
     */
    public void removeClass(Class<?> clazz){
        classes.remove(clazz);
    }

    /**
     * ����ָ��ִ��ʧ��ʱ�Ĵ�����
     * @param handler ������
     */
    public void failureHandler(Consumer<ExecuteFailData> handler){
        this.handler = handler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        for(Class<?> clazz : classes){
            a: for(Method method : clazz.getMethods()){
                CommandCondition cond = method.getAnnotation(CommandCondition.class);
                if(cond == null || cond.originalPos().length != cond.handlePos().length){
                    continue;
                }
                if(cond.argsLen() != args.length && cond.argsLen() >= 0){
                    continue;
                }
                Set<Integer> compared = new HashSet<>();
                for(int i = 0;i < cond.handlePos().length;i++){
                    if(!equals(args[cond.originalPos()[i]],cond.args()[cond.handlePos()[i]],cond.ignoreCase())){
                        continue a;
                    }
                    compared.add(cond.handlePos()[i]);
                }
                for(int i = 0;i < cond.args().length;i++){
                    if(!equals(cond.args()[i],args[i],cond.ignoreCase()) && !compared.contains(i)){
                        continue a;
                    }
                }
                if(!cond.sender().isAssignableFrom(sender.getClass())){
                    ExecuteFailData data = new ExecuteFailData(ExecuteFailData.Reason.IMPROPER_SENDER,args,sender);
                    handler.accept(data);
                    return true;
                }
                for(String perm : cond.perm()){
                    if(!sender.hasPermission(perm)){
                        ExecuteFailData data = new ExecuteFailData(ExecuteFailData.Reason.PERMISSION_DENIED,args,sender);
                        handler.accept(data);
                        return true;
                    }
                }
                try {
                    method.invoke(null,sender,args);
                } catch (InvocationTargetException e) {
                    System.out.println("ָ��ִ���з�������,��������: ");
                    e.printStackTrace();
                } catch (Exception ignored){}
                return true;
            }
        }
        ExecuteFailData data = new ExecuteFailData(ExecuteFailData.Reason.WRONG_ARGS,args,sender);
        handler.accept(data);
        return true;
    }

    private boolean equals(String str1,String str2,boolean ignoreCase){
        return ignoreCase ? str1.equalsIgnoreCase(str2) : str1.equals(str2);
    }

}
