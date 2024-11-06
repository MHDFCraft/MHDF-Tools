package cn.ChengZhiYa.MHDFTools.manager.init.start;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.manager.init.Starter;

import static cn.ChengZhiYa.MHDFTools.commands.CommandRegister.startRegister;

public class CommandInit implements Starter {

    @Override
    public void init() {
        startRegister(PluginLoader.INSTANCE.getPlugin());
    }
}
