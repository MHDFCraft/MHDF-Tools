package cn.ChengZhiYa.MHDFTools.task.player;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.utils.database.VanishUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class PlayerVanishTask extends BukkitRunnable {
    @Override
    public void run() {
        if (PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("VanishSettings.Enable")) {
            for (String vanishPlayer : VanishUtil.getVanishList()) {
                Player player = Bukkit.getPlayerExact(vanishPlayer);
                if (player != null) {
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        try {
                            Player.class.getDeclaredMethod("hidePlayer", Plugin.class, Player.class);
                            Bukkit.getScheduler().runTask(PluginLoader.INSTANCE.getPlugin(), () -> onlinePlayer.hidePlayer(PluginLoader.INSTANCE.getPlugin(), player));
                        } catch (NoSuchMethodException e) {
                            Bukkit.getScheduler().runTask(PluginLoader.INSTANCE.getPlugin(), () -> onlinePlayer.hidePlayer(player));
                        }
                    }
                }
            }
        }
    }
}