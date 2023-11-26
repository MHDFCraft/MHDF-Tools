package cn.ChengZhiYa.ChengToolsReloaded.Listeners.Player;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.BooleanHasMap;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.ScoreboardHasMap;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHasMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Utils.EconomyUtil.initializationPlayerData;
import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Util.*;

public final class PlayerJoin implements Listener {
    @EventHandler
    public void On_Event(PlayerJoinEvent event) {
        if (ChengToolsReloaded.instance.getConfig().getBoolean("EconomySettings.Enable")) {
            initializationPlayerData(event.getPlayer().getName());
        }
        if (ChengToolsReloaded.instance.getConfig().getBoolean("CheckVersion")) {
            Player player = event.getPlayer();
            if (player.hasPermission("ChengTools.Op")) {
                if (!BooleanHasMap.getHasMap().get("CheckVersionError")) {
                    if (!BooleanHasMap.getHasMap().get("IsLast")) {
                        player.sendMessage(ChatColor("&cCheng-Tools不是最新版! 下载链接:https://github.com/ChengZhiNB/Cheng-Tools-Reloaded/releases/"));
                    }
                } else {
                    player.sendMessage(ChatColor("&cCheng-Tools无法检查更新!"));
                }
            }
        }
        if (ChengToolsReloaded.instance.getConfig().getBoolean("SpawnSettings.Enable")) {
            if (ChengToolsReloaded.instance.getConfig().getBoolean("SpawnSettings.JoinTeleport")) {
                Player player = event.getPlayer();
                World world = Bukkit.getWorld(Objects.requireNonNull(ChengToolsReloaded.instance.getConfig().getString("SpawnSettings.World")));
                double X = ChengToolsReloaded.instance.getConfig().getDouble("SpawnSettings.X");
                double Y = ChengToolsReloaded.instance.getConfig().getDouble("SpawnSettings.Y");
                double Z = ChengToolsReloaded.instance.getConfig().getDouble("SpawnSettings.Z");
                float Yaw = (float) ChengToolsReloaded.instance.getConfig().getDouble("SpawnSettings.Yaw");
                float Pitch = (float) ChengToolsReloaded.instance.getConfig().getDouble("SpawnSettings.Pitch");
                Location SpawnLcation = new Location(world, X, Y, Z, Yaw, Pitch);
                player.teleport(SpawnLcation);
            }
        }
        if (ChengToolsReloaded.instance.getConfig().getBoolean("HomeSystemSettings.Enable")) {
            File HomeData = new File(ChengToolsReloaded.instance.getDataFolder() + "/HomeData");
            File HomeData_File = new File(HomeData, event.getPlayer().getName() + ".yml");
            if (!HomeData_File.exists()) {
                try {
                    HomeData_File.createNewFile();
                } catch (IOException ignored) {}
            }
        }
        if (ChengToolsReloaded.instance.getConfig().getBoolean("VanishEnable")) {
            Player player = event.getPlayer();
            for (Player OnlinePlayer : Bukkit.getOnlinePlayers()) {
                if (StringHasMap.getHasMap().get(player.getName() + "_Vanish") != null) {
                    if (Integer.parseInt(Objects.requireNonNull(getNMSVersion()).split("_")[1]) <= 12) {
                        OnlinePlayer.hidePlayer(player);
                    } else {
                        OnlinePlayer.hidePlayer(ChengToolsReloaded.instance, player);
                    }
                }
            }
        }
        if (ChengToolsReloaded.instance.getConfig().getBoolean("TpaSetting.Enable")) {
            Player player = event.getPlayer();
            File TpaData = new File(ChengToolsReloaded.getPlugin(ChengToolsReloaded.class).getDataFolder(), "TpaData.yml");
            YamlConfiguration Tpa_Data = YamlConfiguration.loadConfiguration(TpaData);
            Tpa_Data.set(player.getName() + "_TpaPromptMode", ChengToolsReloaded.instance.getConfig().getBoolean("TpaSetting.DefaultMode"));
            try {
                Tpa_Data.save(TpaData);
            } catch (IOException ignored) {
            }
        }
        if (ChengToolsReloaded.instance.getConfig().getBoolean("LoginSystemSettings.AutoLogin")) {
            Player player = event.getPlayer();
            if (StringHasMap.getHasMap().get(player.getName() + "_LoginIP") != null) {
                if (StringHasMap.getHasMap().get(player.getName() + "_LoginIP").equals(Objects.requireNonNull(player.getAddress()).getHostName())) {
                    StringHasMap.getHasMap().put(player.getName() + "_Login", "t");
                    player.sendMessage(getLang("Login.AutoLogin"));
                }
            }
        }
        if (ChengToolsReloaded.instance.getConfig().getBoolean("CustomJoinServerMessageSettings.Enable")) {
            Player player = event.getPlayer();
            String JoinMessage = Objects.requireNonNull(ChengToolsReloaded.instance.getConfig().getString("CustomJoinServerMessageSettings.JoinMessage")).replaceAll("%PlayerName%", player.getName());
            event.setJoinMessage(ChatColor(player, JoinMessage));
        }
        if (ChengToolsReloaded.instance.getConfig().getBoolean("ScoreboardSettings.Enable")) {
            Player player = event.getPlayer();
            ScoreboardHasMap.getHasMap().put(player.getName() + "_Scoreboard", Bukkit.getScoreboardManager().getNewScoreboard());
        }
        if (ChengToolsReloaded.instance.getConfig().getBoolean("WhiteList.Enable")) {
            for (String WhiteList : ChengToolsReloaded.instance.getConfig().getStringList("WhiteList.List")) {
                if (event.getPlayer().getName().equals(WhiteList)) {
                    break;
                }
            }
            event.getPlayer().kickPlayer(ChengToolsReloaded.instance.getConfig().getString("WhiteList.KickMessage"));
        }
    }
}
