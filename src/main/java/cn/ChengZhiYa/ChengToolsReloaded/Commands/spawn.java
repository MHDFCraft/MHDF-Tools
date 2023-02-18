package cn.ChengZhiYa.ChengToolsReloaded.Commands;

import cn.ChengZhiYa.ChengToolsReloaded.main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.ChatColor;

public class spawn implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            World world = Bukkit.getWorld(Objects.requireNonNull(main.main.getConfig().getString("SpawnSettings.World")));
            double X = main.main.getConfig().getDouble("SpawnSettings.X");
            double Y = main.main.getConfig().getDouble("SpawnSettings.Y");
            double Z = main.main.getConfig().getDouble("SpawnSettings.Z");
            float Yaw = (float) main.main.getConfig().getDouble("SpawnSettings.Yaw");
            float Pitch = (float) main.main.getConfig().getDouble("SpawnSettings.Pitch");
            Location SpawnLcation = new Location(world,X,Y,Z,Yaw,Pitch);
            player.teleport(SpawnLcation);
            sender.sendMessage(ChatColor("&a传送成功!"));
        }else {
            sender.sendMessage(ChatColor( "&c这个命令只有玩家才能执行"));
        }
        return false;
    }
}
