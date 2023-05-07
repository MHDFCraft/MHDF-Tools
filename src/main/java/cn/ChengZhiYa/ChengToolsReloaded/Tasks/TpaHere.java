package cn.ChengZhiYa.ChengToolsReloaded.Tasks;

import cn.ChengZhiYa.ChengToolsReloaded.HashMap.BooleanHasMap;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.IntHasMap;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHasMap;
import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public final class TpaHere extends BukkitRunnable {

    public TpaHere() {
    }

    @Override
    public void run() {
        if (ChengToolsReloaded.instance.getConfig().getBoolean("TpahereEnable")) {

            for (Player player : Bukkit.getOnlinePlayers()) {
                java.lang.String PlayerName = player.getName();

                if (StringHasMap.getHasMap().get(PlayerName + "_Temp_TpaherePlayerName") != null) {
                    if (BooleanHasMap.getHasMap().get(PlayerName + "_Temp_Tpahere")) {
                        int TpaTime = IntHasMap.getHasMap().get(PlayerName + "_Temp_TpahereTime");
                        IntHasMap.getHasMap().put(PlayerName + "_Temp_TpahereTime", TpaTime + 1);
                        if (TpaTime + 1 >= 60) {
                            java.lang.String TpaPlayerName = StringHasMap.getHasMap().get(PlayerName + "_Temp_TpaherePlayerName");
                            Player TpaPlayer = Bukkit.getPlayer(Objects.requireNonNull(TpaPlayerName));
                            Objects.requireNonNull(TpaPlayer).sendMessage(getLang("TpaHere.TimeOut",PlayerName));
                            player.sendMessage(getLang("TpaHere.TimeOutDone",TpaPlayerName));
                            StringHasMap.getHasMap().put(PlayerName + "_Temp_TpaherePlayerName", null);
                            BooleanHasMap.getHasMap().put(PlayerName + "_Temp_Tpahere", false);
                            IntHasMap.getHasMap().put(PlayerName + "_Temp_TpahereTime", 0);
                        }
                    }
                }
            }
        }
    }
}
