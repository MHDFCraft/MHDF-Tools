package cn.ChengZhiYa.MHDFTools.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public final class BungeeCordUtil {
    /**
     * 获取在线玩家列表
     *
     * @return 在线玩家列表
     */
    public static List<String> getPlayerList() {
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .toList();
    }
}
