package cn.ChengZhiYa.MHDFTools.command.subcommand.misc.spawn;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.entity.SuperLocation;
import cn.ChengZhiYa.MHDFTools.utils.BungeeCordUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.i18n;

public final class Spawn implements CommandExecutor {

    private final PluginLoader pluginLoader = PluginLoader.INSTANCE;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(i18n("OnlyPlayer"));
            return false;
        }

        Player player = (Player) sender;
        var config = pluginLoader.getPlugin().getConfig();

        String worldName = config.getString("SpawnSettings.World");
        double spawnX = config.getDouble("SpawnSettings.X");
        double spawnY = config.getDouble("SpawnSettings.Y");
        double spawnZ = config.getDouble("SpawnSettings.Z");
        float spawnYaw = (float) config.getDouble("SpawnSettings.Yaw");
        float spawnPitch = (float) config.getDouble("SpawnSettings.Pitch");
        SuperLocation spawnLocation = new SuperLocation(worldName, spawnX, spawnY, spawnZ, spawnYaw, spawnPitch);

        BungeeCordUtil.tpPlayerTo(player.getName(), config.getString("SpawnSettings.Server"), spawnLocation);
        sender.sendMessage(i18n("Spawn.TeleportDone"));

        return true;
    }
}