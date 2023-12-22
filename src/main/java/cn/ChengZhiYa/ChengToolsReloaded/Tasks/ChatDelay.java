package cn.ChengZhiYa.ChengToolsReloaded.Tasks;

import cn.ChengZhiYa.ChengToolsReloaded.HashMap.IntHasMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public final class ChatDelay extends BukkitRunnable {

    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (IntHasMap.getHasMap().get(player.getName() + "_ChatDelayTime") != null) {
                int ChatDelayTime = IntHasMap.getHasMap().get(player.getName() + "_ChatDelayTime");
                int NewChatDelayTime = ChatDelayTime - 1;
                if (ChatDelayTime <= 0) {
                    IntHasMap.getHasMap().remove(player.getName() + "_ChatDelayTime");
                } else {
                    IntHasMap.getHasMap().put(player.getName() + "_ChatDelayTime", NewChatDelayTime);
                }
            }
        }
    }
}
