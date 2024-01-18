package cn.ChengZhiYa.MHDFTools.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.MHDFTools.Utils.Database.HomeUtil.*;
import static cn.ChengZhiYa.MHDFTools.Utils.Util.i18n;

public final class SetHome implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 1) {
                Player player = (Player) sender;
                String HomeName = args[0];
                if (getMaxHome(player) <= getPlayerHomeTime(player.getName())) {
                    sender.sendMessage(i18n("Home.HomeListFull", String.valueOf(getMaxHome(player))));
                    return false;
                }
                AddHome(player.getName(), HomeName, player.getLocation());
                sender.sendMessage(i18n("Home.SetDone", label));
            } else {
                sender.sendMessage(i18n("Usage.Home", label));
            }
        } else {
            sender.sendMessage(i18n("OnlyPlayer"));
        }
        return false;
    }
}
