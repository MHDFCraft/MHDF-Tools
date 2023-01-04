package cn.ChengZhiYa.ChengToolsReloaded.Tasks;

import cn.ChengZhiYa.ChengToolsReloaded.main;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public class TimeMessage_Task extends BukkitRunnable {
    main pluginmain;

    private int i = 0;

    public TimeMessage_Task(main main) {
        this.pluginmain = main;
    }

    private static int order(int i) {
        i++;
        return i;
    }

    public void run() {
        if (main.main.getConfig().getBoolean("TimeMessageSettings.Enable"))
            if (this.i < main.main.getConfig().getStringList("TimeMessageSettings.Message").size()) {
                if (main.main.getConfig().getBoolean("TimeMessageSettings.ConsoleDisplayed")) {
                    Bukkit.broadcastMessage(ChatColor(PlaceholderAPI.setPlaceholders(null,main.main.getConfig().getStringList("TimeMessageSettings.Message").get(this.i))));
                }else {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendMessage(ChatColor(PlaceholderAPI.setPlaceholders(null,main.main.getConfig().getStringList("TimeMessageSettings.Message").get(this.i))));
                    }
                }
                this.i = order(this.i);
            } else {
                this.i = 0;
                if (main.main.getConfig().getBoolean("TimeMessageSettings.ConsoleDisplayed")) {
                    Bukkit.broadcastMessage(ChatColor(PlaceholderAPI.setPlaceholders(null,main.main.getConfig().getStringList("TimeMessageSettings.Message").get(this.i))));
                }else {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendMessage(ChatColor(PlaceholderAPI.setPlaceholders(null,main.main.getConfig().getStringList("TimeMessageSettings.Message").get(this.i))));
                    }
                }
                this.i = order(this.i);
            }
    }
}
