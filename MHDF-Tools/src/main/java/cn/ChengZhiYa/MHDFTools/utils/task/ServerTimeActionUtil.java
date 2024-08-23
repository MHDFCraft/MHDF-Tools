package cn.ChengZhiYa.MHDFTools.utils.task;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import lombok.Getter;

import java.util.*;

public final class ServerTimeActionUtil {
    @Getter
    static Map<String, Integer> timeActionHashMap = new HashMap<>();

    public static List<String> getTimeActionList() {
        return new ArrayList<>(Objects.requireNonNull(PluginLoader.INSTANCE.getPlugin().getConfig().getConfigurationSection("TimeActionSettings.ActionList")).getKeys(false));
    }

    public static int getDelayTime(String action) {
        String[] time = Objects.requireNonNull(PluginLoader.INSTANCE.getPlugin().getConfig().getString("TimeActionSettings.ActionList." + action + ".Time")).split(":");
        int hour = Integer.parseInt(time[0]) * 3600;
        int minute = Integer.parseInt(time[1]) * 60;
        int second = Integer.parseInt(time[2]);
        return hour + minute + second;
    }
}
