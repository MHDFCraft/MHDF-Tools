package cn.ChengZhiYa.ChengToolsReloaded.Listener;

import cn.ChengZhiYa.ChengToolsReloaded.HashMap.BooleanHashMap;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.ScoreboardHashMap;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHashMap;
import cn.ChengZhiYa.ChengToolsReloaded.main;
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

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public class PlayerJoin implements Listener {
    @EventHandler
    public void On_Event(PlayerJoinEvent event) {
        if (main.main.getConfig().getBoolean("CheckVersion")) {
            Player player = event.getPlayer();
            if (player.hasPermission("ChengTools.Op")) {
                if (!BooleanHashMap.Get("CheckVersionError")) {
                    if (!BooleanHashMap.Get("IsLast")) {
                        player.sendMessage(ChatColor("&cCheng-Tools不是最新版! 下载链接:https://github.com/ChengZhiNB/Cheng-Tools-Reloaded/releases/"));
                    }
                } else {
                    player.sendMessage(ChatColor("&cCheng-Tools无法检查更新!"));
                }
            }
        }
        if (main.main.getConfig().getBoolean("SpawnSettings.Enable")) {
            if (main.main.getConfig().getBoolean("SpawnSettings.JoinTeleport")) {
                Player player = event.getPlayer();
                World world = Bukkit.getWorld(Objects.requireNonNull(main.main.getConfig().getString("SpawnSettings.World")));
                double X = main.main.getConfig().getDouble("SpawnSettings.X");
                double Y = main.main.getConfig().getDouble("SpawnSettings.Y");
                double Z = main.main.getConfig().getDouble("SpawnSettings.Z");
                float Yaw = (float) main.main.getConfig().getDouble("SpawnSettings.Yaw");
                float Pitch = (float) main.main.getConfig().getDouble("SpawnSettings.Pitch");
                Location SpawnLcation = new Location(world, X, Y, Z, Yaw, Pitch);
                player.teleport(SpawnLcation);
            }
        }
        if (main.main.getConfig().getBoolean("HomeSystemSettings.Enable")) {
            File HomeData = new File(main.main.getDataFolder() + "/HomeData");
            File HomeData_File = new File(HomeData, event.getPlayer().getName() + ".yml");
            if (!HomeData_File.exists()) {
                try {
                    HomeData_File.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (main.main.getConfig().getBoolean("VanishEnable")) {
            Player player = event.getPlayer();
            for (Player OnlinePlayer : Bukkit.getOnlinePlayers()) {
                if (StringHashMap.Get(player.getName() + "_Vanish") != null) {
                    OnlinePlayer.hidePlayer(player);
                }
            }
        }
        if (main.main.getConfig().getBoolean("TpaSetting.Enable")) {
            Player player = event.getPlayer();
            File TpaData = new File(main.getPlugin(main.class).getDataFolder(), "TpaData.yml");
            YamlConfiguration Tpa_Data = YamlConfiguration.loadConfiguration(TpaData);
            Tpa_Data.set(player.getName() + "_TpaPromptMode", main.main.getConfig().getBoolean("TpaSetting.DefaultMode"));
            try {
                Tpa_Data.save(TpaData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (main.main.getConfig().getBoolean("LoginSystemSettings.AutoLogin")) {
            Player player = event.getPlayer();
            if (StringHashMap.Get(player.getName() + "_LoginIP") != null) {
                if (StringHashMap.Get(player.getName() + "_LoginIP").equals(Objects.requireNonNull(player.getAddress()).getHostName())) {
                    StringHashMap.Set(player.getName() + "_Login", "t");
                    player.sendMessage(getLang("Login.AutoLogin"));
                }
            }
        }
        if (main.main.getConfig().getBoolean("CustomJoinServerMessageSettings.Enable")) {
            Player player = event.getPlayer();
            if (player.hasPermission("ChengTools.Op")) {
                String JoinMessage = Objects.requireNonNull(main.main.getConfig().getString("CustomJoinServerMessageSettings.AdminJoinMessage")).replaceAll("%PlayerName%", player.getName());
                event.setJoinMessage(ChatColor(player, JoinMessage));
            } else {
                String JoinMessage = Objects.requireNonNull(main.main.getConfig().getString("CustomJoinServerMessageSettings.PlayerJoinMessage")).replaceAll("%PlayerName%", player.getName());
                event.setJoinMessage(ChatColor(player, JoinMessage));
            }
        }
        if (main.main.getConfig().getBoolean("ScoreboardSettings.Enable")) {
            Player player = event.getPlayer();
            ScoreboardHashMap.Set(player.getName() + "_Scoreboard", Bukkit.getScoreboardManager().getNewScoreboard());
        }
        if (main.main.getConfig().getBoolean("PlayerTitleSettings.Enable")) {
            if (main.main.getConfig().getBoolean("PlayerTitleSettings.ShowPrefixAndSuffix")) {
                Player player = event.getPlayer();
                player.setDisplayName(ChatColor(GetPlt(player)[0] + player.getName() + GetPlt(player)[1]));
                player.setPlayerListName(ChatColor(GetPlt(player)[0] + player.getName() + GetPlt(player)[1]));
            }
        }
        if (main.main.getConfig().getBoolean("WhiteList.Enable")) {
            for (String WhiteList : main.main.getConfig().getStringList("WhiteList.List")) {
                if (event.getPlayer().getName().equals(WhiteList)) {
                    break;
                }
            }
            event.getPlayer().kickPlayer(main.main.getConfig().getString("WhiteList.KickMessage"));
        }
    }
}
