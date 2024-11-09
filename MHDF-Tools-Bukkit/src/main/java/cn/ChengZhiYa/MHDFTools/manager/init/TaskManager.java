package cn.ChengZhiYa.MHDFTools.manager.init;

import cn.ChengZhiYa.MHDFTools.Main;
import cn.ChengZhiYa.MHDFTools.manager.interfaces.Init;
import cn.ChengZhiYa.MHDFTools.task.AbstractTask;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;

public final class TaskManager implements Init {
    /**
     * 注册所有启用的计划任务
     */
    @Override
    public void init() {
        try {
            Reflections reflections = new Reflections(AbstractTask.class.getPackageName());

            for (Class<? extends AbstractTask> clazz : reflections.getSubTypesOf(AbstractTask.class)) {
                if (!Modifier.isAbstract(clazz.getModifiers())) {
                    AbstractTask abstractTask = clazz.getDeclaredConstructor().newInstance();
                    if (abstractTask.isEnable()) {
                        abstractTask.runTaskTimerAsynchronously(Main.instance, 0L, abstractTask.getTime());
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
