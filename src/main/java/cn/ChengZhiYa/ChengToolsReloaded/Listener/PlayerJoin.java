package cn.ChengZhiYa.ChengToolsReloaded.Listener;

import cn.ChengZhiYa.ChengToolsReloaded.HashMap.BooleanHashMap;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHashMap;
import cn.ChengZhiYa.ChengToolsReloaded.main;
import org.bukkit.Bukkit;
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
            if (player.isOnline()) {
                if (!BooleanHashMap.Get("IsLast")) {
                    player.sendMessage(ChatColor("&cCheng-Tools不是最新版! 下载链接:https://github.com/ChengZhiNB/Cheng-Tools-Reloaded/releases/"));
                }
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
        if (main.main.getConfig().getBoolean("CustomJoinServerMessageSettings.Enable")) {
            Player player = event.getPlayer();
            if (main.main.getConfig().getBoolean("VanishEnable")) {
                for (Player OnlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (StringHashMap.Get(player.getName() + "_Vanish") != null) {
                        player.teleport(OnlinePlayer);
                    }
                }
            }
            if (main.main.getConfig().getBoolean("TpaSetting.Enable")) {
                File TpaData = new File(main.getPlugin(main.class).getDataFolder(), "TpaData.yml");
                YamlConfiguration Tpa_Data = YamlConfiguration.loadConfiguration(TpaData);
                Tpa_Data.set(player.getName() + "_TpaPromptMode", main.main.getConfig().getBoolean("TpaSetting.DefaultMode"));
                try {
                    Tpa_Data.save(TpaData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (player.isOp()) {
                String JoinMessage = Objects.requireNonNull(main.main.getConfig().getString("CustomJoinServerMessageSettings.AdminJoinMessage")).replaceAll("%PlayerName%", player.getName());
                event.setJoinMessage(ChatColor(JoinMessage));
            } else {
                String JoinMessage = Objects.requireNonNull(main.main.getConfig().getString("CustomJoinServerMessageSettings.PlayerJoinMessage")).replaceAll("%PlayerName%", player.getName());
                event.setJoinMessage(ChatColor(JoinMessage));
            }
        }
    }
}
