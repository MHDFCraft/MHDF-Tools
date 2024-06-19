package cn.chengzhiya.mhdftools.task;

import cn.chengzhiya.mhdftools.MHDFTools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

import static cn.chengzhiya.mhdftools.util.Util.VanishList;

public final class Vanish extends BukkitRunnable {
    @Override
    public void run() {
        if (MHDFTools.instance.getConfig().getBoolean("InvseeSettings.Enable")) {
            if (!VanishList.isEmpty()) {
                for (String VanishPlayer : VanishList) {
                    if (Bukkit.getPlayer(VanishPlayer) != null) {
                        Player player = Bukkit.getPlayer(VanishPlayer);
                        for (Player OnlinePlayer : Bukkit.getOnlinePlayers()) {
                            try {
                                Player.class.getDeclaredMethod("hidePlayer", Plugin.class, Player.class);
                                OnlinePlayer.hidePlayer(MHDFTools.instance, Objects.requireNonNull(player));
                            } catch (NoSuchMethodException e) {
                                OnlinePlayer.hidePlayer(Objects.requireNonNull(player));
                            }
                        }
                    }
                }
            }
        }
    }
}
