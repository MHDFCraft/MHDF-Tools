package cn.ChengZhiYa.MHDFTools.Tasks;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.Utils.Util.VanishList;
import static cn.ChengZhiYa.MHDFTools.Utils.Util.getNMSVersion;

public final class Vanish extends BukkitRunnable {
    @Override
    public void run() {
        if (MHDFTools.instance.getConfig().getBoolean("InvseeSettings.Enable")) {
            if (!VanishList.isEmpty()) {
                for (String VanishPlayer : VanishList) {
                    if (Bukkit.getPlayer(VanishPlayer) != null) {
                        Player player = Bukkit.getPlayer(VanishPlayer);
                        for (Player OnlinePlayer : Bukkit.getOnlinePlayers()) {
                            if (Integer.parseInt(Objects.requireNonNull(getNMSVersion()).split("_")[1]) <= 12) {
                                OnlinePlayer.hidePlayer(Objects.requireNonNull(player));
                            } else {
                                OnlinePlayer.hidePlayer(MHDFTools.instance, Objects.requireNonNull(player));
                            }
                        }
                    }
                }
            }
        }
    }
}
