package cn.ChengZhiYa.MHDFTools.manager.init.start;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.hooks.bStats;
import cn.ChengZhiYa.MHDFTools.manager.init.Invitable;

public class BStats implements Invitable {
    @Override
    public void start() {
        if (PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("bStats")) {
            new bStats(17154);
        }
    }
}
