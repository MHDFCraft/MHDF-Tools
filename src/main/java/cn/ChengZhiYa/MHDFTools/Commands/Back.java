package cn.ChengZhiYa.MHDFTools.Commands;

import cn.ChengZhiYa.MHDFTools.HashMap.IntHasMap;
import cn.ChengZhiYa.MHDFTools.HashMap.LocationHasMap;
import cn.ChengZhiYa.MHDFTools.MHDFTools;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.MHDFTools.Utils.BCUtil.ServerName;
import static cn.ChengZhiYa.MHDFTools.Utils.BCUtil.TpPlayerTo;
import static cn.ChengZhiYa.MHDFTools.Utils.Util.i18n;

public final class Back implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (LocationHasMap.getHasMap().get(player.getName() + "_DeathLocation") != null) {
                if (MHDFTools.instance.getConfig().getStringList("BackSettings.DisableWorldList").contains(LocationHasMap.getHasMap().get(player.getName() + "_DeathLocation").getWorld().getName()) || MHDFTools.instance.getConfig().getStringList("BackSettings.DisableWorldList").contains(player.getLocation().getWorld().getName())) {
                    return false;
                }
                if (IntHasMap.getHasMap().get(player.getName() + "_BackDelay") != null) {
                    player.sendMessage(i18n("Back.Delay"));
                    return false;
                }
                LocationHasMap.getHasMap().put(player.getName() + "_UnBackLocation", player.getLocation());
                IntHasMap.getHasMap().put(player.getName() + "_BackDelay", MHDFTools.instance.getConfig().getInt("BackSettings.Delay"));
                Bukkit.getScheduler().runTaskLaterAsynchronously(MHDFTools.instance, () -> IntHasMap.getHasMap().remove(player.getName() + "_BackDelay"), 20L * MHDFTools.instance.getConfig().getInt("BackSettings.Delay"));
                TpPlayerTo(player.getName(), ServerName,
                        LocationHasMap.getHasMap().get(player.getName() + "_DeathLocation")
                );
                player.sendMessage(i18n("Back.Done"));
            } else {
                sender.sendMessage(i18n("Back.NotFound"));
            }
        } else {
            sender.sendMessage(i18n("OnlyPlayer"));
        }
        return false;
    }
}
