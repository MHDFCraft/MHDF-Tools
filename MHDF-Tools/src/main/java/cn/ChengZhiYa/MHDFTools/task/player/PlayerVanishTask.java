package cn.ChengZhiYa.MHDFTools.task.player;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.utils.database.VanishUtil;
import com.github.Anon8281.universalScheduler.foliaScheduler.FoliaScheduler;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.function.Consumer;

public final class PlayerVanishTask implements Consumer<ScheduledTask> {

    private static final Method HIDE_PLAYER_METHOD;

    static {
        Method method;
        try {
            method = Player.class.getDeclaredMethod("hidePlayer", Plugin.class, Player.class);
        } catch (NoSuchMethodException e) {
            method = null;
        }
        HIDE_PLAYER_METHOD = method;
    }

    @Override
    public void accept(ScheduledTask task) {
        if (PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("VanishSettings.Enable")) {
            for (String vanishPlayerName : VanishUtil.getVanishList()) {
                Player vanishPlayer = Bukkit.getPlayerExact(vanishPlayerName);
                if (vanishPlayer != null) {
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        hidePlayer(onlinePlayer, vanishPlayer);
                    }
                }
            }
        }
    }

    private void hidePlayer(Player onlinePlayer, Player vanishPlayer) {
        if (HIDE_PLAYER_METHOD != null) {
            try {
                new FoliaScheduler(PluginLoader.INSTANCE.getPlugin()).runTask(() -> {
                    try {
                        HIDE_PLAYER_METHOD.invoke(onlinePlayer, PluginLoader.INSTANCE.getPlugin(), vanishPlayer);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Bukkit.getScheduler().runTask(PluginLoader.INSTANCE.getPlugin(), () -> onlinePlayer.hidePlayer(vanishPlayer));
        }
    }
}