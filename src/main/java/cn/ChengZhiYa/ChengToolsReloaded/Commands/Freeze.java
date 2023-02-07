package cn.ChengZhiYa.ChengToolsReloaded.Commands;

import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHashMap;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.ChatColor;

public class Freeze implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("ChengTools.Freeze")) {
            if (args.length == 1) {
                String PlayerName = args[0];
                if (Bukkit.getPlayer(PlayerName) == null) {
                    sender.sendMessage(ChatColor("&c&l这个玩家不存在或不在线"));
                    return false;
                }
                if (StringHashMap.Get(PlayerName + "_Freeze") == null) {
                    StringHashMap.Set(PlayerName + "_Freeze", "t");
                    sender.sendMessage(ChatColor("&e&l冻结成功!"));
                } else {
                    StringHashMap.Remove(PlayerName + "_Freeze");
                    sender.sendMessage(ChatColor("&e&l已取消冻结!"));
                }
            } else {
                sender.sendMessage(ChatColor("&c&l用法错误,正确用法:/freeze <玩家ID>"));
            }
        } else {
            sender.sendMessage(ChatColor("&c&l您没有权限怎么做!"));
        }
        return false;
    }
}
