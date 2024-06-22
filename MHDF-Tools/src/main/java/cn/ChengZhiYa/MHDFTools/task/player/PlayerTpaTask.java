package cn.ChengZhiYa.MHDFTools.task.player;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.utils.BungeeCordUtil;
import cn.ChengZhiYa.MHDFTools.utils.SpigotUtil;
import cn.ChengZhiYa.MHDFTools.utils.map.MapUtil;
import org.bukkit.scheduler.BukkitRunnable;

public final class PlayerTpaTask extends BukkitRunnable {
    @Override
    public void run() {
        if (MHDFTools.instance.getConfig().getBoolean("TpaSettings.Enable")) {
                for (Object Tpa : MapUtil.getStringHashMap().keySet()) {
                    if (!Tpa.toString().contains("_TPAPlayerName")) continue;
                    String PlayerName = Tpa.toString().replaceAll("_TPAPlayerName", "");
                    String TagerPlayerName = MapUtil.getStringHashMap().get(Tpa);
                    int Time = MapUtil.getIntHashMap().get(PlayerName + "_TPATime");
                    if (BungeeCordUtil.ifPlayerOnline(PlayerName)) {
                        if (BungeeCordUtil.ifPlayerOnline(TagerPlayerName)) {
                            if (Time >= 0) {
                                MapUtil.getIntHashMap().put(PlayerName + "_TPATime", Time - 1);
                                continue;
                            }
                            BungeeCordUtil.SendMessage(PlayerName, SpigotUtil.i18n("Tpa.TimeOutDone", TagerPlayerName));
                            BungeeCordUtil.SendMessage(TagerPlayerName, SpigotUtil.i18n("Tpa.TimeOut", PlayerName));
                            BungeeCordUtil.CancelTpa(PlayerName);
                            continue;
                        }
                        BungeeCordUtil.SendMessage(PlayerName, SpigotUtil.i18n("Tpa.Offline", TagerPlayerName));
                        BungeeCordUtil.CancelTpa(PlayerName);
                        continue;
                    }
                    BungeeCordUtil.CancelTpa(PlayerName);
                }
            }
        }
    }
