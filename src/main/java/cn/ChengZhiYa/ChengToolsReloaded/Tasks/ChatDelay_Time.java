package cn.ChengZhiYa.ChengToolsReloaded.Tasks;

import cn.ChengZhiYa.ChengToolsReloaded.HashMap.IntHashMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ChatDelay_Time extends BukkitRunnable {

    public ChatDelay_Time() {
    }

    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (IntHashMap.Get(player.getName() + "_ChatDelayTime") != null) {
                int ChatDelayTime = IntHashMap.Get(player.getName() + "_ChatDelayTime");
                int NewChatDelayTime = ChatDelayTime - 1;
                if (ChatDelayTime <= 0) {
                    IntHashMap.Remove(player.getName() + "_ChatDelayTime");
                } else {
                    IntHashMap.Set(player.getName() + "_ChatDelayTime", NewChatDelayTime);
                }
            }
        }
    }
}
