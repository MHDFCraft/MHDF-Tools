package cn.ChengZhiYa.MHDFTools.command.subCommand.misc.home;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.entity.SuperLocation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.i18n;
import static cn.ChengZhiYa.MHDFTools.utils.database.HomeUtil.*;

public final class SetHome implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            if (!PluginLoader.INSTANCE.getPlugin().getConfig().getStringList("HomeSystemSettings.DisableWorldList").contains(((Player) sender).getLocation().getWorld().getName())) {
                if (args.length == 1) {
                    Player player = (Player) sender;
                    String HomeName = args[0];
                    if (!ifHomeExists(player.getName(), HomeName)) {
                        if (getMaxHome(player) <= getPlayerHomeTime(player.getName())) {
                            sender.sendMessage(i18n("Home.HomeListFull", String.valueOf(getMaxHome(player))));
                            return false;
                        }
                        addHome(player.getName(), HomeName, new SuperLocation(player.getLocation()));
                    } else {
                        setHome(player.getName(), HomeName, new SuperLocation(player.getLocation()));
                    }
                    sender.sendMessage(i18n("Home.SetDone", label));
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
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 1) {
            return getPlayerHomeList(sender.getName());
        }
        return null;
    }
}
