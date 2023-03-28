package cn.ChengZhiYa.ChengToolsReloaded.Commands.Other;

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

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public class Vanish implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            if (sender.hasPermission("ChengTools.Vanish")) {
                Player player = (Player) sender;
                if (StringHashMap.Get(player.getName() + "_Vanish") == null) {
                    for (Player OnlinePlayer : Bukkit.getOnlinePlayers()) {
                        OnlinePlayer.hidePlayer(main.main, player);
                    }
                    PotionEffect INVISIBILITY = new PotionEffect(PotionEffectType.INVISIBILITY, 99999, 255, true);
                    player.addPotionEffect(INVISIBILITY);
                    player.sendMessage(getLang("Vanish.Done", getLang("Vanish.True")));
                    StringHashMap.Set(player.getName() + "_Vanish", "已启用");
                } else {
                    for (Player OnlinePlayer : Bukkit.getOnlinePlayers()) {
                        OnlinePlayer.showPlayer(main.main, player);
                    }
                    player.removePotionEffect(PotionEffectType.INVISIBILITY);
                    player.sendMessage(getLang("Vanish.Done", getLang("Vanish.False")));
                    StringHashMap.Set(player.getName() + "_Vanish", null);
                }
            } else {
                sender.sendMessage(getLang("NoPermission"));
            }
        }
        if (args.length == 1) {
            if (sender.hasPermission("ChengTools.Vanish.Give")) {
                String PlayerName = args[0];
                if (Bukkit.getPlayer(PlayerName) == null) {
                    sender.sendMessage(getLang("PlayerNotOnline"));
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
                    player.sendMessage(getLang("Vanish.Done", getLang("Vanish.True")));
                    sender.sendMessage(getLang("Vanish.SetDone", player.getName(), getLang("Vanish.True")));
                    StringHashMap.Set(player.getName() + "_Vanish", "已启用");
                } else {
                    for (Player OnlinePlayer : Bukkit.getOnlinePlayers()) {
                        OnlinePlayer.showPlayer(main.main, player);
                    }
                    player.removePotionEffect(PotionEffectType.INVISIBILITY);
                    player.sendMessage(getLang("Vanish.Done", getLang("Vanish.False")));
                    sender.sendMessage(getLang("Vanish.SetDone", player.getName(), getLang("Vanish.False")));
                    StringHashMap.Set(player.getName() + "_Vanish", null);
                }
            } else {
                sender.sendMessage(getLang("NoPermission"));
            }
        }else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(getLang("Usage.Vanish"));
            }
        }
        return false;
    }
}
