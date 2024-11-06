package cn.ChengZhiYa.MHDFTools.util.message;

import org.bukkit.Bukkit;

import static cn.ChengZhiYa.MHDFTools.util.message.ColorUtil.color;

public final class LogUtil {
    private static final String CONSOLE_PREFIX = "[MHDF-Tools] ";
    private static final String DEBUG_PREFIX = "[MHDF-Tools-Debug] ";

    public static void log(String message) {
        Bukkit.getConsoleSender().sendMessage(color(CONSOLE_PREFIX + message));
    }

    public static void debug(String... messages) {
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
