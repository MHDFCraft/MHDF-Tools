package cn.ChengZhiYa.MHDFTools.Tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.Utils.Database.FlyUtil.*;
import static cn.ChengZhiYa.MHDFTools.Utils.Util.*;

public final class Fly extends BukkitRunnable {
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (InFlyList.contains(player.getName()) && getFlyTime(player.getName()) != -999) {
                takeFlyTime(player.getName(), 1);
                if (LangFileData.getString("FlyTime.CountTime." + getFlyTime(player.getName())) != null) {
                    SendTitle(Objects.requireNonNull(player), i18n("FlyTime.CountTime." + getFlyTime(player.getName())));
                }
                if (sound("FlyOffCountTime." + getFlyTime(player.getName())) != null) {
                    PlaySound(Objects.requireNonNull(player), sound("FlyOffCountTime." + getFlyTime(player.getName())));
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
