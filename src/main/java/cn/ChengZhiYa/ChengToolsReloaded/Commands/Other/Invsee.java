package cn.ChengZhiYa.ChengToolsReloaded.Commands.Other;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Util.i18n;

public final class Invsee implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            if (sender.hasPermission("ChengTools.Invsee")) {
                if (args.length == 1) {
                    Player player = (Player) sender;
                    if (Bukkit.getPlayer(args[0]) == null) {
                        sender.sendMessage(i18n("PlayerNotOnline"));
                        return false;
                    }
                    Player Player = Bukkit.getPlayer(args[0]);
                    player.openInventory(Objects.requireNonNull(Player).getInventory());
                } else {
                    sender.sendMessage(i18n("Usage.Invsee"));
                }
            } else {
                sender.sendMessage(i18n("NoPermission"));
            }
        } else {
            sender.sendMessage(i18n("OnlyPlayer"));
        }
        return false;
    }
}
