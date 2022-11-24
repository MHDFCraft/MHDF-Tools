package cn.ChengZhiYa.ChengToolsReloaded.Commands;

import cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class Day implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("ChengTools.Day")) {
            for (World world : Bukkit.getWorlds()) {
                world.setTime(1000);
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a完成!"));
        }else {
            sender.sendMessage(multi.ChatColor("&c您没有权限怎么做!"));
        }
        return false;
    }
}
