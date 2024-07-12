package cn.ChengZhiYa.MHDFTools.task.server;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.Placeholder;

public final class ServerTimeMessageTask extends BukkitRunnable {
    int a = 0;
    JavaPlugin plugin = PluginLoader.INSTANCE.getPlugin();

    private void broadcastMessage(String message) {
        if (plugin.getConfig().getBoolean("TimeMessageSettings.ConsoleDisplayed")) {
            Bukkit.broadcast(message,"MHDFTools.TimeMessage");
        } else {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("MHDFTools.TimeMessage")) {
                    player.sendMessage(message);
                }
            }
        }
    }

    public void run() {
        if (plugin.getConfig().getBoolean("TimeMessageSettings.Enable")) {
            if (Bukkit.getOnlinePlayers().size() >= plugin.getConfig().getInt("TimeMessageSettings.MinPlayer")) {
                String message;
                List<String> messageList = plugin.getConfig().getStringList("TimeMessageSettings.Message");
                if (plugin.getConfig().getBoolean("TimeMessageSettings.RandomMode")) {
                    Random random = new Random();
                    message = messageList.get(random.nextInt(messageList.size()));
                } else {
                    message = messageList.get(a);
                    a = (a + 1) % messageList.size();
                }
                broadcastMessage(Placeholder(null, message));
            }
        }
    }
}