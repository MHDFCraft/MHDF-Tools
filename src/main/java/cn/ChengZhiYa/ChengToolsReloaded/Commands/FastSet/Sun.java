package cn.ChengZhiYa.ChengToolsReloaded.Commands.FastSet;

import cn.ChengZhiYa.ChengToolsReloaded.Ultis.*;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public final class Sun implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("ChengTools.Sun")) {
            for (World world : Bukkit.getWorlds()) {
                world.setStorm(false);
                world.setThundering(false);
            }
            sender.sendMessage(getLang("FastSetDone"));
        } else {
            sender.sendMessage(getLang("NoPermission"));
        }
        return false;
    }
}
