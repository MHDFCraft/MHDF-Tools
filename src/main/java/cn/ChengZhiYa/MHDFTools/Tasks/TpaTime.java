package cn.ChengZhiYa.MHDFTools.Tasks;

import cn.ChengZhiYa.MHDFTools.HashMap.IntHasMap;
import cn.ChengZhiYa.MHDFTools.HashMap.StringHasMap;
import org.bukkit.scheduler.BukkitRunnable;

import static cn.ChengZhiYa.MHDFTools.Utils.BCUtil.*;
import static cn.ChengZhiYa.MHDFTools.Utils.Util.i18n;

public final class TpaTime extends BukkitRunnable {
    @Override
    public void run() {
        for (Object Tpa : StringHasMap.getHasMap().keySet()) {
            if (Tpa.toString().contains("_TPAPlayerName")) {
                String PlayerName = Tpa.toString().replaceAll("_TPAPlayerName", "");
                String TagerPlayerName = StringHasMap.getHasMap().get(Tpa);
                int Time = IntHasMap.getHasMap().get(PlayerName + "_TPATime");
                if (ifPlayerOnline(TagerPlayerName)) {
                    if (Time >= 0) {
                        IntHasMap.getHasMap().put(TagerPlayerName + "_TPATime", Time - 1);
                    } else {
                        SendMessage(PlayerName, i18n("Tpa.TimeOutDone", TagerPlayerName));
                        SendMessage(TagerPlayerName, i18n("Tpa.TimeOut", PlayerName));
                        CancelTpa(PlayerName);
                    }
                } else {
                    SendMessage(PlayerName, i18n("Tpa.Offline", TagerPlayerName));
                    CancelTpa(PlayerName);
                }
            }
        }
    }
}
