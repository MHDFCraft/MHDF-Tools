package cn.ChengZhiYa.MHDFTools.task;

import cn.ChengZhiYa.MHDFTools.hashmap.IntHasMap;
import cn.ChengZhiYa.MHDFTools.hashmap.LocationHasMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.util.BCUtil.ServerName;
import static cn.ChengZhiYa.MHDFTools.util.BCUtil.TpPlayerTo;
import static cn.ChengZhiYa.MHDFTools.util.Util.*;

public final class Back extends BukkitRunnable {
    @Override
    public void run() {
        for (Object Key : IntHasMap.getHasMap().keySet()) {
            if (Key.toString().contains("_BackDelay")) {
                String PlayerName = Key.toString().replaceAll("_BackDelay", "");
                if (Bukkit.getPlayer(PlayerName) != null) {
                    Player player = Bukkit.getPlayer(PlayerName);
                    SendTitle(Objects.requireNonNull(player), i18n("TeleportDelay." + IntHasMap.getHasMap().get(Key)));
                    if (IntHasMap.getHasMap().get(Key) > 0) {
                        PlaySound(Objects.requireNonNull(player), sound("TeleportDelay." + IntHasMap.getHasMap().get(Key)));
                        IntHasMap.getHasMap().put(Key, IntHasMap.getHasMap().get(Key) - 1);
                    } else {
                        PlaySound(Objects.requireNonNull(player), sound("TeleportSound"));
                        TpPlayerTo(PlayerName, ServerName, LocationHasMap.getHasMap().get(PlayerName + "_DeathLocation"));
                        LocationHasMap.getHasMap().put(PlayerName + "_UnBackLocation", Objects.requireNonNull(player).getLocation());
                        player.sendMessage(i18n("Back.Done"));
                        IntHasMap.getHasMap().remove(Key);
                    }
                } else {
                    IntHasMap.getHasMap().remove(Key);
                }
            }
        }
    }
}
