package cn.ChengZhiYa.MHDFTools.util.message;

import cn.ChengZhiYa.MHDFTools.Main;
import cn.ChengZhiYa.MHDFTools.util.config.LangUtil;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ColorUtil {
    /**
     * RGB彩色符号(例如: #ffffff)处理
     *
     * @param message 文本
     */
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

    /**
     * 旧版彩色符号(例如: &e)处理
     *
     * @param message 文本
     */
    private static String legacy(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * 彩色符号处理
     *
     * @param message 文本
     */
    public static String color(String message) {
        message = message.replace("{prefix}", LangUtil.i18n("prefix"));
        if (Main.getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_16)) {
            message = rgb(message);
        }
        return legacy(message);
    }
}
