package cn.ChengZhiYa.MHDFTools.task.player;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.i18n;
import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.ifLogin;
import static cn.ChengZhiYa.MHDFTools.utils.database.LoginUtil.loginExists;

public final class PlayerLoginTask implements Consumer<ScheduledTask> {

    @Override
    public void accept(ScheduledTask task) {
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
