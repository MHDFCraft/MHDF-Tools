package cn.ChengZhiYa.ChengToolsReloaded.Commands.Other;

import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHasMap;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Util.i18n;

public final class Freeze implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender.hasPermission("ChengTools.Freeze")) {
            if (args.length == 1) {
                String PlayerName = args[0];
                if (Bukkit.getPlayer(PlayerName) == null) {
                    sender.sendMessage(i18n("PlayerNotOnline"));
                    return false;
                }
                if (StringHasMap.getHasMap().get(PlayerName + "_Freeze") == null) {
                    StringHasMap.getHasMap().put(PlayerName + "_Freeze", "t");
                    sender.sendMessage(i18n("Freeze.Done"));
                } else {
                    StringHasMap.getHasMap().remove(PlayerName + "_Freeze");
                    sender.sendMessage(i18n("Freeze.UnDone"));
                }
            } else {
                sender.sendMessage(i18n("Usage.Freeze"));
            }
        } else {
            sender.sendMessage(i18n("NoPermission"));
        }
        return false;
    }
}
