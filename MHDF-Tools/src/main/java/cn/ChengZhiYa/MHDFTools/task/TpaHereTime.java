package cn.ChengZhiYa.MHDFTools.task;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.utils.map.MapUtil;
import org.bukkit.scheduler.BukkitRunnable;

import static cn.ChengZhiYa.MHDFTools.utils.BCUtil.*;
import static cn.ChengZhiYa.MHDFTools.utils.Util.i18n;

public final class TpaHereTime extends BukkitRunnable {
    @Override
    public void run() {
        if (MHDFTools.instance.getConfig().getBoolean("TpaHereSettings.Enable")) {
            for (Object Tpa : StringHasMap.getHasMap().keySet()) {
                if (Tpa.toString().contains("_TPAHerePlayerName")) {
                    String PlayerName = Tpa.toString().replaceAll("_TPAHerePlayerName", "");
                    String TagerPlayerName = MapUtil.getStringHasMap().get(Tpa);
                    int Time = MapUtil.getIntHasMap().get(PlayerName + "_TPAHereTime");
                    if (ifPlayerOnline(PlayerName)) {
                        if (ifPlayerOnline(TagerPlayerName)) {
                            if (Time >= 0) {
                                MapUtil.getIntHasMap().put(PlayerName + "_TPATime", Time - 1);
                            } else {
                                SendMessage(PlayerName, i18n("TpaHere.TimeOutDone", TagerPlayerName));
                                SendMessage(TagerPlayerName, i18n("TpaHere.TimeOut", PlayerName));
                                CancelTpaHere(PlayerName);
                            }
                        } else {
                            SendMessage(PlayerName, i18n("TpaHere.Offline", TagerPlayerName));
                            CancelTpaHere(PlayerName);
                        }
                    } else {
                        CancelTpaHere(PlayerName);
                    }
                }
            }
        }
    }
}
