package cn.ChengZhiYa.MHDFTools.manager.init.start;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.manager.init.Invitable;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.checkUpdate;

public class UpdateCheck implements Invitable {
    @Override
    public void start() {
        if (PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("CheckVersion")) {
            checkUpdate();
        }
    }
}