package cn.ChengZhiYa.ChengToolsReloaded.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public class Gamemode_Command implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            if (sender.hasPermission(" ChengTools.Gamemode")) {
                if (args.length == 1) {
                    Player player = (Player) sender;
                    String GameModeName = args[0];
                    if (GameModeName.equals("生存") || GameModeName.equals("0")) {
                        player.setGameMode(Objects.requireNonNull(GetGamemode(0)));
                        player.sendMessage("您的游戏模式已被更新为生存模式");
                        OpSendMessage("&7[" + player.getName() + ": 将" + player.getName() + "的游戏模式改为生存模式]", player.getName());
                    }
                    if (GameModeName.equals("冒险") || GameModeName.equals("2")) {
                        player.setGameMode(Objects.requireNonNull(GetGamemode(2)));
                        player.sendMessage("您的游戏模式已被更新为冒险模式");
                        OpSendMessage("&7[" + player.getName() + ": 将" + player.getName() + "的游戏模式改为冒险模式]", player.getName());
                    }
                    if (GameModeName.equals("创造") || GameModeName.equals("1")) {
                        player.setGameMode(Objects.requireNonNull(GetGamemode(1)));
                        player.sendMessage("您的游戏模式已被更新为创造模式");
                        OpSendMessage("&7[" + player.getName() + ": 将" + player.getName() + "的游戏模式改为创造模式]", player.getName());
                    }
                    if (GameModeName.equals("旁观") || GameModeName.equals("3")) {
                        player.setGameMode(Objects.requireNonNull(GetGamemode(3)));
                        player.sendMessage("您的游戏模式已被更新为旁观模式");
                        OpSendMessage("&7[" + player.getName() + ": 将" + player.getName() + "的游戏模式改为旁观模式]", player.getName());
                    }
                    return false;
                }
                if (args.length == 2) {
                    String GameModeName = args[0];
                    String PlayerName = args[1];
                    if (Bukkit.getPlayer(PlayerName) == null) {
                        sender.sendMessage(ChatColor("&c&l这个玩家不存在或不在线"));
                        return false;
                    }
                    Player player = Bukkit.getPlayer(PlayerName);
                    assert player != null;
                    if (GameModeName.equals("生存") || GameModeName.equals("0")) {
                        player.setGameMode(Objects.requireNonNull(GetGamemode(0)));
                        player.sendMessage("您的游戏模式已被更新为生存模式");
                        OpSendMessage("&7[" + sender.getName() + ": 将" + player.getName() + "的游戏模式改为生存模式]", sender.getName());
                    }
                    if (GameModeName.equals("冒险") || GameModeName.equals("2")) {
                        player.setGameMode(Objects.requireNonNull(GetGamemode(2)));
                        player.sendMessage("您的游戏模式已被更新为冒险模式");
                        OpSendMessage("&7[" + sender.getName() + ": 将" + player.getName() + "的游戏模式改为冒险模式]", sender.getName());
                    }
                    if (GameModeName.equals("创造") || GameModeName.equals("1")) {
                        player.setGameMode(Objects.requireNonNull(GetGamemode(1)));
                        player.sendMessage("您的游戏模式已被更新为创造模式");
                        OpSendMessage("&7[" + sender.getName() + ": 将" + player.getName() + "的游戏模式改为创造模式]", sender.getName());
                    }
                    if (GameModeName.equals("旁观") || GameModeName.equals("3")) {
                        player.setGameMode(Objects.requireNonNull(GetGamemode(3)));
                        player.sendMessage("您的游戏模式已被更新为旁观模式");
                        OpSendMessage("&7[" + sender.getName() + ": 将" + player.getName() + "的游戏模式改为旁观模式]", sender.getName());
                    }
                    return false;
                }
                sender.sendMessage(ChatColor("&c&l用法错误,正确用法:/" + label + " <模式> [玩家ID]\n&a&l<>是必填,[]是选填"));
            } else {
                sender.sendMessage(ChatColor("&c&l您没有权限怎么做!"));
            }
        } else {
            if (args.length == 2) {
                String GameModeName = args[0];
                String PlayerName = args[1];
                if (Bukkit.getPlayer(PlayerName) == null) {
                    sender.sendMessage(ChatColor("&c&l这个玩家不i存在或不在线"));
                    return false;
                }
                Player player = Bukkit.getPlayer(PlayerName);
                assert player != null;
                if (GameModeName.equals("生存") || GameModeName.equals("0")) {
                    player.setGameMode(Objects.requireNonNull(GetGamemode(0)));
                    player.sendMessage("您的游戏模式已被更新为生存模式");
                    OpSendMessage("&7[Server: 将" + player.getName() + "的游戏模式改为生存模式]", sender.getName());
                }
                if (GameModeName.equals("冒险") || GameModeName.equals("2")) {
                    player.setGameMode(Objects.requireNonNull(GetGamemode(2)));
                    player.sendMessage("您的游戏模式已被更新为冒险模式");
                    OpSendMessage("&7[Server: 将" + player.getName() + "的游戏模式改为冒险模式]", sender.getName());
                }
                if (GameModeName.equals("创造") || GameModeName.equals("1")) {
                    player.setGameMode(Objects.requireNonNull(GetGamemode(1)));
                    player.sendMessage("您的游戏模式已被更新为创造模式");
                    OpSendMessage("&7[Server: 将" + player.getName() + "的游戏模式改为创造模式]", sender.getName());
                }
                if (GameModeName.equals("旁观") || GameModeName.equals("3")) {
                    player.setGameMode(Objects.requireNonNull(GetGamemode(3)));
                    player.sendMessage("您的游戏模式已被更新为旁观模式");
                    OpSendMessage("&7[Server: 将" + player.getName() + "的游戏模式改为旁观模式]", sender.getName());
                }
                return false;
            }
            sender.sendMessage(ChatColor("&c&l用法错误,正确用法:/" + label + " <模式> <玩家ID>\n&a&l<>是必填,[]是选填"));
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            ArrayList<String> tablist = new ArrayList<>();
            tablist.add("冒险");
            tablist.add("创造");
            tablist.add("旁观");
            tablist.add("生存");
            return tablist;
        }
        return null;
    }
}
