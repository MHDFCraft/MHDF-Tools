package cn.ChengZhiYa.MHDFTools.tasks;

import cn.ChengZhiYa.MHDFTools.map.IntHasMap;
import cn.ChengZhiYa.MHDFTools.map.LocationHasMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.utils.BCUtil.ServerName;
import static cn.ChengZhiYa.MHDFTools.utils.BCUtil.TpPlayerTo;
import static cn.ChengZhiYa.MHDFTools.utils.Util.*;

public final class TpBack extends BukkitRunnable {
    @Override
    public void run() {
        for (Object Key : IntHasMap.getHasMap().keySet()) {
            if (Key.toString().contains("_TpBackDelay")) {
                String PlayerName = Key.toString().replaceAll("_TpBackDelay", "");
                if (Bukkit.getPlayer(PlayerName) != null) {
                    Player player = Bukkit.getPlayer(PlayerName);
                    SendTitle(Objects.requireNonNull(player), i18n("TeleportDelay." + IntHasMap.getHasMap().get(Key)));
                    if (IntHasMap.getHasMap().get(Key) > 0) {
                        PlaySound(Objects.requireNonNull(player), sound("TeleportDelay." + IntHasMap.getHasMap().get(Key)));
                        IntHasMap.getHasMap().put(Key, IntHasMap.getHasMap().get(Key) - 1);
                    } else {
                        PlaySound(Objects.requireNonNull(player), sound("TeleportSound"));
                        TpPlayerTo(PlayerName, ServerName, LocationHasMap.getHasMap().get(PlayerName + "_TpBackLocation"));
                        LocationHasMap.getHasMap().put(PlayerName + "_UnBackLocation", Objects.requireNonNull(player).getLocation());
                        player.sendMessage(i18n("TpBack.Done"));
                        IntHasMap.getHasMap().remove(Key);
                    }
                } else {
                    IntHasMap.getHasMap().remove(Key);
                }
            }
        }
    }
}
