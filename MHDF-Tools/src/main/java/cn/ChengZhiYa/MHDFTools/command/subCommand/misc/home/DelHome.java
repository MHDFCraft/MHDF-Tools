package cn.ChengZhiYa.MHDFTools.command.subCommand.misc.home;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import static cn.ChengZhiYa.MHDFTools.utils.Util.i18n;
import static cn.ChengZhiYa.MHDFTools.utils.database.HomeUtil.*;

public final class DelHome implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(i18n("OnlyPlayer"));
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 1) {
            sender.sendMessage(i18n("Usage.Home", label));
            return true;
        }

        String homeName = args[0];

        if (!ifHomeExists(player.getName(), homeName)) {
            player.sendMessage(i18n("Home.NotFound", homeName));
            return true;
        }

        RemoveHome(player.getName(), homeName);
        player.sendMessage(i18n("Home.RemoveDone"));
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            return Collections.emptyList();
        }

        if (args.length == 1) {
            Player player = (Player) sender;
            return getPlayerHomeList(player.getName());
        }

        return Collections.emptyList();
    }
}