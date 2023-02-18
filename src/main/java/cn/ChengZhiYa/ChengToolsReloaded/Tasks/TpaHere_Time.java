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

public class TpaHere_Time extends BukkitRunnable {
    main pluginmain;

    public TpaHere_Time(main main1) {
        this.pluginmain = main1;
    }

    @Override
    public void run() {
        if (main.main.getConfig().getBoolean("TpahereEnable")) {

            for (Player player : Bukkit.getOnlinePlayers()) {
                String PlayerName = player.getName();

                if (StringHashMap.Get(PlayerName + "_Temp_TpaherePlayerName") != null) {
                    if (!BooleanHashMap.Get(PlayerName + "_Temp_Tpahere")) {
                        int TpaTime = IntHashMap.Get(PlayerName + "_Temp_TpahereTime");
                        IntHashMap.Set(PlayerName + "_Temp_TpahereTime", TpaTime + 1);
                        if (TpaTime + 1 >= 60) {
                            String TpaPlayerName = StringHashMap.Get(PlayerName + "_Temp_TpaherePlayerName");
                            Player TpaPlayer = Bukkit.getPlayer(Objects.requireNonNull(TpaPlayerName));
                            Objects.requireNonNull(TpaPlayer).sendMessage(ChatColor("&c" + PlayerName + "给你发的的传送请求已超时"));
                            player.sendMessage(ChatColor("&c你发给" + TpaPlayerName + "的传送请求已超时"));
                            StringHashMap.Set(PlayerName + "_Temp_TpaherePlayerName", null);
                            BooleanHashMap.Set(PlayerName + "_Temp_Tpahere", false);
                            IntHashMap.Set(PlayerName + "_Temp_TpahereTime", 0);
                        }
                    }
                }
            }
        }
    }
}
