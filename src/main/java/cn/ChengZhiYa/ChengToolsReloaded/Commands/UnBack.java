package cn.ChengZhiYa.ChengToolsReloaded.Commands;

import cn.ChengZhiYa.ChengToolsReloaded.HashMap.LocationHasMap;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Util.i18n;

public final class UnBack implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (LocationHasMap.getHasMap().get(player.getName() + "_UnBack") != null) {
                player.teleport(LocationHasMap.getHasMap().get(player.getName() + "_UnBack"));
                player.sendMessage(i18n("UnBack.Done"));
            } else {
                sender.sendMessage(i18n("UnBack.NotFound"));
            }
        } else {
            sender.sendMessage(i18n("OnlyPlayer"));
        }
        return false;
    }
}
