package cn.ChengZhiYa.MHDFTools.Commands;

import cn.ChengZhiYa.MHDFTools.HashMap.LocationHasMap;
import cn.ChengZhiYa.MHDFTools.HashMap.StringHasMap;
import cn.ChengZhiYa.MHDFTools.MHDFTools;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.MHDFTools.Utils.BCUtil.*;
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
                SaveLocation(player.getName() + "_UnBackLocation", ServerName, player.getLocation());
                TpPlayerTo(player.getName(),
                        StringHasMap.getHasMap().get(player.getName() + "_DeathLocation_Server"),
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