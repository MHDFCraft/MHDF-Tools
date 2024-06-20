package cn.ChengZhiYa.MHDFTools.manager.init.start;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.manager.init.Invitable;

public class bStats implements Invitable {
    @Override
    public void start() {
        if (MHDFTools.instance.getConfig().getBoolean("bStats")) {
            new cn.ChengZhiYa.MHDFTools.hooks.bStats(MHDFTools.instance, 17154);
        }
    }
}
