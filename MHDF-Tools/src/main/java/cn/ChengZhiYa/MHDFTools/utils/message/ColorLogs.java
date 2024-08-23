package cn.ChengZhiYa.MHDFTools.utils.message;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class ColorLogs {
    public static String rgb(int r, int g, int b) {
        return String.format("\u001B[38;2;%d;%d;%dm", r, g, b);
    }

    public void debug(String... messages) {
        if (PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("Debug")) {
            StringBuilder messageBuilder = new StringBuilder("[MHDF-Tools-调试] ");
            for (int i = 0; i < messages.length; i++) {
                messageBuilder.append(messages[i]);
                if (i < messages.length - 1) {
                    messageBuilder.append(" | ");
                }
            }
            colorMessage(messageBuilder.toString());
        }
    }

    public void colorMessage(String message) {
        CommandSender sender = Bukkit.getConsoleSender();
        if (PluginLoader.INSTANCE.getServerManager().is1_16orAbove()) {
            message = MessageUtil.translateHexCodes(message);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }

    public static String convertColorsInText(String text) {
        Pattern pattern = Pattern.compile("§x(§[0-9a-fA-F]){6}");
        Matcher matcher = pattern.matcher(text);

        StringBuilder result = new StringBuilder();
        int lastEnd = 0;

        while (matcher.find()) {
            result.append(text, lastEnd, matcher.start());
            String matched = matcher.group();
            String hexColor = convertToHexColor(matched);
            result.append(hexColor);
            lastEnd = matcher.end();
        }
        result.append(text.substring(lastEnd));

        return result.toString();
    }

    private static String convertToHexColor(String input) {
        String cleanedInput = input.replace("§x", "").replace("§", "");
        return "#" + cleanedInput.replaceAll("(.{2})", "$1").toUpperCase();
    }

    public void consoleMessage(final String info) {
        if (PluginLoader.INSTANCE.getServerManager().is1_16orAbove()) {
            final String hexPattern = "#([A-Fa-f0-9]{6})";
            Matcher matcher = Pattern.compile(hexPattern).matcher(convertColorsInText(info));
            StringBuilder sb = new StringBuilder(info.length());
            while (matcher.find()) {
                String hex = matcher.group(1);
                Color color = Color.decode("#" + hex);
                matcher.appendReplacement(sb, rgb(color.getRed(), color.getGreen(), color.getBlue()));
            }
            matcher.appendTail(sb);
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', sb.toString()));
        } else {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', info));
        }
    }
}