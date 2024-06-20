package cn.ChengZhiYa.MHDFTools.command.subCommand.misc.spawn;

import cn.ChengZhiYa.MHDFTools.utils.BungeeCord;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.i18n;

public final class SetSpawn implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            BungeeCord.SetSpawn(player.getLocation());
            sender.sendMessage(i18n("Spawn.SetDone"));
        } else {
            sender.sendMessage(i18n("OnlyPlayer"));
        }
        return false;
    }
}
