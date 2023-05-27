package cn.ChengZhiYa.ChengToolsReloaded.Commands.Back;

import cn.ChengZhiYa.ChengToolsReloaded.HashMap.LocationHasMap;
import cn.ChengZhiYa.ChengToolsReloaded.Ultis.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public final class UnBack implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (LocationHasMap.getHasMap().get(player.getName() + "_UnBack") != null) {
                player.teleport(LocationHasMap.getHasMap().get(player.getName() + "_UnBack"));
                player.sendMessage(getLang("UnBack.Done"));
            } else {
                sender.sendMessage(getLang("UnBack.NotFound"));
            }
        } else {
            sender.sendMessage(getLang("OnlyPlayer"));
        }
        return false;
    }
}
