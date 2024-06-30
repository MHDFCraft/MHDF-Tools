package cn.ChengZhiYa.MHDFTools.task.player;

import cn.ChengZhiYa.MHDFTools.utils.BungeeCordUtil;
import cn.ChengZhiYa.MHDFTools.utils.SpigotUtil;
import cn.ChengZhiYa.MHDFTools.utils.map.MapUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;
import java.util.Set;

public final class PlayerTpBackTask extends BukkitRunnable {
    @Override
    public void run() {
        Set<Object> keySet = MapUtil.getIntHashMap().keySet();
        for (Object Key : keySet) {
            if (!Key.toString().contains("_TpBackDelay")) continue;
            String PlayerName = Key.toString().replaceAll("_TpBackDelay", "");
            if (Bukkit.getPlayer(PlayerName) != null) {
                Player player = Bukkit.getPlayer(PlayerName);
                SpigotUtil.sendTitle(Objects.requireNonNull(player), SpigotUtil.i18n("TeleportDelay." + MapUtil.getIntHashMap().get(Key)));
                if (MapUtil.getIntHashMap().get(Key) > 0) {
                    SpigotUtil.playSound(Objects.requireNonNull(player), SpigotUtil.sound("TeleportDelay." + MapUtil.getIntHashMap().get(Key)));
                    MapUtil.getIntHashMap().put(Key, MapUtil.getIntHashMap().get(Key) - 1);
                    continue;
                }
                SpigotUtil.playSound(Objects.requireNonNull(player), SpigotUtil.sound("TeleportSound"));
                BungeeCordUtil.TpPlayerTo(PlayerName, BungeeCordUtil.ServerName, MapUtil.getLocationHashMap().get(PlayerName + "_TpBackLocation"));
                MapUtil.getLocationHashMap().put(PlayerName + "_UnBackLocation", Objects.requireNonNull(player).getLocation());
                player.sendMessage(SpigotUtil.i18n("TpBack.Done"));
                MapUtil.getIntHashMap().remove(Key);
                continue;
            }
            MapUtil.getIntHashMap().remove(Key);
        }
    }
}
