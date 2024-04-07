package cn.ChengZhiYa.MHDFTools.Commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.MHDFTools.Utils.Util.i18n;

public final class Day implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender.hasPermission("MHDFTools.Command.Day")) {
            for (World world : Bukkit.getWorlds()) {
                world.setTime(1000);
            }
            sender.sendMessage(i18n("FastSetDone"));
        } else {
            sender.sendMessage(i18n("NoPermission"));
        }
        return false;
    }
}
