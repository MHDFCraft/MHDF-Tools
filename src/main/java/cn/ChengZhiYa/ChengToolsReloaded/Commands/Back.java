package cn.ChengZhiYa.ChengToolsReloaded.Commands;

import cn.ChengZhiYa.ChengToolsReloaded.HashMap.LocationHashMap;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.ChatColor;

public class Back implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (LocationHashMap.Get(player.getName() + "_DeathLocation") != null) {
                LocationHashMap.Set(player.getName() + "_UnBack", player.getLocation());
                player.teleport(LocationHashMap.Get(player.getName() + "_DeathLocation"));
                player.sendMessage("&a&l已返回死亡点，如果想回到传送前的位置请使用/unback");
            } else {
                sender.sendMessage(ChatColor("&c&l找不到你的死亡记录!"));
            }
        } else {
            sender.sendMessage(ChatColor("&c&l这个命令只能玩家使用!"));
        }
        return false;
    }
}
