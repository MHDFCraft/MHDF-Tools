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

public class Tpa_Time extends BukkitRunnable {

    public Tpa_Time() {
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
                            Objects.requireNonNull(TpaPlayer).sendMessage(getLang("Tpa.TimeOut",PlayerName));
                            player.sendMessage(getLang("Tpa.TimeOutDone",TpaPlayerName));
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
