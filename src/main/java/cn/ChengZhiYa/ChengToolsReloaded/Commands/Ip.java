package cn.ChengZhiYa.ChengToolsReloaded.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.ChatColor;
import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.getIpLocation;

public class Ip implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("ChengTools.Ip")) {
            if (args.length == 1) {
                String PlayerName = args[0];
                if (Bukkit.getPlayer(PlayerName) == null) {
                    sender.sendMessage(ChatColor("&c&l这个玩家不存在或不在线"));
                    return false;
                }
                Player player = Bukkit.getPlayer(PlayerName);
                String PlayerIp = Objects.requireNonNull(Objects.requireNonNull(player).getAddress()).getHostString();
                String PlayerLocation = getIpLocation(PlayerIp);
                sender.sendMessage(ChatColor("&e玩家: " + player.getName() + "\n" +
                        "IP: " + PlayerIp + "\n" +
                        "IP归属地: " + PlayerLocation));
            } else {
                sender.sendMessage(ChatColor("&c&l用法错误,正确用法:/ip <玩家ID>"));
            }
        }else {
            sender.sendMessage(ChatColor("&c&l您没有权限怎么做!"));
        }
        return false;
    }
}
