package cn.ChengZhiYa.MHDFTools.manager.init.start;

import cn.ChengZhiYa.MHDFTools.MHDFPluginLoader;
import cn.ChengZhiYa.MHDFTools.manager.init.Invitable;


public class PlaceholderAPI implements Invitable {
    @Override
    public void start() {
        if (MHDFPluginLoader.hasPlaceholderAPI) {
            new cn.ChengZhiYa.MHDFTools.hooks.PlaceholderAPI().register();
        }
    }
}
