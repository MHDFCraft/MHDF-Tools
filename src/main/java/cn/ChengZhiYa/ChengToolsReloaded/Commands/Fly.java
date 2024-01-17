package cn.ChengZhiYa.ChengToolsReloaded.Commands;

import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHasMap;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Util.i18n;

public final class Fly implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            if (sender.hasPermission("ChengTools.Command.Fly")) {
                Player player = (Player) sender;
                if (StringHasMap.getHasMap().get(player.getName() + "_Fly") == null) {
                    player.setAllowFlight(true);
                    player.sendMessage(i18n("Fly.Done", i18n("Fly.Enable")));
                    StringHasMap.getHasMap().put(player.getName() + "_Fly", "已启用");
                } else {
                    player.setAllowFlight(false);
                    player.sendMessage(i18n("Fly.Done", i18n("Fly.Disabled")));
                    StringHasMap.getHasMap().put(player.getName() + "_Fly", null);
                }
            } else {
                sender.sendMessage(i18n("NoPermission"));
            }
        }
        if (args.length == 1) {
            if (sender.hasPermission("ChengTools.Fly.Give")) {
                String PlayerName = args[0];
                if (Bukkit.getPlayer(PlayerName) == null) {
                    sender.sendMessage(i18n("PlayerNotOnline"));
                    return false;
                }
                Player player = Bukkit.getPlayer(PlayerName);
                assert player != null;
                if (StringHasMap.getHasMap().get(player.getName() + "_Fly") == null) {
                    player.setAllowFlight(true);
                    player.sendMessage(i18n("Fly.Done", i18n("Fly.Enable")));
                    sender.sendMessage(i18n("Fly.SetDone", player.getName(), i18n("Fly.Enable")));
                    StringHasMap.getHasMap().put(player.getName() + "_Fly", "已启用");
                } else {
                    player.setAllowFlight(false);
                    player.sendMessage(i18n("Fly.Done", i18n("Fly.Disabled")));
                    sender.sendMessage(i18n("Fly.SetDone", player.getName(), i18n("Fly.Disabled")));
                    StringHasMap.getHasMap().put(player.getName() + "_Fly", null);
                }
            } else {
                sender.sendMessage(i18n("NoPermission"));
            }
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(i18n("Usage.Fly"));
            }
        }
        return false;
    }
}