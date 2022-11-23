package cn.ChengZhiYa.ChengToolsReloaded.Tasks;

import cn.ChengZhiYa.ChengToolsReloaded.HashMap.BooleanHashMap;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.IntHashMap;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHashMap;
import cn.ChengZhiYa.ChengToolsReloaded.main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class Tpa_Detect extends BukkitRunnable {
    main pluginmain;

    public Tpa_Detect(main main1) {
        this.pluginmain = main1;
    }

    public void run() {
        if (main.main.getConfig().getBoolean("TpaSettings.Enable")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                String PlayerName = player.getName();
                if (StringHashMap.Get(PlayerName + "_Temp_TpaPlayerName") != null) {
                    if (BooleanHashMap.Get(PlayerName + "_Temp_Tpa")) {
                        if (Bukkit.getPlayer(Objects.requireNonNull(StringHashMap.Get(PlayerName + "_Temp_TpaPlayerName"))) == null) {
                            player.sendMessage(ChatColor.RED + "错误,那个玩家突然下线了!传送已取消");
                        } else {
                            Player TpaPlayer = Bukkit.getPlayer(Objects.requireNonNull(StringHashMap.Get(PlayerName + "_Temp_TpaPlayerName")));
                            Location TpaLocation = Objects.requireNonNull(TpaPlayer).getLocation();
                            player.teleport(TpaLocation);
                            player.sendMessage(ChatColor.GREEN + "传送成功！");
                            TpaPlayer.sendMessage(ChatColor.GREEN + player.getName() + "传送到了你这里!");
                        }
                        StringHashMap.Set(PlayerName + "_Temp_TpaPlayerName", null);
                        BooleanHashMap.Set(PlayerName + "_Temp_Tpa", false);
                        IntHashMap.Set(PlayerName + "_Temp_TpaTime", 0);
                    }
                }
            }
        } else {
            this.cancel();
        }
    }
}
