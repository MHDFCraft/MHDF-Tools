package cn.ChengZhiYa.MHDFTools.command.subCommand.misc;

import cn.ChengZhiYa.MHDFTools.utils.BungeeCordUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.i18n;

public final class Feed implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Player player = sender instanceof Player ? (Player) sender : null;
        if (args.length == 1) {
            if (!BungeeCordUtil.ifPlayerOnline(args[0])) {
                sender.sendMessage(i18n("PlayerNotOnline"));
                return false;
            }
            player = Bukkit.getPlayer(args[0]);
        }
        if (player != null) {
            player.setFoodLevel(20);
            player.setSaturation(20);
            sender.sendMessage(i18n("Feed.Done"));
        } else {
            sender.sendMessage(i18n("Usage.Feed"));
        }
        return false;
    }
}
