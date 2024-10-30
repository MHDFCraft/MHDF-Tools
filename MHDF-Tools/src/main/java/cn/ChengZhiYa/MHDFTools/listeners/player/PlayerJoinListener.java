package cn.ChengZhiYa.MHDFTools.listeners.player;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.utils.BungeeCordUtil;
import cn.ChengZhiYa.MHDFTools.utils.database.FlyUtil;
import cn.ChengZhiYa.MHDFTools.utils.map.MapUtil;
import cn.ChengZhiYa.MHDFTools.utils.message.MessageUtil;
import com.github.Anon8281.universalScheduler.foliaScheduler.FoliaScheduler;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.utils.BungeeCordUtil.getServerName;
import static cn.ChengZhiYa.MHDFTools.utils.database.EconomyUtil.initializationPlayerData;

public final class PlayerJoinListener implements Listener {
    JavaPlugin plugin = PluginLoader.INSTANCE.getPlugin();

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        if (plugin.getConfig().getBoolean("FlySettings.Enable")) {
            new FoliaScheduler(plugin).runTaskLater(() -> {
                FlyUtil.getFlyTimeHashMap().remove(event.getPlayer().getName());
                FlyUtil.getFlyTime(event.getPlayer().getName());
            }, 1);
        }
        if (plugin.getConfig().getBoolean("BungeecordSettings.Enable")) {
            getServerName();
        }
        if (plugin.getConfig().getBoolean("BungeecordSettings.Enable")) {
            new FoliaScheduler(plugin).runTaskLater(BungeeCordUtil::getPlayerList, 1);
            new FoliaScheduler(plugin).runTaskLater(BungeeCordUtil::getServerName, 1);
        }
        if (plugin.getConfig().getBoolean("EconomySettings.Enable")) {
            initializationPlayerData(event.getPlayer().getName(), plugin.getConfig().getDouble("EconomySettings.InitialMoney"));
        }
        if (plugin.getConfig().getBoolean("CheckVersion")) {
            try {
                Player player = event.getPlayer();
                if (player.hasPermission("MHDFTools.Op")) {
                    if (!MapUtil.getBooleanHashMap().get("CheckVersionError")) {
                        if (!MapUtil.getBooleanHashMap().get("IsLast")) {
                            player.sendMessage(MessageUtil.colorMessage("&cMHDF-Tools是最新版! 下载链接:https://github.com/MHDFCraft/MHDF-Tools/releases/"));
                        }
                    } else {
                        player.sendMessage(MessageUtil.colorMessage("&cMHDF-Tools无法检查更新!"));
                    }
                }
            } catch (Exception ignored) {
            }
        }
        if (plugin.getConfig().getBoolean("TpaSetting.Enable")) {
            Player player = event.getPlayer();
            File TpaData = new File(plugin.getDataFolder(), "TpaData.yml");
            YamlConfiguration Tpa_Data = YamlConfiguration.loadConfiguration(TpaData);
            Tpa_Data.set(player.getName() + "_TpaPromptMode", plugin.getConfig().getBoolean("TpaSetting.DefaultMode"));
            try {
                Tpa_Data.save(TpaData);
            } catch (IOException ignored) {
            }
        }
        if (plugin.getConfig().getBoolean("WhiteList.Enable")) {
            if (!plugin.getConfig().getStringList("WhiteList.List").contains(event.getPlayer().getName()) || event.getPlayer().hasPermission(Objects.requireNonNull(plugin.getConfig().getString("WhiteList.Permission")))) {
                event.getPlayer().kickPlayer(MessageUtil.colorMessage(plugin.getConfig().getString("WhiteList.KickMessage")));
            }
        }
    }
}
