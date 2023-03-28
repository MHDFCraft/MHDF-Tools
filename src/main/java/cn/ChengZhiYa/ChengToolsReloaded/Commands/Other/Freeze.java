package cn.ChengZhiYa.ChengToolsReloaded.Commands.Other;

import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHashMap;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public class Freeze implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("ChengTools.Freeze")) {
            if (args.length == 1) {
                String PlayerName = args[0];
                if (Bukkit.getPlayer(PlayerName) == null) {
                    sender.sendMessage(getLang("PlayerNotOnline"));
                    return false;
                }
                if (StringHashMap.Get(PlayerName + "_Freeze") == null) {
                    StringHashMap.Set(PlayerName + "_Freeze", "t");
                    sender.sendMessage(getLang("Freeze.Done"));
                } else {
                    StringHashMap.Remove(PlayerName + "_Freeze");
                    sender.sendMessage(getLang("Freeze.UnDone"));
                }
            } else {
                sender.sendMessage(getLang("Usage.Freeze"));
            }
        } else {
            sender.sendMessage(getLang("NoPermission"));
        }
        return false;
    }
}
