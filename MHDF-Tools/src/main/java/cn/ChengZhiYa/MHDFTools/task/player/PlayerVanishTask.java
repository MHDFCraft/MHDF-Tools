package cn.ChengZhiYa.MHDFTools.task.player;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.utils.database.VanishUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class PlayerVanishTask extends BukkitRunnable {
    @Override
    public void run() {
        if (MHDFTools.instance.getConfig().getBoolean("VanishSettings.Enable")) {
            for (String vanishPlayer : VanishUtil.getVanishList()) {
                Player player = Bukkit.getPlayerExact(vanishPlayer);
                if (player != null) {
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        try {
                            Player.class.getDeclaredMethod("hidePlayer", Plugin.class, Player.class);
                            onlinePlayer.hidePlayer(MHDFTools.instance, player);
                        } catch (NoSuchMethodException e) {
                            onlinePlayer.hidePlayer(player);
                        }
                    }
                }
            }
        }
    }
}