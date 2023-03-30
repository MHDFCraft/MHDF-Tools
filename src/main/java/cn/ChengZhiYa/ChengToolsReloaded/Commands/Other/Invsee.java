package cn.ChengZhiYa.ChengToolsReloaded.Commands.Other;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public class Invsee implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            if (sender.hasPermission(" ChengTools.Gamemode")) {
                if (args.length == 1) {
                    Player player = (Player) sender;
                    if (Bukkit.getPlayer(args[0]) == null) {
                        sender.sendMessage(getLang("PlayerNotOnline"));
                        return false;
                    }
                    Player Player = Bukkit.getPlayer(args[0]);
                    player.openInventory(Objects.requireNonNull(Player).getInventory());
                } else {
                    sender.sendMessage(getLang("Usage.Invsee"));
                }
            } else {
                sender.sendMessage(getLang("NoPermission"));
            }
        } else {
            sender.sendMessage(getLang("OnlyPlayer"));
        }
        return false;
    }
}
