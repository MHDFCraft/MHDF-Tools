package cn.ChengZhiYa.MHDFTools.task.player;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.utils.database.VanishUtil;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.function.Consumer;

public final class PlayerVanishTask implements Consumer<ScheduledTask> {

    @Override
    public void accept(ScheduledTask task) {
        if (PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("VanishSettings.Enable")) {
            for (String vanishPlayer : VanishUtil.getVanishList()) {
                Player player = Bukkit.getPlayerExact(vanishPlayer);
                if (player != null) {
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        try {
                            Player.class.getDeclaredMethod("hidePlayer", Plugin.class, Player.class);
                            Bukkit.getRegionScheduler().run(PluginLoader.INSTANCE.getPlugin(), onlinePlayer.getLocation(), t -> onlinePlayer.hidePlayer(PluginLoader.INSTANCE.getPlugin(), player));
                        } catch (NoSuchMethodException e) {
                            Bukkit.getRegionScheduler().run(PluginLoader.INSTANCE.getPlugin(), onlinePlayer.getLocation(), t -> onlinePlayer.hidePlayer(player));
                        }
                    }
                }
            }
        }
    }
}