package cn.ChengZhiYa.MHDFTools.task.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.*;
import static cn.ChengZhiYa.MHDFTools.utils.database.FlyUtil.*;

public final class PlayerFlightTask extends BukkitRunnable {
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            String playerName = player.getName();

            if (InFlyList.contains(playerName)) {
                int flyTime = getFlyTime(playerName);

                if (flyTime != -999) {
                    addFlyTime(playerName, 1);

                    String titleKey = "FlyTime.CountTime." + flyTime;
                    if (LangFileData.getString(titleKey) != null) {
                        sendTitle(player, i18n(titleKey));
                    }

                    String soundKey = "FlyOffCountTime." + flyTime;
                    if (sound(soundKey) != null) {
                        playSound(player, sound(soundKey));
                    }

                    if (flyTime <= 0) {
                        InFlyList.remove(playerName);
                        removeFlyTime(playerName);
                        player.setAllowFlight(false);
                    }
                }
            }
        }
    }
}