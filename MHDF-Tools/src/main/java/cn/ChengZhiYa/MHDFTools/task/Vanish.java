package cn.ChengZhiYa.MHDFTools.task;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Method;

import static cn.ChengZhiYa.MHDFTools.utils.Util.VanishList;

public final class Vanish extends BukkitRunnable {

    private static Method hidePlayerMethod;

    static {
        try {
            hidePlayerMethod = Player.class.getDeclaredMethod("hidePlayer", Plugin.class, Player.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        if (MHDFTools.instance.getConfig().getBoolean("VanishSettings.Enable")) {
            for (String vanishPlayer : VanishList) {
                Player player = Bukkit.getPlayerExact(vanishPlayer);
                if (player != null) {
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        if (hidePlayerMethod != null) {
                            try {
                                hidePlayerMethod.invoke(onlinePlayer, MHDFTools.instance, player);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            onlinePlayer.hidePlayer(player);
                        }
                    }
                }
            }
        }
    }
}