package cn.ChengZhiYa.ChengToolsReloaded.Commands;

import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHashMap;
import cn.ChengZhiYa.ChengToolsReloaded.main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.ChatColor;

public class Vanish_Command implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {
            if (sender.hasPermission("ChengTools.Vanish")) {
                if (args.length == 1) {
                    if (sender.hasPermission("ChengTools.Vanish.Give")) {
                        String PlayerName = args[0];
                        if (Bukkit.getPlayer(PlayerName) == null) {
                            sender.sendMessage(ChatColor("&c&l这个玩家不存在或不在线"));
                            return false;
                        }
                        Player player = Bukkit.getPlayer(PlayerName);
                        assert player != null;
                        if (StringHashMap.Get(player.getName() + "_Vanish") == null) {
                            for (Player OnlinePlayer : Bukkit.getOnlinePlayers()) {
                                OnlinePlayer.hidePlayer(main.main, player);
                            }
                            PotionEffect INVISIBILITY = new PotionEffect(PotionEffectType.INVISIBILITY, 99999, 255, true);
                            player.addPotionEffect(INVISIBILITY);
                            player.sendMessage(ChatColor("&6&l您的隐身模式已启用"));
                            sender.sendMessage(ChatColor("&a&l已将" + player.getName() + "的隐身模式启用"));
                            StringHashMap.Set(player.getName() + "_Vanish", "已启用");
                        } else {
                            for (Player OnlinePlayer : Bukkit.getOnlinePlayers()) {
                                OnlinePlayer.showPlayer(main.main, player);
                            }
                            player.removePotionEffect(PotionEffectType.INVISIBILITY);
                            player.sendMessage(ChatColor("&6&l您的隐身模式已关闭"));
                            sender.sendMessage(ChatColor("&a&l已将" + player.getName() + "的隐身模式关闭"));
                            StringHashMap.Set(player.getName() + "_Vanish", null);
                        }
                    } else {
                        Player player = (Player) sender;
                        if (StringHashMap.Get(player.getName() + "_Vanish") == null) {
                            for (Player OnlinePlayer : Bukkit.getOnlinePlayers()) {
                                OnlinePlayer.hidePlayer(main.main, player);
                            }
                            PotionEffect INVISIBILITY = new PotionEffect(PotionEffectType.INVISIBILITY, 99999, 255, true);
                            player.addPotionEffect(INVISIBILITY);
                            player.sendMessage(ChatColor("&6&l您的隐身模式已启用"));
                            StringHashMap.Set(player.getName() + "_Vanish", "已启用");
                        } else {
                            for (Player OnlinePlayer : Bukkit.getOnlinePlayers()) {
                                OnlinePlayer.showPlayer(main.main, player);
                            }
                            player.removePotionEffect(PotionEffectType.INVISIBILITY);
                            player.sendMessage(ChatColor("&6&l您的隐身模式已关闭"));
                            StringHashMap.Set(player.getName() + "_Vanish", null);
                        }
                    }
                } else {
                    Player player = (Player) sender;
                    if (StringHashMap.Get(player.getName() + "_Vanish") == null) {
                        for (Player OnlinePlayer : Bukkit.getOnlinePlayers()) {
                            OnlinePlayer.hidePlayer(main.main, player);
                        }
                        PotionEffect INVISIBILITY = new PotionEffect(PotionEffectType.INVISIBILITY, 99999, 255, true);
                        player.addPotionEffect(INVISIBILITY);
                        player.sendMessage(ChatColor("&6&l您的隐身模式已启用"));
                        StringHashMap.Set(player.getName() + "_Vanish", "已启用");
                    } else {
                        for (Player OnlinePlayer : Bukkit.getOnlinePlayers()) {
                            OnlinePlayer.showPlayer(main.main, player);
                        }
                        player.removePotionEffect(PotionEffectType.INVISIBILITY);
                        player.sendMessage(ChatColor("&6&l您的隐身模式已关闭"));
                        StringHashMap.Set(player.getName() + "_Vanish", null);
                    }
                }
            } else {
                sender.sendMessage(ChatColor("&c&l您没有权限怎么做!"));
            }
        } else {
            if (args.length == 1) {
                if (sender.hasPermission("ChengTools.Vanish.Give")) {
                    String PlayerName = args[0];
                    if (Bukkit.getPlayer(PlayerName) == null) {
                        sender.sendMessage(ChatColor("&c&l这个玩家不存在或不在线"));
                        return false;
                    }
                    Player player = Bukkit.getPlayer(PlayerName);
                    assert player != null;
                    if (StringHashMap.Get(player.getName() + "_Vanish") == null) {
                        for (Player OnlinePlayer : Bukkit.getOnlinePlayers()) {
                            OnlinePlayer.hidePlayer(main.main, player);
                        }
                        PotionEffect INVISIBILITY = new PotionEffect(PotionEffectType.INVISIBILITY, 99999, 255, true);
                        player.addPotionEffect(INVISIBILITY);
                        player.sendMessage(ChatColor("&6&l您的隐身模式已启用"));
                        sender.sendMessage(ChatColor("&a&l已将" + player.getName() + "的隐身模式启用"));
                        StringHashMap.Set(player.getName() + "_Vanish", "已启用");
                    } else {
                        for (Player OnlinePlayer : Bukkit.getOnlinePlayers()) {
                            OnlinePlayer.showPlayer(main.main, player);
                        }
                        player.removePotionEffect(PotionEffectType.INVISIBILITY);
                        player.sendMessage(ChatColor("&6&l您的隐身模式已关闭"));
                        sender.sendMessage(ChatColor("&a&l已将" + player.getName() + "的隐身模式关闭"));
                        StringHashMap.Set(player.getName() + "_Vanish", null);
                    }
                }
                return false;
            }
            sender.sendMessage(ChatColor("&c&l用法错误,正确用法:/" + label + " <玩家ID>"));
        }
        return false;
    }
}
