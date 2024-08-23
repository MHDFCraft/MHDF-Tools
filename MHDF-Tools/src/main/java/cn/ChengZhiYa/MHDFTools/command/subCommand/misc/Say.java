package cn.ChengZhiYa.MHDFTools.command.subCommand.misc;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.Placeholder;
import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.i18n;

public final class Say implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length > 0) {
            String message = args.length == 1 ? args[0] : String.join(" ", Arrays.copyOfRange(args, 1, args.length));
            Bukkit.broadcast(Placeholder(null, message), "MHDFTools.Broadcast");
        } else {
            sender.sendMessage(i18n("Usage.Say"));
        }
        return false;
    }
}