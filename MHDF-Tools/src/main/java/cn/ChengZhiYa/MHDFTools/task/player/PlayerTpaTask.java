package cn.ChengZhiYa.MHDFTools.task.player;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.entity.TpaData;
import cn.ChengZhiYa.MHDFTools.utils.BungeeCordUtil;
import cn.ChengZhiYa.MHDFTools.utils.SpigotUtil;
import cn.ChengZhiYa.MHDFTools.utils.command.TpaUtil;
import com.github.Anon8281.universalScheduler.UniversalRunnable;

public final class PlayerTpaTask extends UniversalRunnable {

    @Override
    public void run() {
        if (PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("TpaSettings.Enable")) {
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