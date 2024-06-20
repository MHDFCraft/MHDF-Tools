package cn.ChengZhiYa.MHDFTools.manager.init.start;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.manager.init.Invitable;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.checkUpdate;

public class UpdateCheck implements Invitable {
    @Override
    public void start() {
        if (MHDFTools.instance.getConfig().getBoolean("CheckVersion")) {
            checkUpdate();
        }
    }
}