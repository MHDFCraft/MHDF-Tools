package cn.chengzhiya.mhdftools.task;

import cn.chengzhiya.mhdftools.MHDFTools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static cn.chengzhiya.mhdftools.util.Util.i18n;
import static cn.chengzhiya.mhdftools.util.Util.ifLogin;
import static cn.chengzhiya.mhdftools.util.database.LoginUtil.LoginExists;

public final class LoginMessage extends BukkitRunnable {

    public void run() {
        if (MHDFTools.instance.getConfig().getBoolean("LoginSystemSettings.Enable")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!ifLogin(player)) {
                    if (LoginExists(player.getName())) {
                        player.sendMessage(i18n("Login.LoginMessage"));
                    } else {
                        player.sendMessage(i18n("Login.RegisterMessage"));
                    }
                }
            }
        }
    }
}
