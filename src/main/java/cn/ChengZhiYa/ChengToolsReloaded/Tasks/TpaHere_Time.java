package cn.ChengZhiYa.ChengToolsReloaded.Tasks;

import cn.ChengZhiYa.ChengToolsReloaded.HashMap.BooleanHashMap;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.IntHashMap;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHashMap;
import cn.ChengZhiYa.ChengToolsReloaded.main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public class TpaHere_Time extends BukkitRunnable {

    public TpaHere_Time() {
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
                            Objects.requireNonNull(TpaPlayer).sendMessage(getLang("TpaHere.TimeOut",PlayerName));
                            player.sendMessage(getLang("TpaHere.TimeOutDone",TpaPlayerName));
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
