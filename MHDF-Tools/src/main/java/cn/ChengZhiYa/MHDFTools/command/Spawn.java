package cn.ChengZhiYa.MHDFTools.command;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.util.BCUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.util.Util.i18n;

public final class Spawn implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            World world = Bukkit.getWorld(Objects.requireNonNull(MHDFTools.instance.getConfig().getString("SpawnSettings.World")));
            double X = MHDFTools.instance.getConfig().getDouble("SpawnSettings.X");
            double Y = MHDFTools.instance.getConfig().getDouble("SpawnSettings.Y");
            double Z = MHDFTools.instance.getConfig().getDouble("SpawnSettings.Z");
            float Yaw = (float) MHDFTools.instance.getConfig().getDouble("SpawnSettings.Yaw");
            float Pitch = (float) MHDFTools.instance.getConfig().getDouble("SpawnSettings.Pitch");
            Location spawnLocation = new Location(world, X, Y, Z, Yaw, Pitch);
            BCUtil.TpPlayerTo(player.getName(), MHDFTools.instance.getConfig().getString("SpawnSettings.Server"), spawnLocation);
            sender.sendMessage(i18n("Spawn.TeleportDone"));
        } else {
            sender.sendMessage(i18n("OnlyPlayer"));
        }
        return false;
    }
}
