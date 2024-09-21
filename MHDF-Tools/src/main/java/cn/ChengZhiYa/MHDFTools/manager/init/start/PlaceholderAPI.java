package cn.ChengZhiYa.MHDFTools.manager.init.start;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.manager.init.Invitable;


public class PlaceholderAPI implements Invitable {
    @Override
    public void init() {
        if (PluginLoader.INSTANCE.isHasPlaceholderAPI()) {
            new cn.ChengZhiYa.MHDFTools.hooks.PlaceholderAPI().register();
        }
    }
}
