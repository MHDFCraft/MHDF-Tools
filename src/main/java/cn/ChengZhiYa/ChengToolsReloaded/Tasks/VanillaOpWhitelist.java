package cn.ChengZhiYa.ChengToolsReloaded.Tasks;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Util.OpSendMessage;
import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Util.i18n;

public final class VanillaOpWhitelist extends BukkitRunnable {
    public void run() {
        if (ChengToolsReloaded.instance.getConfig().getBoolean("VanillaOpWhitelist.Enable")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.isOp()) {
                    for (String OpWhitelits : ChengToolsReloaded.instance.getConfig().getStringList("VanillaOpWhitelist.Whitelist")) {
                        if (!player.getName().equals(OpWhitelits)) {
                            player.setOp(false);
                            OpSendMessage(i18n("OpWhietList.Message", player.getName()), player.getName());
                            player.kickPlayer("OpWhietList.KickMessage");
                        }
                    }
                }
            }
        }
    }
}
