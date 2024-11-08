package cn.ChengZhiYa.MHDFTools.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class BungeeCordUtil {
    public static List<String> getPlayerList() {
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .toList();
    }
}
