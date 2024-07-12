package cn.ChengZhiYa.MHDFTools.task.player;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.entity.TpaData;
import cn.ChengZhiYa.MHDFTools.utils.BungeeCordUtil;
import cn.ChengZhiYa.MHDFTools.utils.SpigotUtil;
import cn.ChengZhiYa.MHDFTools.utils.command.TpaUtil;
import org.bukkit.scheduler.BukkitRunnable;

public final class PlayerTpaTask extends BukkitRunnable {
    @Override
    public void run() {
        if (MHDFTools.instance.getConfig().getBoolean("TpaSettings.Enable")) {
            for (String playerName : TpaUtil.getTpaHashMap().keySet()) {
                TpaData tpaData = TpaUtil.getTpaHashMap().get(playerName);
                String targetPlayerName = tpaData.getTargetPlayerName();
                Integer time = tpaData.getTpaOutTime();

                if (time != null && time >= 0) {
                    tpaData.takeTime(1);
                    continue;
                }

                if (BungeeCordUtil.ifPlayerOnline(playerName)) {
                    if (BungeeCordUtil.ifPlayerOnline(targetPlayerName)) {
                        BungeeCordUtil.sendMessage(playerName, SpigotUtil.i18n("Tpa.TimeOutDone", targetPlayerName));
                        BungeeCordUtil.sendMessage(targetPlayerName, SpigotUtil.i18n("Tpa.TimeOut", playerName));
                    } else {
                        BungeeCordUtil.sendMessage(playerName, SpigotUtil.i18n("Tpa.Offline", targetPlayerName));
                    }
                    BungeeCordUtil.cancelTpa(playerName);
                } else {
                    BungeeCordUtil.cancelTpa(playerName);
                }
            }
        }
    }
}