package cn.ChengZhiYa.MHDFTools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.MHDFTools.utils.databases.FlyUtil.addFly;
import static cn.ChengZhiYa.MHDFTools.utils.databases.FlyUtil.getFlyTime;
import static cn.ChengZhiYa.MHDFTools.utils.Util.getTimeString;
import static cn.ChengZhiYa.MHDFTools.utils.Util.i18n;

public final class FlyTime implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender.hasPermission("MHDFTools.Fly.Give")) {
            if (args.length == 2) {
                addFly(args[0], Integer.parseInt(args[1]));
                sender.sendMessage(i18n("FlyTime.SetDone", args[0], getTimeString(Integer.parseInt(args[1]))));
            } else {
                sender.sendMessage(i18n("Usage.FlyTime"));
            }
        } else {
            sender.sendMessage(i18n("FlyTime.GetTime", getTimeString(getFlyTime(sender.getName()))));
        }
        return false;
    }
}
