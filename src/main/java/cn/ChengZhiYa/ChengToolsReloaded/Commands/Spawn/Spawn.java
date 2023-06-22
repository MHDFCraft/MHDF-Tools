package cn.ChengZhiYa.ChengToolsReloaded.Commands.Spawn;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public final class Spawn implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            World world = Bukkit.getWorld(Objects.requireNonNull(ChengToolsReloaded.instance.getConfig().getString("SpawnSettings.World")));
            double X = ChengToolsReloaded.instance.getConfig().getDouble("SpawnSettings.X");
            double Y = ChengToolsReloaded.instance.getConfig().getDouble("SpawnSettings.Y");
            double Z = ChengToolsReloaded.instance.getConfig().getDouble("SpawnSettings.Z");
            float Yaw = (float) ChengToolsReloaded.instance.getConfig().getDouble("SpawnSettings.Yaw");
            float Pitch = (float) ChengToolsReloaded.instance.getConfig().getDouble("SpawnSettings.Pitch");
            Location SpawnLcation = new Location(world, X, Y, Z, Yaw, Pitch);
            player.teleport(SpawnLcation);
            sender.sendMessage(getLang("Spawn.TeleportDone"));
        } else {
            sender.sendMessage(getLang("OnlyPlayer"));
        }
        return false;
    }
}
