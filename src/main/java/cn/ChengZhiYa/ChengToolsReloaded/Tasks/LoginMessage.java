package cn.ChengZhiYa.ChengToolsReloaded.Tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Database.LoginUtil.LoginExists;
import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Util.getLang;
import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Util.getLogin;

public final class LoginMessage extends BukkitRunnable {

    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!getLogin(player)) {
                if (LoginExists(player.getName())) {
                    player.sendMessage(getLang("Login.LoginMessage"));
                } else {
                    player.sendMessage(getLang("Login.RegisterMessage"));
                }
            }
        }
    }
}
