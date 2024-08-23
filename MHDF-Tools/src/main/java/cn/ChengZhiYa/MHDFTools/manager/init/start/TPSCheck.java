package cn.ChengZhiYa.MHDFTools.manager.init.start;

import cn.ChengZhiYa.MHDFTools.manager.init.Invitable;
import cn.ChengZhiYa.MHDFTools.utils.message.ColorLogs;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.canTPS;

public class TPSCheck implements Invitable {
    @Override
    public void start() {
        if (!canTPS()) {
            ColorLogs.consoleMessage("&f[MHDF-Tools] &f服务端不是Paper或是服务器版本较旧，已关闭自带TPS变量!");
        }
    }
}
