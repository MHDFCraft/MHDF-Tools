package cn.ChengZhiYa.MHDFTools.command;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.MHDFTools.util.Util.i18n;

public final class Night implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length > 0) {
            return false;
        }

        for (World world : Bukkit.getWorlds()) {
            world.setTime(13000);
        }

        sender.sendMessage(i18n("FastSetDone"));
        return true;
    }
}