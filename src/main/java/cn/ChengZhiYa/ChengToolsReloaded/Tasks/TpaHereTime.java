package cn.ChengZhiYa.ChengToolsReloaded.Tasks;

import cn.ChengZhiYa.ChengToolsReloaded.HashMap.IntHasMap;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHasMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Util.getLang;

public final class TpaHereTime extends BukkitRunnable {
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (IntHasMap.getHasMap().get(player.getName() + "_TPAHereTime") != null && StringHasMap.getHasMap().get(player.getName() + "_TPAHerePlayerName") != null) {
                String PlayerName = StringHasMap.getHasMap().get(player.getName() + "_TPAHerePlayerName");
                int Time = IntHasMap.getHasMap().get(player.getName() + "_TPAHereTime");
                if (Bukkit.getPlayer(PlayerName) != null) {
                    if (Time >= 0) {
                        IntHasMap.getHasMap().put(player.getName() + "_TPAHereTime", Time - 1);
                    } else {
                        player.sendMessage(getLang("TpaHere.TimeOutDone", PlayerName));
                        Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).sendMessage(getLang("TpaHere.TimeOut", player.getName()));
                        IntHasMap.getHasMap().remove(player.getName() + "_TPAHereTime");
                        StringHasMap.getHasMap().remove(player.getName() + "_TPAHerePlayerName");
                    }
                } else {
                    player.sendMessage(getLang("TpaHere.Offline", PlayerName));
                    IntHasMap.getHasMap().remove(player.getName() + "_TPAHereTime");
                    StringHasMap.getHasMap().remove(player.getName() + "_TPAHerePlayerName");
                }
            }
        }
    }
}
