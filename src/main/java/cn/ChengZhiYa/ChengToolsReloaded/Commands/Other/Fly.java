package cn.ChengZhiYa.ChengToolsReloaded.Commands.Other;

import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHasMap;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Util.getLang;

public final class Fly implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            if (sender.hasPermission("ChengTools.Fly")) {
                Player player = (Player) sender;
                if (StringHasMap.getHasMap().get(player.getName() + "_Fly") == null) {
                    player.setAllowFlight(true);
                    player.sendMessage(getLang("Fly.Done", getLang("Fly.Enable")));
                    StringHasMap.getHasMap().put(player.getName() + "_Fly", "已启用");
                } else {
                    player.setAllowFlight(false);
                    player.sendMessage(getLang("Fly.Done", getLang("Fly.Disabled")));
                    StringHasMap.getHasMap().put(player.getName() + "_Fly", null);
                }
            } else {
                sender.sendMessage(getLang("NoPermission"));
            }
        }
        if (args.length == 1) {
            if (sender.hasPermission("ChengTools.Fly.Give")) {
                String PlayerName = args[0];
                if (Bukkit.getPlayer(PlayerName) == null) {
                    sender.sendMessage(getLang("PlayerNotOnline"));
                    return false;
                }
                Player player = Bukkit.getPlayer(PlayerName);
                assert player != null;
                if (StringHasMap.getHasMap().get(player.getName() + "_Fly") == null) {
                    player.setAllowFlight(true);
                    player.sendMessage(getLang("Fly.Done", getLang("Fly.Enable")));
                    sender.sendMessage(getLang("Fly.SetDone", player.getName(), getLang("Fly.Enable")));
                    StringHasMap.getHasMap().put(player.getName() + "_Fly", "已启用");
                } else {
                    player.setAllowFlight(false);
                    player.sendMessage(getLang("Fly.Done", getLang("Fly.Disabled")));
                    sender.sendMessage(getLang("Fly.SetDone", player.getName(), getLang("Fly.Disabled")));
                    StringHasMap.getHasMap().put(player.getName() + "_Fly", null);
                }
            } else {
                sender.sendMessage(getLang("NoPermission"));
            }
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(getLang("Usage.Fly"));
            }
        }
        return false;
    }
}