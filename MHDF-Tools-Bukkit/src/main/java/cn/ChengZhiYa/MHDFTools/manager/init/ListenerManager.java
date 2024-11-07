package cn.ChengZhiYa.MHDFTools.manager.init;

import cn.ChengZhiYa.MHDFTools.Main;
import cn.ChengZhiYa.MHDFTools.listener.AbstractListener;
import cn.ChengZhiYa.MHDFTools.manager.interfaces.Init;
import org.bukkit.Bukkit;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;

public final class ListenerManager implements Init {
    /**
     * 注册所有启用的监听器
     */
    @Override
    public void init() {
        try {
            Reflections reflections = new Reflections(AbstractListener.class.getPackageName());

            for (Class<? extends AbstractListener> clazz : reflections.getSubTypesOf(AbstractListener.class)) {
                if (!Modifier.isAbstract(clazz.getModifiers())) {
                    AbstractListener abstractListener = clazz.getDeclaredConstructor().newInstance();
                    if (abstractListener.isEnable()) {
                        Bukkit.getPluginManager().registerEvents(abstractListener, Main.instance);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
