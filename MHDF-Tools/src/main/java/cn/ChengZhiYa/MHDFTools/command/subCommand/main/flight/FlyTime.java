package cn.ChengZhiYa.MHDFTools.command.subCommand.main.flight;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.getTimeString;
import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.i18n;
import static cn.ChengZhiYa.MHDFTools.utils.database.FlyUtil.addFlyTime;
import static cn.ChengZhiYa.MHDFTools.utils.database.FlyUtil.getFlyTime;

public final class FlyTime implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender.hasPermission("MHDFTools.Fly.Give")) {
            if (args.length == 2) {
                addFlyTime(args[0], Integer.parseInt(args[1]));
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
