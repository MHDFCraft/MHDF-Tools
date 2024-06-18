package cn.ChengZhiYa.MHDFTools.task;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.util.Util.*;
import static cn.ChengZhiYa.MHDFTools.util.database.FlyUtil.*;

public final class Fly extends BukkitRunnable {
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (InFlyList.contains(player.getName()) && getFlyTime(player.getName()) != -999) {
                takeFlyTime(player.getName(), 1);
                if (LangFileData.getString("FlyTime.CountTime." + getFlyTime(player.getName())) != null) {
                    sendTitle(Objects.requireNonNull(player), i18n("FlyTime.CountTime." + getFlyTime(player.getName())));
                }
                if (sound("FlyOffCountTime." + getFlyTime(player.getName())) != null) {
                    playSound(Objects.requireNonNull(player), sound("FlyOffCountTime." + getFlyTime(player.getName())));
                }
                if (getFlyTime(player.getName()) <= 0) {
                    InFlyList.remove(player.getName());
                    removeFly(player.getName());
                    player.setAllowFlight(false);
                }
            }
        }
    }
}
