package cn.ChengZhiYa.MHDFTools.task.player;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.utils.BungeeCordUtil;
import cn.ChengZhiYa.MHDFTools.utils.SpigotUtil;
import cn.ChengZhiYa.MHDFTools.utils.map.MapUtil;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Map;

public final class PlayerTpaTask extends BukkitRunnable {
    @Override
    public void run() {
        if (MHDFTools.instance.getConfig().getBoolean("TpaSettings.Enable")) {
            Map<Object, String> stringHashMap = MapUtil.getStringHashMap();
            if (stringHashMap != null && !stringHashMap.isEmpty()) {
                for (Object key : new HashSet<>(stringHashMap.keySet())) {
                    if (!key.toString().contains("_TPAPlayerName")) continue;
                    String playerName = key.toString().replaceAll("_TPAPlayerName", "");
                    String targetPlayerName = stringHashMap.get(key);
                    Integer time = MapUtil.getIntHashMap().get(playerName + "_TPATime");
                    if (time != null && time >= 0) {
                        MapUtil.getIntHashMap().put(playerName + "_TPATime", time - 1);
                        continue;
                    }

                    if (BungeeCordUtil.ifPlayerOnline(playerName)) {
                        if (BungeeCordUtil.ifPlayerOnline(targetPlayerName)) {
                            BungeeCordUtil.SendMessage(playerName, SpigotUtil.i18n("Tpa.TimeOutDone", targetPlayerName));
                            BungeeCordUtil.SendMessage(targetPlayerName, SpigotUtil.i18n("Tpa.TimeOut", playerName));
                        } else {
                            BungeeCordUtil.SendMessage(playerName, SpigotUtil.i18n("Tpa.Offline", targetPlayerName));
                        }
                        BungeeCordUtil.CancelTpa(playerName);
                    } else {
                        BungeeCordUtil.CancelTpa(playerName);
                    }
                }
            }
        }
    }
}