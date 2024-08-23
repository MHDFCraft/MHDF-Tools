package cn.ChengZhiYa.MHDFTools.task.player;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.*;
import static cn.ChengZhiYa.MHDFTools.utils.database.FlyUtil.*;

public final class PlayerFlightTask implements Consumer<ScheduledTask> {

    @Override
    public void accept(ScheduledTask task) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            String playerName = player.getName();

            if (flyList.contains(playerName)) {
                int flyTime = getFlyTime(playerName);
                if (flyTime != -999) {
                    if (flyTime > 0) {
                        takeFlyTime(playerName, 1);

                        String titleKey = "FlyTime.CountTime." + flyTime;
                        if (LangFileData.getString(titleKey) != null) {
                            sendTitle(player, i18n(titleKey));
                        }

                        String soundKey = "FlyOffCountTime." + flyTime;
                        if (sound(soundKey) != null) {
                            playSound(player, sound(soundKey));
                        }
                    } else {
                        flyList.remove(playerName);
                        removeFly(playerName);
                        player.setAllowFlight(false);
                    }
                }
            }
        }
    }
}