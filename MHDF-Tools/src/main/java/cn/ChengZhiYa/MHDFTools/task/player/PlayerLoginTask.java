package cn.ChengZhiYa.MHDFTools.task.player;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.i18n;
import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.ifLogin;
import static cn.ChengZhiYa.MHDFTools.utils.database.LoginUtil.loginExists;

public final class PlayerLoginTask extends BukkitRunnable {

    public void run() {
        if (MHDFTools.instance.getConfig().getBoolean("LoginSystemSettings.Enable")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!ifLogin(player)) {
                    if (loginExists(player.getName())) {
                        player.sendMessage(i18n("Login.LoginMessage"));
                    } else {
                        player.sendMessage(i18n("Login.RegisterMessage"));
                    }
                }
            }
        }
    }
}
