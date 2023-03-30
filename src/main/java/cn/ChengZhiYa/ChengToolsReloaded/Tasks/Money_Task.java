package cn.ChengZhiYa.ChengToolsReloaded.Tasks;

import cn.ChengZhiYa.ChengToolsReloaded.Ultis.*;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class Money_Task extends BukkitRunnable {
    private static LinkedList<Map.Entry<String, Double>> list;

    public static Map.Entry<String, Double> getPlayer(int position) {
        if (position > list.size() || position <= 0) {
            return null;
        }
        return list.get(list.size() - position);
    }

    @Override
    public void run() {
        Map<String, Double> leaderboard = new HashMap<>();
        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            String playerName = player.getName();
            Double money = new EconomyAPI().checkMoney(playerName);
            leaderboard.put(playerName, money);
        }
        list = new LinkedList<>(leaderboard.entrySet());
        list.sort(Map.Entry.comparingByValue());
    }
}
