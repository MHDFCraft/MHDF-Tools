package cn.ChengZhiYa.MHDFTools.Tasks;

import cn.ChengZhiYa.MHDFTools.HashMap.IntHasMap;
import cn.ChengZhiYa.MHDFTools.HashMap.StringHasMap;
import org.bukkit.scheduler.BukkitRunnable;

import static cn.ChengZhiYa.MHDFTools.Utils.BCUtil.*;
import static cn.ChengZhiYa.MHDFTools.Utils.Util.i18n;

public final class TpaHereTime extends BukkitRunnable {
    @Override
    public void run() {
        for (Object Tpa : StringHasMap.getHasMap().keySet()) {
            if (Tpa.toString().contains("_TPAHerePlayerName")) {
                String PlayerName = Tpa.toString().replaceAll("_TPAHerePlayerName", "");
                String TagerPlayerName = StringHasMap.getHasMap().get(Tpa);
                int Time = IntHasMap.getHasMap().get(PlayerName + "_TPAHereTime");
                if (ifPlayerOnline(TagerPlayerName)) {
                    if (Time >= 0) {
                        IntHasMap.getHasMap().put(TagerPlayerName + "_TPATime", Time - 1);
                    } else {
                        SendMessage(PlayerName, i18n("TpaHere.TimeOutDone", TagerPlayerName));
                        SendMessage(TagerPlayerName, i18n("TpaHere.TimeOut", PlayerName));
                        CancelTpaHere(PlayerName);
                    }
                } else {
                    SendMessage(PlayerName, i18n("TpaHere.Offline", TagerPlayerName));
                    CancelTpaHere(PlayerName);
                }
            }
        }
//        for (Player player : Bukkit.getOnlinePlayers()) {
//            if (IntHasMap.getHasMap().get(player.getName() + "_TPAHereTime") != null && StringHasMap.getHasMap().get(player.getName() + "_TPAHerePlayerName") != null) {
//                String PlayerName = StringHasMap.getHasMap().get(player.getName() + "_TPAHerePlayerName");
//                int Time = IntHasMap.getHasMap().get(player.getName() + "_TPAHereTime");
//                if (ifPlayerOnline(PlayerName)) {
//                    if (Time >= 0) {
//                        IntHasMap.getHasMap().put(player.getName() + "_TPAHereTime", Time - 1);
//                    } else {
//                        player.sendMessage(i18n("TpaHere.TimeOutDone", PlayerName));
//                        SendMessage(PlayerName, i18n("Tpa.TimeOut", player.getName()));
//                        IntHasMap.getHasMap().remove(player.getName() + "_TPAHereTime");
//                        StringHasMap.getHasMap().remove(player.getName() + "_TPAHerePlayerName");
//                    }
//                } else {
//                    player.sendMessage(i18n("TpaHere.Offline", PlayerName));
//                    IntHasMap.getHasMap().remove(player.getName() + "_TPAHereTime");
//                    StringHasMap.getHasMap().remove(player.getName() + "_TPAHerePlayerName");
//                }
//            }
//        }
    }
}
