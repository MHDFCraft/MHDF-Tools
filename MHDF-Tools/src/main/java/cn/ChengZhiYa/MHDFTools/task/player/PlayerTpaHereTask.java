package cn.ChengZhiYa.MHDFTools.task.player;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.utils.map.MapUtil;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Map;

import static cn.ChengZhiYa.MHDFTools.utils.BungeeCord.*;
import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.i18n;

public final class PlayerTpaHereTask extends BukkitRunnable {
    @Override
    public void run() {
        if (MHDFTools.instance.getConfig().getBoolean("TpaHereSettings.Enable")) {
            Map<Object, String> stringMap = MapUtil.getStringHasMap();
            Map<Object, Integer> intMap = MapUtil.getIntHasMap();

            for (Object key : new HashSet<>(stringMap.keySet())) {
                if (key.toString().contains("_TPAHerePlayerName")) {
                    String playerName = key.toString().replace("_TPAHerePlayerName", "");
                    String targetPlayerName = stringMap.get(key);

                    if (!ifPlayerOnline(playerName)) {
                        CancelTpaHere(playerName);
                        continue;
                    }

                    if (!ifPlayerOnline(targetPlayerName)) {
                        SendMessage(playerName, i18n("TpaHere.Offline", targetPlayerName));
                        CancelTpaHere(playerName);
                        continue;
                    }

                    int time = intMap.getOrDefault(playerName + "_TPAHereTime", 0);

                    if (time >= 0) {
                        intMap.put(playerName + "_TPAHereTime", time - 1);
                    } else {
                        SendMessage(playerName, i18n("TpaHere.TimeOutDone", targetPlayerName));
                        SendMessage(targetPlayerName, i18n("TpaHere.TimeOut", playerName));
                        CancelTpaHere(playerName);
                    }
                }
            }
        }
    }
}