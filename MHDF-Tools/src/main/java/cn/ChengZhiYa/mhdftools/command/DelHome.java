package cn.ChengZhiYa.MHDFTools.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static cn.ChengZhiYa.MHDFTools.util.Util.i18n;
import static cn.ChengZhiYa.MHDFTools.util.database.HomeUtil.*;

public final class DelHome implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 1) {
                Player player = (Player) sender;
                String HomeName = args[0];
                if (ifHomeExists(player.getName(), HomeName)) {
                    RemoveHome(player.getName(), HomeName);
                    player.sendMessage(i18n("Home.RemoveDone"));
                } else {
                    player.sendMessage(i18n("Home.NotFound", HomeName));
                }
            } else {
                sender.sendMessage(i18n("Usage.Home", label));
            }
        } else {
            sender.sendMessage(i18n("OnlyPlayer"));
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 1) {
                Player player = (Player) sender;
                return getPlayerHomeList(player.getName());
            }
        }
        return null;
    }
}
