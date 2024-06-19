package cn.chengzhiya.mhdftools.command;

import cn.chengzhiya.mhdftools.MHDFTools;
import cn.chengzhiya.mhdftools.hashmap.IntHasMap;
import cn.chengzhiya.mhdftools.hashmap.LocationHasMap;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static cn.chengzhiya.mhdftools.util.Util.i18n;

public final class Back implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (LocationHasMap.getHasMap().get(player.getName() + "_DeathLocation") != null) {
                if (MHDFTools.instance.getConfig().getStringList("BackSettings.DisableWorldList").contains(LocationHasMap.getHasMap().get(player.getName() + "_DeathLocation").getWorld().getName()) || MHDFTools.instance.getConfig().getStringList("BackSettings.DisableWorldList").contains(player.getLocation().getWorld().getName())) {
                    return false;
                }
                if (IntHasMap.getHasMap().get(player.getName() + "_BackDelay") != null || IntHasMap.getHasMap().get(player.getName() + "_TpBackDelay") != null) {
                    player.sendMessage(i18n("Back.RepectSend"));
                    return false;
                }
                IntHasMap.getHasMap().put(player.getName() + "_BackDelay", MHDFTools.instance.getConfig().getInt("BackSettings.Delay"));
            } else {
                sender.sendMessage(i18n("Back.NotFound"));
            }
        } else {
            sender.sendMessage(i18n("OnlyPlayer"));
        }
        return false;
    }
}
