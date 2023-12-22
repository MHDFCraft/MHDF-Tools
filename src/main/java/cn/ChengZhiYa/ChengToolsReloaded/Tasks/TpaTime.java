package cn.ChengZhiYa.ChengToolsReloaded.Tasks;

import cn.ChengZhiYa.ChengToolsReloaded.HashMap.IntHasMap;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHasMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Util.getLang;

public final class TpaTime extends BukkitRunnable {
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (IntHasMap.getHasMap().get(player.getName() + "_TPATime") != null && StringHasMap.getHasMap().get(player.getName() + "_TPAPlayerName") != null) {
                String PlayerName = StringHasMap.getHasMap().get(player.getName() + "_TPAPlayerName");
                int Time = IntHasMap.getHasMap().get(player.getName() + "_TPATime");
                if (Bukkit.getPlayer(PlayerName) != null) {
                    if (Time >= 0) {
                        IntHasMap.getHasMap().put(player.getName() + "_TPATime", Time - 1);
                    } else {
                        player.sendMessage(getLang("Tpa.TimeOutDone", PlayerName));
                        Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).sendMessage(getLang("Tpa.TimeOut", player.getName()));
                        IntHasMap.getHasMap().remove(player.getName() + "_TPATime");
                        StringHasMap.getHasMap().remove(player.getName() + "_TPAPlayerName");
                    }
                } else {
                    player.sendMessage(getLang("Tpa.Offline", PlayerName));
                    IntHasMap.getHasMap().remove(player.getName() + "_TPATime");
                    StringHasMap.getHasMap().remove(player.getName() + "_TPAPlayerName");
                }
            }
        }
    }
}
