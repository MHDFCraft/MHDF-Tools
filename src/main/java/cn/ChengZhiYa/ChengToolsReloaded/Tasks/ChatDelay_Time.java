package cn.ChengZhiYa.ChengToolsReloaded.Tasks;

import cn.ChengZhiYa.ChengToolsReloaded.HashMap.IntHashMap;
import cn.ChengZhiYa.ChengToolsReloaded.main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ChatDelay_Time extends BukkitRunnable {
    main pluginmain;

    public ChatDelay_Time(main main) {
        this.pluginmain = main;
    }

    public void run() {
        if (main.main.getConfig().getBoolean("ChatSettings.ChatDelayEnable")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (IntHashMap.Get(player.getName() + "_ChatDelayTime") == null) {
                    return;
                }
                if (IntHashMap.Get(player.getName() + "_ChatDelayTime") <= 0) {
                    IntHashMap.Remove(player.getName() + "_ChatDelayTime");
                    return;
                }
                if (IntHashMap.Get(player.getName() + "_ChatDelayTime") > 0) {
                    IntHashMap.Set(player.getName() + "_ChatDelayTime", IntHashMap.Get(player.getName() + "_ChatDelayTime") - 1);
                    return;
                }
            }
        } else {
            this.cancel();
        }
    }
}
