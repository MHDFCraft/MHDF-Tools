package cn.ChengZhiYa.MHDFTools.util.message;

import cn.ChengZhiYa.MHDFTools.Main;
import org.bukkit.Bukkit;

import static cn.ChengZhiYa.MHDFTools.util.message.ColorUtil.color;

public final class LogUtil {
    private static final String CONSOLE_PREFIX = "[MHDF-Tools] ";
    private static final String DEBUG_PREFIX = "[MHDF-Tools-Debug] ";

    /**
     * 日志消息
     *
     * @param message 内容
     */
    public static void log(String message) {
        Bukkit.getConsoleSender().sendMessage(color(CONSOLE_PREFIX + message));
    }

    /**
     * 调试消息
     *
     * @param messages 内容
     */
    public static void debug(String... messages) {
        if (Main.instance.getConfig().getBoolean("debug")) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String message : messages) {
                stringBuilder.append(message);
                if (!message.equals(messages[messages.length - 1])) {
                    stringBuilder.append(" | ");
                }
            }
            log(stringBuilder.toString());
        }
    }
}
