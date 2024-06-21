package cn.ChengZhiYa.MHDFTools.task.server;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.Placeholder;

public final class ServerTimeTask extends BukkitRunnable {
    int a = 0;
    private void broadcastMessage(String message) {
        if (MHDFTools.instance.getConfig().getBoolean("TimeMessageSettings.ConsoleDisplayed")) {
            Bukkit.broadcastMessage(message);
        } else {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(message);
            }
        }
    }

    public void run() {
        if (MHDFTools.instance.getConfig().getBoolean("TimeMessageSettings.Enable")) {
            String message = Placeholder(null, MHDFTools.instance.getConfig().getStringList("TimeMessageSettings.Message").get(a));
            broadcastMessage(message);
            a = (a + 1) % MHDFTools.instance.getConfig().getStringList("TimeMessageSettings.Message").size();
        }
    }
}