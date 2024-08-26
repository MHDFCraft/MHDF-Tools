package cn.ChengZhiYa.MHDFTools.command.subcommand.misc.home;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.i18n;
import static cn.ChengZhiYa.MHDFTools.utils.database.HomeUtil.*;

public final class DelHome implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            if (!PluginLoader.INSTANCE.getPlugin().getConfig().getStringList("HomeSystemSettings.DisableWorldList").contains(((Player) sender).getLocation().getWorld().getName())) {
                Player player = (Player) sender;
                if (args.length == 1) {
                    String homeName = args[0];
                    if (ifHomeExists(player.getName(), homeName)) {
                        removeHome(player.getName(), homeName);
                        player.sendMessage(i18n("Home.RemoveDone"));
                    } else {
                        player.sendMessage(i18n("Home.NotFound", homeName));
                    }
                } else {
                    sender.sendMessage(i18n("Usage.Home", label));
                }
            }
        } else {
            sender.sendMessage(i18n("OnlyPlayer"));
        }
        return false;
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