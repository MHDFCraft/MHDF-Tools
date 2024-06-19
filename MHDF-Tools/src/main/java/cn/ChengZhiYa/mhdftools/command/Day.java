package cn.chengzhiya.mhdftools.command;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static cn.chengzhiya.mhdftools.util.Util.i18n;

public final class Day implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        for (World world : Bukkit.getWorlds()) {
            world.setTime(1000);
        }
        sender.sendMessage(i18n("FastSetDone"));
        return false;
    }
}
