package cn.ChengZhiYa.MHDFTools.task.player;

import cn.ChengZhiYa.MHDFTools.entity.SuperLocation;
import cn.ChengZhiYa.MHDFTools.utils.BungeeCordUtil;
import cn.ChengZhiYa.MHDFTools.utils.SpigotUtil;
import cn.ChengZhiYa.MHDFTools.utils.map.MapUtil;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import static cn.ChengZhiYa.MHDFTools.utils.BungeeCordUtil.ServerName;

public final class PlayerBackTask implements Consumer<ScheduledTask> {

    @Override
    public void accept(ScheduledTask task) {
        Set<Object> keySet = MapUtil.getIntHashMap().keySet();
        for (Object Key : keySet) {
            if (!Key.toString().contains("_BackDelay")) continue;
            String PlayerName = Key.toString().replaceAll("_BackDelay", "");
            if (Bukkit.getPlayer(PlayerName) != null) {
                Player player = Bukkit.getPlayer(PlayerName);
                SpigotUtil.sendTitle(Objects.requireNonNull(player), SpigotUtil.i18n("TeleportDelay." + MapUtil.getIntHashMap().get(Key)));
                if (MapUtil.getIntHashMap().get(Key) > 0) {
                    SpigotUtil.playSound(Objects.requireNonNull(player), SpigotUtil.sound("TeleportDelay." + MapUtil.getIntHashMap().get(Key)));
                    MapUtil.getIntHashMap().put(Key, MapUtil.getIntHashMap().get(Key) - 1);
                    continue;
                }
                SpigotUtil.playSound(Objects.requireNonNull(player), SpigotUtil.sound("TeleportSound"));
                BungeeCordUtil.tpPlayerTo(PlayerName, ServerName, MapUtil.getLocationHashMap().get(PlayerName + "_DeathLocation"));
                MapUtil.getLocationHashMap().put(PlayerName + "_UnBackLocation", new SuperLocation(Objects.requireNonNull(player).getLocation()));
                player.sendMessage(SpigotUtil.i18n("Back.Done"));
                MapUtil.getIntHashMap().remove(Key);
                continue;
            }
            MapUtil.getIntHashMap().remove(Key);
        }
    }
}
