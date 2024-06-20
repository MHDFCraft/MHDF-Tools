package cn.ChengZhiYa.MHDFTools.command.subCommand.misc;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.utils.Util.i18n;

public final class InvSee implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
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
            sender.sendMessage(i18n("OnlyPlayer"));
        }
        return false;
    }
}