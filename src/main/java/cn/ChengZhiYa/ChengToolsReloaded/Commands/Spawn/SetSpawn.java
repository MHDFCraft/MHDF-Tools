package cn.ChengZhiYa.ChengToolsReloaded.Commands.Spawn;

import cn.ChengZhiYa.ChengToolsReloaded.main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public class SetSpawn implements CommandExecutor {
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
                main.main.getConfig().set("SpawnSettings.world", World);
                main.main.getConfig().set("SpawnSettings.X", X);
                main.main.getConfig().set("SpawnSettings.Y", Y);
                main.main.getConfig().set("SpawnSettings.Z", Z);
                main.main.getConfig().set("SpawnSettings.Yaw", Yaw);
                main.main.getConfig().set("SpawnSettings.Pitch", Pitch);
                main.main.saveConfig();
                main.main.reloadConfig();
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
