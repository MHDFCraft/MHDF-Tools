package cn.ChengZhiYa.MHDFTools.manager.init.stop;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.hooks.PlaceholderAPI;
import cn.ChengZhiYa.MHDFTools.manager.init.Invitable;

public class stopPlaceholderAPI implements Invitable {
    @Override
    public void start() {
        if (PluginLoader.INSTANCE.isHasPlaceholderAPI()) {
            new PlaceholderAPI().unregister();
        }
    }
}
