package cn.ChengZhiYa.MHDFTools.task.player;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import com.github.Anon8281.universalScheduler.UniversalRunnable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.i18n;
import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.ifLogin;
import static cn.ChengZhiYa.MHDFTools.utils.database.LoginUtil.loginExists;

public final class PlayerLoginTask extends UniversalRunnable {

    @Override
    public void run() {
        if (PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("LoginSystemSettings.Enable")) {
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
