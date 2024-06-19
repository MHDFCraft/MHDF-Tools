package cn.chengzhiya.mhdftools.task;

import cn.chengzhiya.mhdftools.MHDFTools;
import cn.chengzhiya.mhdftools.hashmap.IntHasMap;
import cn.chengzhiya.mhdftools.hashmap.StringHasMap;
import org.bukkit.scheduler.BukkitRunnable;

import static cn.chengzhiya.mhdftools.util.BCUtil.*;
import static cn.chengzhiya.mhdftools.util.Util.i18n;

public final class TpaHereTime extends BukkitRunnable {
    @Override
    public void run() {
        if (MHDFTools.instance.getConfig().getBoolean("TpaHereSettings.Enable")) {
            for (Object Tpa : StringHasMap.getHasMap().keySet()) {
                if (Tpa.toString().contains("_TPAHerePlayerName")) {
                    String PlayerName = Tpa.toString().replaceAll("_TPAHerePlayerName", "");
                    String TagerPlayerName = StringHasMap.getHasMap().get(Tpa);
                    int Time = IntHasMap.getHasMap().get(PlayerName + "_TPAHereTime");
                    if (ifPlayerOnline(PlayerName)) {
                        if (ifPlayerOnline(TagerPlayerName)) {
                            if (Time >= 0) {
                                IntHasMap.getHasMap().put(PlayerName + "_TPATime", Time - 1);
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
