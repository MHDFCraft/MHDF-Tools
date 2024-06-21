package cn.ChengZhiYa.MHDFTools.listeners.player;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.utils.BungeeCord;
import cn.ChengZhiYa.MHDFTools.utils.database.FlyUtil;
import cn.ChengZhiYa.MHDFTools.utils.map.MapUtil;
import cn.ChengZhiYa.MHDFTools.utils.message.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.utils.BungeeCord.getServerName;
import static cn.ChengZhiYa.MHDFTools.utils.database.EconomyUtil.initializationPlayerData;
import static cn.ChengZhiYa.MHDFTools.utils.database.FlyUtil.getFlyTimeHashMap;

public final class PlayerJoinListener implements Listener {
    @EventHandler
    public void On_Event(PlayerJoinEvent event) {
        if (MHDFTools.instance.getConfig().getBoolean("FlySettings.Enable")) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(MHDFTools.instance, () -> {
                getFlyTimeHashMap().remove(event.getPlayer().getName());
                FlyUtil.getFlyTime(event.getPlayer().getName());
            }, 20);
        }
        if (MHDFTools.instance.getConfig().getBoolean("BungeecordSettings.Enable")) {
            getServerName();
        }
        if (MHDFTools.instance.getConfig().getBoolean("BungeecordSettings.Enable")) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(MHDFTools.instance, BungeeCord::getPlayerList, 20);
            Bukkit.getScheduler().runTaskLaterAsynchronously(MHDFTools.instance, BungeeCord::getServerName, 20);
        }
        if (MHDFTools.instance.getConfig().getBoolean("EconomySettings.Enable")) {
            initializationPlayerData(event.getPlayer().getName());
        }
        if (MHDFTools.instance.getConfig().getBoolean("CheckVersion")) {
            try {
                Player player = event.getPlayer();
                if (player.hasPermission("MHDFTools.Op")) {
                    if (!MapUtil.getBooleanHashMap().get("CheckVersionError")) {
                        if (!MapUtil.getBooleanHashMap().get("IsLast")) {
                            player.sendMessage(MessageUtil.colorMessage("&cCheng-Tools不是最新版! 下载链接:https://github.com/ChengZhiNB/Cheng-Tools-Reloaded/releases/"));
                        }
                    } else {
                        player.sendMessage(MessageUtil.colorMessage("&cCheng-Tools无法检查更新!"));
                    }
                }
            } catch (Exception ignored) {
            }
        }
        if (MHDFTools.instance.getConfig().getBoolean("TpaSetting.Enable")) {
            Player player = event.getPlayer();
            File TpaData = new File(MHDFTools.getPlugin(MHDFTools.class).getDataFolder(), "TpaData.yml");
            YamlConfiguration Tpa_Data = YamlConfiguration.loadConfiguration(TpaData);
            Tpa_Data.set(player.getName() + "_TpaPromptMode", MHDFTools.instance.getConfig().getBoolean("TpaSetting.DefaultMode"));
            try {
                Tpa_Data.save(TpaData);
            } catch (IOException ignored) {
            }
        }
        if (MHDFTools.instance.getConfig().getBoolean("ScoreboardSettings.Enable")) {
            Player player = event.getPlayer();
            MapUtil.getScoreboardHashMap().put(player.getName() + "_Scoreboard", Bukkit.getScoreboardManager().getNewScoreboard());
        }
        if (MHDFTools.instance.getConfig().getBoolean("WhiteList.Enable")) {
            if (!MHDFTools.instance.getConfig().getStringList("WhiteList.List").contains(event.getPlayer().getName()) || event.getPlayer().hasPermission(Objects.requireNonNull(MHDFTools.instance.getConfig().getString("WhiteList.Permission")))) {
                event.getPlayer().kickPlayer(MessageUtil.colorMessage(MHDFTools.instance.getConfig().getString("WhiteList.KickMessage")));
            }
        }
        if (MHDFTools.instance.getConfig().getBoolean("PlayerJoinSendMessageSettings.Enable")) {
            event.getPlayer().sendMessage(MessageUtil.colorMessage(MHDFTools.instance.getConfig().getString("PlayerJoinSendMessageSettings.Message"))
                    .replaceAll("%PlayerName%", event.getPlayer().getName())
            );
        }
    }
}
