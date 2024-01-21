package cn.ChengZhiYa.MHDFTools.Commands;

import cn.ChengZhiYa.MHDFTools.HashMap.LocationHasMap;
import cn.ChengZhiYa.MHDFTools.HashMap.StringHasMap;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.MHDFTools.Utils.BCUtil.TpPlayerTo;
import static cn.ChengZhiYa.MHDFTools.Utils.Util.i18n;

public final class UnBack implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (LocationHasMap.getHasMap().get(player.getName() + "_UnBackLocation") != null) {
                TpPlayerTo(player.getName(),
                        StringHasMap.getHasMap().get(player.getName() + "_UnBackLocation_Server"),
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
