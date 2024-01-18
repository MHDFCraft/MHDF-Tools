package cn.ChengZhiYa.MHDFTools.Commands;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.MHDFTools.Utils.Util.i18n;

public final class SetSpawn implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender.hasPermission("MHDFTools.Command.SetSpawn")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                String World = player.getLocation().getWorld().getName();
                double X = player.getLocation().getX();
                double Y = player.getLocation().getY();
                double Z = player.getLocation().getZ();
                double Yaw = player.getLocation().getYaw();
                double Pitch = player.getLocation().getPitch();
                MHDFTools.instance.getConfig().set("SpawnSettings.world", World);
                MHDFTools.instance.getConfig().set("SpawnSettings.X", X);
                MHDFTools.instance.getConfig().set("SpawnSettings.Y", Y);
                MHDFTools.instance.getConfig().set("SpawnSettings.Z", Z);
                MHDFTools.instance.getConfig().set("SpawnSettings.Yaw", Yaw);
                MHDFTools.instance.getConfig().set("SpawnSettings.Pitch", Pitch);
                MHDFTools.instance.saveConfig();
                MHDFTools.instance.reloadConfig();
                sender.sendMessage(i18n("Spawn.SetDone"));
            } else {
                sender.sendMessage(i18n("OnlyPlayer"));
            }
        } else {
            sender.sendMessage(i18n("NoPermission"));
        }
        return false;
    }
}
