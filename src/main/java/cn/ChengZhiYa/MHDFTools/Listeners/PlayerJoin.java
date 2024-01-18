package cn.ChengZhiYa.MHDFTools.Listeners;

import cn.ChengZhiYa.MHDFTools.HashMap.BooleanHasMap;
import cn.ChengZhiYa.MHDFTools.HashMap.ScoreboardHasMap;
import cn.ChengZhiYa.MHDFTools.HashMap.StringHasMap;
import cn.ChengZhiYa.MHDFTools.MHDFTools;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.Utils.Database.EconomyUtil.initializationPlayerData;
import static cn.ChengZhiYa.MHDFTools.Utils.Util.ChatColor;
import static cn.ChengZhiYa.MHDFTools.Utils.Util.getNMSVersion;

public final class PlayerJoin implements Listener {
    @EventHandler
    public void On_Event(PlayerJoinEvent event) {
        if (MHDFTools.instance.getConfig().getBoolean("EconomySettings.Enable")) {
            initializationPlayerData(event.getPlayer().getName());
        }
        if (MHDFTools.instance.getConfig().getBoolean("CheckVersion")) {
            Player player = event.getPlayer();
            if (player.hasPermission("MHDFTools.Op")) {
                if (!BooleanHasMap.getHasMap().get("CheckVersionError")) {
                    if (!BooleanHasMap.getHasMap().get("IsLast")) {
                        player.sendMessage(ChatColor("&cCheng-Tools不是最新版! 下载链接:https://github.com/ChengZhiNB/Cheng-Tools-Reloaded/releases/"));
                    }
                } else {
                    player.sendMessage(ChatColor("&cCheng-Tools无法检查更新!"));
                }
            }
        }
        if (MHDFTools.instance.getConfig().getBoolean("VanishEnable")) {
            Player player = event.getPlayer();
            for (Player OnlinePlayer : Bukkit.getOnlinePlayers()) {
                if (StringHasMap.getHasMap().get(player.getName() + "_Vanish") != null) {
                    if (Integer.parseInt(Objects.requireNonNull(getNMSVersion()).split("_")[1]) <= 12) {
                        OnlinePlayer.hidePlayer(player);
                    } else {
                        OnlinePlayer.hidePlayer(MHDFTools.instance, player);
                    }
                }
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
            ScoreboardHasMap.getHasMap().put(player.getName() + "_Scoreboard", Bukkit.getScoreboardManager().getNewScoreboard());
        }
        if (MHDFTools.instance.getConfig().getBoolean("WhiteList.Enable")) {
            for (String WhiteList : MHDFTools.instance.getConfig().getStringList("WhiteList.List")) {
                if (event.getPlayer().getName().equals(WhiteList)) {
                    break;
                }
            }
            event.getPlayer().kickPlayer(MHDFTools.instance.getConfig().getString("WhiteList.KickMessage"));
        }
        if (MHDFTools.instance.getConfig().getBoolean("PlayerJoinSendMessageSettings.Enable")) {
            event.getPlayer().sendMessage(ChatColor(MHDFTools.instance.getConfig().getString("PlayerJoinSendMessageSettings.Message"))
                    .replaceAll("%PlayerName%", event.getPlayer().getName())
            );
        }
    }
}
