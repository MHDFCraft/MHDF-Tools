package cn.ChengZhiYa.MHDFTools.util.message;

import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class MessageUtil {
    public String translateHexCodes(String message) {
        final String hexPattern = "#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{8})";
        Matcher matcher = Pattern.compile(hexPattern).matcher(message);
        StringBuffer sb = new StringBuffer(message.length());
        while (matcher.find()) {
            String hex = matcher.group(1);
            ChatColor color = ChatColor.of("#" + hex);
            matcher.appendReplacement(sb, color.toString());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}