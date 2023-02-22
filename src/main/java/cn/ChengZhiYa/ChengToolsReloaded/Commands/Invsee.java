package cn.ChengZhiYa.ChengToolsReloaded.Commands;

import cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.ChatColor;

public class Invsee implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            if (sender.hasPermission(" ChengTools.Gamemode")) {
                if (args.length == 1) {
                    Player player = (Player) sender;
                    if (Bukkit.getPlayer(args[0]) == null) {
                        sender.sendMessage(ChatColor("&c这个玩家不存在"));
                        return false;
                    }
                    Player Player = Bukkit.getPlayer(args[0]);
                    player.openInventory(Objects.requireNonNull(Player).getInventory());
                } else {
                    sender.sendMessage(ChatColor("&c用法错误,用法:/invsee <玩家ID>"));
                }
            }else {
                sender.sendMessage(multi.ChatColor("&c您没有权限怎么做!"));
            }
        } else {
            sender.sendMessage(ChatColor("&c&l这个命令只能玩家使用!"));
        }
        return false;
    }
}
