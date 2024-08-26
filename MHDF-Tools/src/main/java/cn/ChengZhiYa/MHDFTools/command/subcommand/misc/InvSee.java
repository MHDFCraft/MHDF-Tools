package cn.ChengZhiYa.MHDFTools.command.subcommand.misc;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.i18n;

public final class InvSee implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(i18n("OnlyPlayer"));
            return false;
        }

        if (args.length != 1) {
            sender.sendMessage(i18n("Usage.Invsee"));
            return false;
        }

        Player player = (Player) sender;
        Player targetPlayer = Bukkit.getPlayer(args[0]);

        if (targetPlayer == null) {
            sender.sendMessage(i18n("PlayerNotOnline"));
            return false;
        }

        player.openInventory(targetPlayer.getInventory());

        return true;
    }
}