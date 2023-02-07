package cn.ChengZhiYa.ChengToolsReloaded.Tasks;

import cn.ChengZhiYa.ChengToolsReloaded.HashMap.BooleanHashMap;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.IntHashMap;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHashMap;
import cn.ChengZhiYa.ChengToolsReloaded.main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.ChatColor;

public class Tpa_Time extends BukkitRunnable {
    main pluginmain;

    public Tpa_Time(main main1) {
        this.pluginmain = main1;
    }

    public void run() {
        if (main.main.getConfig().getBoolean("TpaEnable")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                String PlayerName = player.getName();

                if (StringHashMap.Get(PlayerName + "_Temp_TpaPlayerName") != null) {
                    if (!BooleanHashMap.Get(PlayerName + "_Temp_Tpa")) {
                        int TpaTime = IntHashMap.Get(PlayerName + "_Temp_TpaTime");
                        IntHashMap.Set(PlayerName + "_Temp_TpaTime", TpaTime + 1);
                        if (TpaTime + 1 >= 60) {
                            String TpaPlayerName = StringHashMap.Get(PlayerName + "_Temp_TpaPlayerName");
                            Player TpaPlayer = Bukkit.getPlayer(Objects.requireNonNull(TpaPlayerName));
                            Objects.requireNonNull(TpaPlayer).sendMessage(ChatColor("&c" + PlayerName + "给你发的的传送请求已超时"));
                            player.sendMessage(ChatColor("&c你发给" + TpaPlayerName + "的传送请求已超时"));
                            StringHashMap.Set(PlayerName + "_Temp_TpaPlayerName", null);
                            BooleanHashMap.Set(PlayerName + "_Temp_Tpa", false);
                            IntHashMap.Set(PlayerName + "_Temp_TpaTime", 0);
                        }
                    }
                }
            }
        } else {
            this.cancel();
        }
    }
}
