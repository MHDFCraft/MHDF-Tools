package cn.ChengZhiYa.MHDFTools.Tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static cn.ChengZhiYa.MHDFTools.Utils.Database.LoginUtil.LoginExists;
import static cn.ChengZhiYa.MHDFTools.Utils.Util.i18n;
import static cn.ChengZhiYa.MHDFTools.Utils.Util.ifLogin;

public final class LoginMessage extends BukkitRunnable {

    public void run() {
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
