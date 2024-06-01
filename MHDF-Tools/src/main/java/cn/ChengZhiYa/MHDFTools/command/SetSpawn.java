package cn.ChengZhiYa.MHDFTools.command;

import cn.ChengZhiYa.MHDFTools.util.BCUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.MHDFTools.util.Util.i18n;

public final class SetSpawn implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            BCUtil.SetSpawn(player.getLocation());
            sender.sendMessage(i18n("Spawn.SetDone"));
        } else {
            sender.sendMessage(i18n("OnlyPlayer"));
        }
        return false;
    }
}
