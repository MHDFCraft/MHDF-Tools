package cn.ChengZhiYa.MHDFTools.task.player;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.utils.map.MapUtil;
import org.bukkit.scheduler.BukkitRunnable;

import static cn.ChengZhiYa.MHDFTools.utils.BungeeCordUtil.*;
import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.i18n;

public final class PlayerTpaHereTask extends BukkitRunnable {
    @Override
    public void run() {
        if (MHDFTools.instance.getConfig().getBoolean("TpaHereSettings.Enable")) {
            return;
        }
        for (Object Tpa : MapUtil.getStringHashMap().keySet()) {
            if (!Tpa.toString().contains("_TPAHerePlayerName")) continue;

            String playerName = Tpa.toString().replaceAll("_TPAHerePlayerName", "");
            String targetPlayerName = MapUtil.getStringHashMap().get(Tpa);

            if (!ifPlayerOnline(playerName)) {
                CancelTpaHere(playerName);
                continue;
            }

            if (!ifPlayerOnline(targetPlayerName)) {
                SendMessage(playerName, i18n("TpaHere.Offline", targetPlayerName));
                CancelTpaHere(playerName);
                continue;
            }

            int time = MapUtil.getIntHashMap().getOrDefault(playerName + "_TPAHereTime", 0);

            if (time >= 0) {
                MapUtil.getIntHashMap().put(playerName + "_TPAHereTime", time - 1);
            } else {
                SendMessage(playerName, i18n("TpaHere.TimeOutDone", targetPlayerName));
                SendMessage(targetPlayerName, i18n("TpaHere.TimeOut", playerName));
                CancelTpaHere(playerName);
            }
        }
    }
}