package cn.ChengZhiYa.ChengToolsReloaded.Commands.Other;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHasMap;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Util.getLang;

public final class Vanish implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            if (sender.hasPermission("ChengTools.Vanish")) {
                Player player = (Player) sender;
                if (StringHasMap.getHasMap().get(player.getName() + "_Vanish") == null) {
                    for (Player OnlinePlayer : Bukkit.getOnlinePlayers()) {
                        OnlinePlayer.hidePlayer(ChengToolsReloaded.instance, player);
                    }
                    PotionEffect INVISIBILITY = new PotionEffect(PotionEffectType.INVISIBILITY, 99999, 255, true);
                    player.addPotionEffect(INVISIBILITY);
                    player.sendMessage(getLang("Vanish.Done", getLang("Vanish.Enable")));
                    StringHasMap.getHasMap().put(player.getName() + "_Vanish", "已启用");
                } else {
                    for (Player OnlinePlayer : Bukkit.getOnlinePlayers()) {
                        OnlinePlayer.showPlayer(ChengToolsReloaded.instance, player);
                    }
                    player.removePotionEffect(PotionEffectType.INVISIBILITY);
                    player.sendMessage(getLang("Vanish.Done", getLang("Vanish.Disabled")));
                    StringHasMap.getHasMap().put(player.getName() + "_Vanish", null);
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
                if (StringHasMap.getHasMap().get(player.getName() + "_Vanish") == null) {
                    for (Player OnlinePlayer : Bukkit.getOnlinePlayers()) {
                        OnlinePlayer.hidePlayer(ChengToolsReloaded.instance, player);
                    }
                    PotionEffect INVISIBILITY = new PotionEffect(PotionEffectType.INVISIBILITY, 99999, 255, true);
                    player.addPotionEffect(INVISIBILITY);
                    player.sendMessage(getLang("Vanish.Done", getLang("Vanish.Enable")));
                    sender.sendMessage(getLang("Vanish.SetDone", player.getName(), getLang("Vanish.Enable")));
                    StringHasMap.getHasMap().put(player.getName() + "_Vanish", "已启用");
                } else {
                    for (Player OnlinePlayer : Bukkit.getOnlinePlayers()) {
                        OnlinePlayer.showPlayer(ChengToolsReloaded.instance, player);
                    }
                    player.removePotionEffect(PotionEffectType.INVISIBILITY);
                    player.sendMessage(getLang("Vanish.Done", getLang("Vanish.Disabled")));
                    sender.sendMessage(getLang("Vanish.SetDone", player.getName(), getLang("Vanish.Disabled")));
                    StringHasMap.getHasMap().put(player.getName() + "_Vanish", null);
                }
            } else {
                sender.sendMessage(getLang("NoPermission"));
            }
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(getLang("Usage.Vanish"));
            }
        }
        return false;
    }
}
