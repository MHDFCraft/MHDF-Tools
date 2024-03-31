package cn.ChengZhiYa.MHDFTools.commands;

import cn.ChengZhiYa.MHDFTools.map.LocationHasMap;
import cn.ChengZhiYa.MHDFTools.MHDFTools;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.MHDFTools.utils.BCUtil.ServerName;
import static cn.ChengZhiYa.MHDFTools.utils.BCUtil.TpPlayerTo;
import static cn.ChengZhiYa.MHDFTools.utils.Util.i18n;

public final class UnBack implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (LocationHasMap.getHasMap().get(player.getName() + "_UnBackLocation") != null) {
                if (MHDFTools.instance.getConfig().getStringList("BackSettings.DisableWorldList").contains(LocationHasMap.getHasMap().get(player.getName() + "_UnBackLocation").getWorld().getName()) || MHDFTools.instance.getConfig().getStringList("TpBackSettings.DisableWorldList").contains(player.getLocation().getWorld().getName())) {
                    return false;
                }
                if (MHDFTools.instance.getConfig().getStringList("TpBackSettings.DisableWorldList").contains(LocationHasMap.getHasMap().get(player.getName() + "_UnBackLocation").getWorld().getName()) || MHDFTools.instance.getConfig().getStringList("TpBackSettings.DisableWorldList").contains(player.getLocation().getWorld().getName())) {
                    return false;
                }
                TpPlayerTo(player.getName(), ServerName,
                        LocationHasMap.getHasMap().get(player.getName() + "_UnBackLocation")
                );
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
