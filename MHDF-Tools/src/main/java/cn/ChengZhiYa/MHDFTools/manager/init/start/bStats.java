package cn.ChengZhiYa.MHDFTools.manager.init.start;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.manager.init.Invitable;

public class bStats implements Invitable {
    @Override
    public void start() {
        if (PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("bStats")) {
            new cn.ChengZhiYa.MHDFTools.hooks.bStats(PluginLoader.INSTANCE.getPlugin(), 17154);
        }
    }
}
