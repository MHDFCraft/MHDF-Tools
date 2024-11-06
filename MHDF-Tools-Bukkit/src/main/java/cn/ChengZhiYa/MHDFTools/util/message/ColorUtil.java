package cn.ChengZhiYa.MHDFTools.util.message;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ColorUtil {

    private static String rgb(String message) {
        Matcher matcher = Pattern.compile("#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{8})").matcher(message);
        StringBuilder sb = new StringBuilder(message.length());
        while (matcher.find()) {
            String hex = matcher.group(1);
            ChatColor color = ChatColor.of("#" + hex);
            matcher.appendReplacement(sb, color.toString());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static String legacy(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String color(String message) {
        if (PluginLoader.INSTANCE.getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_16)) {
            message = rgb(message);
        }
        return legacy(message);
    }
}
