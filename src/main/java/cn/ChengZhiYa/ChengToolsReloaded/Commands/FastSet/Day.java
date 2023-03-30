package cn.ChengZhiYa.ChengToolsReloaded.Commands.FastSet;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public class Day implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("ChengTools.Day")) {
            for (World world : Bukkit.getWorlds()) {
                world.setTime(1000);
            }
            sender.sendMessage(getLang("FastSetDone"));
        } else {
            sender.sendMessage(getLang("NoPermission"));
        }
        return false;
    }
}
