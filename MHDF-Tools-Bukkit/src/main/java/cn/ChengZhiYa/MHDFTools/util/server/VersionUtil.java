package cn.ChengZhiYa.MHDFTools.util.server;

import org.bukkit.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class VersionUtil {
    public static int getVersion() {
        Pattern pattern = Pattern.compile("MC: 1\\.([0-9]+)");
        Matcher matcher = pattern.matcher(Bukkit.getVersion());
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1).trim());
        }
        // 无法获取游戏版本
        return -1;
    }

    public static boolean is1_8orAbove() {
        return getVersion() >= 8;
    }

    public static boolean is1_9orAbove() {
        return getVersion() >= 9;
    }

    public static boolean is1_12orAbove() {
        return getVersion() >= 12;
    }

    public static boolean is1_13orAbove() {
        return getVersion() >= 13;
    }

    public static boolean is1_14orAbove() {
        return getVersion() >= 14;
    }

    public static boolean is1_15orAbove() {
        return getVersion() >= 15;
    }

    public static boolean is1_16orAbove() {
        return getVersion() >= 16;
    }

    public static boolean is1_17orAbove() {
        return getVersion() >= 17;
    }

    public static boolean is1_18orAbove() {
        return getVersion() >= 18;
    }

    public static boolean is1_19orAbove() {
        return getVersion() >= 19;
    }

    public static boolean is1_20orAbove() {
        return getVersion() >= 20;
    }
}
