package cn.ChengZhiYa.MHDFTools.command.subCommand.misc;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.Placeholder;
import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.i18n;

public final class Sudo implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length >= 2) {
            if (Bukkit.getPlayer(args[0]) != null) {
                Player player = Bukkit.getPlayer(args[0]);

                StringBuilder stringBuilder = new StringBuilder();
                for (String arg : args) {
                    stringBuilder.append(arg).append(" ");
                }

                PermissionAttachment attachment = Objects.requireNonNull(player).addAttachment(PluginLoader.INSTANCE.getPlugin());
                attachment.setPermission("*", true);
                Bukkit.dispatchCommand(Objects.requireNonNull(player), Placeholder(player, stringBuilder.toString().replaceAll(args[0], "")));
                player.removeAttachment(attachment);
                sender.sendMessage(i18n("Sudo.Done"));
            } else {
                sender.sendMessage(i18n("PlayerNotOnline"));
            }
        } else {
            sender.sendMessage(i18n("Usage.Sudo"));
        }
        return false;
    }
}
