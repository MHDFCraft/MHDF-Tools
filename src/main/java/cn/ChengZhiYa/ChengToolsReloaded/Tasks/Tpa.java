package cn.ChengZhiYa.ChengToolsReloaded.Tasks;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.BooleanHasMap;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.IntHasMap;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHasMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public final class Tpa extends BukkitRunnable {

    public Tpa() {
    }

    public void run() {
        if (ChengToolsReloaded.instance.getConfig().getBoolean("TpaEnable")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                java.lang.String PlayerName = player.getName();

                if (StringHasMap.getHasMap().get(PlayerName + "_Temp_TpaPlayerName") != null) {
                    if (BooleanHasMap.getHasMap().get(PlayerName + "_Temp_Tpa")) {
                        int TpaTime = IntHasMap.getHasMap().get(PlayerName + "_Temp_TpaTime");
                        IntHasMap.getHasMap().put(PlayerName + "_Temp_TpaTime", TpaTime + 1);
                        if (TpaTime + 1 >= 60) {
                            java.lang.String TpaPlayerName = StringHasMap.getHasMap().get(PlayerName + "_Temp_TpaPlayerName");
                            Player TpaPlayer = Bukkit.getPlayer(Objects.requireNonNull(TpaPlayerName));
                            Objects.requireNonNull(TpaPlayer).sendMessage(getLang("Tpa.TimeOut",PlayerName));
                            player.sendMessage(getLang("Tpa.TimeOutDone",TpaPlayerName));
                            StringHasMap.getHasMap().put(PlayerName + "_Temp_TpaPlayerName", null);
                            BooleanHasMap.getHasMap().put(PlayerName + "_Temp_Tpa", false);
                            IntHasMap.getHasMap().put(PlayerName + "_Temp_TpaTime", 0);
                        }
                    }
                }
            }
        } else {
            this.cancel();
        }
    }
}
