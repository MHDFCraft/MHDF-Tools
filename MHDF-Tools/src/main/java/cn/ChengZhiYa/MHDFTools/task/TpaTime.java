package cn.ChengZhiYa.MHDFTools.task;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.utils.map.MapUtil;
import org.bukkit.scheduler.BukkitRunnable;

import static cn.ChengZhiYa.MHDFTools.utils.BCUtil.*;
import static cn.ChengZhiYa.MHDFTools.utils.Util.i18n;

public final class TpaTime extends BukkitRunnable {
    @Override
    public void run() {
        if (MHDFTools.instance.getConfig().getBoolean("TpaSettings.Enable")) {
            for (Object Tpa : MapUtil.getStringHasMap().keySet()) {
                if (Tpa.toString().contains("_TPAPlayerName")) {
                    String PlayerName = Tpa.toString().replaceAll("_TPAPlayerName", "");
                    String TagerPlayerName = MapUtil.getStringHasMap().get(Tpa);
                    int Time = MapUtil.getIntHasMap().get(PlayerName + "_TPATime");
                    if (ifPlayerOnline(PlayerName)) {
                        if (ifPlayerOnline(TagerPlayerName)) {
                            if (Time >= 0) {
                                MapUtil.getIntHasMap().put(PlayerName + "_TPATime", Time - 1);
                            } else {
                                SendMessage(PlayerName, i18n("Tpa.TimeOutDone", TagerPlayerName));
                                SendMessage(TagerPlayerName, i18n("Tpa.TimeOut", PlayerName));
                                CancelTpa(PlayerName);
                            }
                        } else {
                            SendMessage(PlayerName, i18n("Tpa.Offline", TagerPlayerName));
                            CancelTpa(PlayerName);
                        }
                    } else {
                        CancelTpa(PlayerName);
                    }
                }
            }
        }
    }
}
