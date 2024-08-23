package cn.ChengZhiYa.MHDFTools.task.server;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.utils.message.ColorLogs;
import cn.ChengZhiYa.MHDFTools.utils.task.ServerTimeActionUtil;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;

import java.time.LocalTime;
import java.util.Objects;
import java.util.function.Consumer;

import static cn.ChengZhiYa.MHDFTools.utils.menu.MenuUtil.runAction;
import static cn.ChengZhiYa.MHDFTools.utils.task.ServerTimeActionUtil.getDelayTime;
import static cn.ChengZhiYa.MHDFTools.utils.task.ServerTimeActionUtil.getTimeActionList;

public final class ServerTimeActionTask implements Consumer<ScheduledTask> {

    @Override
    public void accept(ScheduledTask task) {
        for (String action : getTimeActionList()) {
            switch (Objects.requireNonNull(PluginLoader.INSTANCE.getPlugin().getConfig().getString("TimeActionSettings.ActionList." + action + ".Type"))) {
                case "定时操作": {
                    int time = ServerTimeActionUtil.getTimeActionHashMap().get(action) != null ? ServerTimeActionUtil.getTimeActionHashMap().get(action) : 0;
                    ServerTimeActionUtil.getTimeActionHashMap().put(action, time + 1);

                    if (ServerTimeActionUtil.getTimeActionHashMap().get(action) >= getDelayTime(action)) {
                        ServerTimeActionUtil.getTimeActionHashMap().remove(action);
                        runActions(action);
                    }
                    break;
                }
                case "定点操作": {
                    LocalTime localTime = LocalTime.now();
                    String[] time = Objects.requireNonNull(PluginLoader.INSTANCE.getPlugin().getConfig().getString("TimeActionSettings.ActionList." + action + ".Time")).split(":");
                    int hour = Integer.parseInt(time[0]);
                    int minute = Integer.parseInt(time[1]);
                    int second = Integer.parseInt(time[2]);
                    if (localTime.getHour() == hour && localTime.getMinute() == minute && localTime.getSecond() == second) {
                        runActions(action);
                    }
                    break;
                }
            }
        }
    }

    private void runActions(String action) {
        if (PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("TimeActionSettings.Enable")) {
            ColorLogs.debug("定时操作", "操作名称: " + action);
            runAction(Bukkit.getConsoleSender(), null, action.split("\\|"));
        }
    }
}
