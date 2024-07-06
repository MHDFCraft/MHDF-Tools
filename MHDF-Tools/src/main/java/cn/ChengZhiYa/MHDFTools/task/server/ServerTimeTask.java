package cn.ChengZhiYa.MHDFTools.task.server;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.Placeholder;

public final class ServerTimeTask extends BukkitRunnable {
    int a = 0;
    JavaPlugin plugin = PluginLoader.INSTANCE.getPlugin();

    private void broadcastMessage(String message) {
        if (plugin.getConfig().getBoolean("TimeMessageSettings.ConsoleDisplayed")) {
            Bukkit.broadcastMessage(message);
        } else {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(message);
            }
        }
    }

    public void run() {
        if (plugin.getConfig().getBoolean("TimeMessageSettings.Enable")) {
            String message = Placeholder(null, plugin.getConfig().getStringList("TimeMessageSettings.Message").get(a));
            broadcastMessage(message);
            a = (a + 1) % plugin.getConfig().getStringList("TimeMessageSettings.Message").size();
        }
    }
}