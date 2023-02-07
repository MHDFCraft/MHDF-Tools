package cn.ChengZhiYa.ChengToolsReloaded.Commands;

import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHashMap;
import cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.ChatColor;

public class Fly implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            if (sender.hasPermission("ChengTools.Fly")) {
                if (args.length == 1) {
                    if (sender.hasPermission("ChengTools.Fly.Give")) {
                        String PlayerName = args[0];
                        if (Bukkit.getPlayer(PlayerName) == null) {
                            sender.sendMessage(ChatColor("&c&l这个玩家不存在或不在线"));
                            return false;
                        }
                        Player player = Bukkit.getPlayer(PlayerName);
                        assert player != null;
                        if (StringHashMap.Get(player.getName() + "_Fly") == null) {
                            player.setAllowFlight(true);
                            player.sendMessage(ChatColor("&6&l您的飞行模式已启用"));
                            sender.sendMessage(ChatColor("&a&l已将" + player.getName() + "的飞行模式启用"));
                            StringHashMap.Set(player.getName() + "_Fly", "已启用");
                        } else {
                            player.setAllowFlight(false);
                            player.sendMessage(ChatColor("&6&l您的飞行模式已关闭"));
                            sender.sendMessage(ChatColor("&a&l已将" + player.getName() + "的飞行模式关闭"));
                            StringHashMap.Set(player.getName() + "_Fly", null);
                        }
                    } else {
                        Player player = (Player) sender;
                        if (StringHashMap.Get(player.getName() + "_Fly") == null) {
                            player.setAllowFlight(true);
                            player.sendMessage(ChatColor("&6&l您的飞行模式已启用"));
                            StringHashMap.Set(player.getName() + "_Fly", "已启用");
                        } else {
                            player.setAllowFlight(false);
                            player.sendMessage(ChatColor("&6&l您的飞行模式已关闭"));
                            StringHashMap.Set(player.getName() + "_Fly", null);
                        }
                    }
                } else {
                    Player player = (Player) sender;
                    if (StringHashMap.Get(player.getName() + "_Fly") == null) {
                        player.setAllowFlight(true);
                        player.sendMessage(ChatColor("&6&l您的飞行模式已启用"));
                        StringHashMap.Set(player.getName() + "_Fly", "已启用");
                    } else {
                        player.setAllowFlight(false);
                        player.sendMessage(ChatColor("&6&l您的飞行模式已关闭"));
                        StringHashMap.Set(player.getName() + "_Fly", null);
                    }
                }
            } else {
                sender.sendMessage(multi.ChatColor("&c&l您没有权限怎么做!"));
            }
        } else {
            if (args.length == 1) {
                if (sender.hasPermission("ChengTools.Fly.Give")) {
                    String PlayerName = args[0];
                    if (Bukkit.getPlayer(PlayerName) == null) {
                        sender.sendMessage(ChatColor("&c&l这个玩家不存在或不在线"));
                        return false;
                    }
                    Player player = Bukkit.getPlayer(PlayerName);
                    assert player != null;
                    if (StringHashMap.Get(player.getName() + "_Fly") == null) {
                        player.setAllowFlight(true);
                        player.sendMessage(ChatColor("&6&l您的飞行模式已启用"));
                        sender.sendMessage(ChatColor("&a&l已将" + player.getName() + "的飞行模式启用"));
                        StringHashMap.Set(player.getName() + "_Fly", "已启用");
                    } else {
                        player.setAllowFlight(false);
                        player.sendMessage(ChatColor("&6&l您的飞行模式已关闭"));
                        sender.sendMessage(ChatColor("&a&l已将" + player.getName() + "的飞行模式关闭"));
                        StringHashMap.Set(player.getName() + "_Fly", null);
                    }
                }
                return false;
            }
            sender.sendMessage(ChatColor("&c&l用法错误,正确用法:/fly <玩家ID>"));
        }
        return false;
    }
}