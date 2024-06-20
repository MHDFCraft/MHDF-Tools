package cn.ChengZhiYa.MHDFTools.task;

import cn.ChengZhiYa.MHDFTools.utils.map.MapUtil;
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
        for (Object Key : MapUtil.getIntHasMap().keySet()) {
            if (Key.toString().contains("_TpBackDelay")) {
                String PlayerName = Key.toString().replaceAll("_TpBackDelay", "");
                if (Bukkit.getPlayer(PlayerName) != null) {
                    Player player = Bukkit.getPlayer(PlayerName);
                    sendTitle(Objects.requireNonNull(player), i18n("TeleportDelay." + MapUtil.getIntHasMap().get(Key)));
                    if (MapUtil.getIntHasMap().get(Key) > 0) {
                        playSound(Objects.requireNonNull(player), sound("TeleportDelay." + MapUtil.getIntHasMap().get(Key)));
                        MapUtil.getIntHasMap().put(Key, MapUtil.getIntHasMap().get(Key) - 1);
                    } else {
                        playSound(Objects.requireNonNull(player), sound("TeleportSound"));
                        TpPlayerTo(PlayerName, ServerName, MapUtil.getLocationHasMap().get(PlayerName + "_TpBackLocation"));
                        MapUtil.getLocationHasMap().put(PlayerName + "_UnBackLocation", Objects.requireNonNull(player).getLocation());
                        player.sendMessage(i18n("TpBack.Done"));
                        MapUtil.getIntHasMap().remove(Key);
                    }
                } else {
                    MapUtil.getIntHasMap().remove(Key);
                }
            }
        }
    }
}
