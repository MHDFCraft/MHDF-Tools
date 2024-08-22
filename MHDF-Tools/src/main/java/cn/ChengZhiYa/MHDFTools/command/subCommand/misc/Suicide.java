package cn.ChengZhiYa.MHDFTools.command.subCommand.misc;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.i18n;

public final class Suicide implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            ((Player) sender).setHealth(0);
            sender.sendMessage(i18n("Suicide.Done"));
        } else {
            sender.sendMessage(i18n("Usage.Suicide"));
        }
        return false;
    }
}
