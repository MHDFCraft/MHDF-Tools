package cn.ChengZhiYa.MHDFTools.task;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.hashmap.IntHasMap;
import cn.ChengZhiYa.MHDFTools.hashmap.StringHasMap;
import org.bukkit.scheduler.BukkitRunnable;

import static cn.ChengZhiYa.MHDFTools.util.BCUtil.*;
import static cn.ChengZhiYa.MHDFTools.util.Util.i18n;

public final class TpaTime extends BukkitRunnable {
    @Override
    public void run() {
        if (MHDFTools.instance.getConfig().getBoolean("TpaSettings.Enable")) {
            for (Object Tpa : StringHasMap.getHasMap().keySet()) {
                if (Tpa.toString().contains("_TPAPlayerName")) {
                    String PlayerName = Tpa.toString().replaceAll("_TPAPlayerName", "");
                    String TagerPlayerName = StringHasMap.getHasMap().get(Tpa);
                    int Time = IntHasMap.getHasMap().get(PlayerName + "_TPATime");
                    if (ifPlayerOnline(PlayerName)) {
                        if (ifPlayerOnline(TagerPlayerName)) {
                            if (Time >= 0) {
                                IntHasMap.getHasMap().put(PlayerName + "_TPATime", Time - 1);
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
