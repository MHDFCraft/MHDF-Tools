package cn.ChengZhiYa.ChengToolsReloaded.Commands;

import cn.ChengZhiYa.ChengToolsReloaded.main;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public class List_Command implements CommandExecutor {
    private final Runtime runtime = Runtime.getRuntime();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        for (String Message : main.main.getConfig().getStringList("SuperListSettings.Message")) {
            List<String> PlayerList = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                PlayerList.add(player.getName());
            }
            sender.sendMessage(ChatColor(PlaceholderAPI.setPlaceholders(null,Message.
                    replaceAll("%mem_used%", String.valueOf((runtime.totalMemory() - runtime.freeMemory()) / 1048576L)).
                    replaceAll("%mem_max%", String.valueOf(runtime.maxMemory() / 1048576L)).
                    replaceAll("%PlayerList%", String.valueOf(PlayerList)))));
        }
        return false;
    }
}
