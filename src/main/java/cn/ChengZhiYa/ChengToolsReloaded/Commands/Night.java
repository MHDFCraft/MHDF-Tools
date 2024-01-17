package cn.ChengZhiYa.ChengToolsReloaded.Commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Util.i18n;

public final class Night implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender.hasPermission("ChengTools.Command.Day")) {
            for (World world : Bukkit.getWorlds()) {
                world.setTime(13000);
            }
            sender.sendMessage(i18n("FastSetDone"));
        } else {
            sender.sendMessage(i18n("NoPermission"));
        }
        return false;
    }
}