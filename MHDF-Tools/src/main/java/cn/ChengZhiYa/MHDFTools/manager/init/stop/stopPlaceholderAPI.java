package cn.ChengZhiYa.MHDFTools.manager.init.stop;

import cn.ChengZhiYa.MHDFTools.MHDFPluginLoader;
import cn.ChengZhiYa.MHDFTools.hooks.PlaceholderAPI;
import cn.ChengZhiYa.MHDFTools.manager.init.Invitable;

public class stopPlaceholderAPI implements Invitable {
    @Override
    public void start() {
        if (MHDFPluginLoader.hasPlaceholderAPI) {
            new PlaceholderAPI().unregister();
        }
    }
}
