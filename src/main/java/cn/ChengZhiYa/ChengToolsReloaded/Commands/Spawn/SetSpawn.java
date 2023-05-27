package cn.ChengZhiYa.ChengToolsReloaded.Commands.Spawn;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import cn.ChengZhiYa.ChengToolsReloaded.Ultis.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public final class SetSpawn implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("ChengTools.SetSpawn")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                String World = player.getLocation().getWorld().getName();
                double X = player.getLocation().getX();
                double Y = player.getLocation().getY();
                double Z = player.getLocation().getZ();
                double Yaw = player.getLocation().getYaw();
                double Pitch = player.getLocation().getPitch();
                ChengToolsReloaded.instance.getConfig().set("SpawnSettings.world", World);
                ChengToolsReloaded.instance.getConfig().set("SpawnSettings.X", X);
                ChengToolsReloaded.instance.getConfig().set("SpawnSettings.Y", Y);
                ChengToolsReloaded.instance.getConfig().set("SpawnSettings.Z", Z);
                ChengToolsReloaded.instance.getConfig().set("SpawnSettings.Yaw", Yaw);
                ChengToolsReloaded.instance.getConfig().set("SpawnSettings.Pitch", Pitch);
                ChengToolsReloaded.instance.saveConfig();
                ChengToolsReloaded.instance.reloadConfig();
                sender.sendMessage(getLang("Spawn.SetDone"));
            } else {
                sender.sendMessage(getLang("OnlyPlayer"));
            }
        } else {
            sender.sendMessage(getLang("NoPermission"));
        }
        return false;
    }
}
