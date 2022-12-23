package cn.ChengZhiYa.ChengToolsReloaded.Commands;

import cn.ChengZhiYa.ChengToolsReloaded.HashMap.LocationHashMap;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.ChatColor;

public class UnBack implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (LocationHashMap.Get(player.getName() + "_UnBack") != null) {
                player.teleport(LocationHashMap.Get(player.getName() + "_UnBack"));
                player.sendMessage(ChatColor("&a&l已返回传送前的位置"));
            } else {
                sender.sendMessage(ChatColor("&c&l找不到你前往死亡点的记录!"));
            }
        } else {
            sender.sendMessage(ChatColor("&c&l这个命令只能玩家使用!"));
        }
        return false;
    }
}
