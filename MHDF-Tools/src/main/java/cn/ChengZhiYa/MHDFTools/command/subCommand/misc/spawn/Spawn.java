package cn.ChengZhiYa.MHDFTools.command.subCommand.misc.spawn;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.entity.SuperLocation;
import cn.ChengZhiYa.MHDFTools.utils.BungeeCordUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.i18n;

public final class Spawn implements CommandExecutor {

    static String WORLD_CONFIG_KEY = "SpawnSettings.World";
    static String X_CONFIG_KEY = "SpawnSettings.X";
    static String Y_CONFIG_KEY = "SpawnSettings.Y";
    static String Z_CONFIG_KEY = "SpawnSettings.Z";
    static String YAW_CONFIG_KEY = "SpawnSettings.Yaw";
    static String PITCH_CONFIG_KEY = "SpawnSettings.Pitch";
    static String SERVER_CONFIG_KEY = "SpawnSettings.Server";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(i18n("OnlyPlayer"));
            return false;
        }

        Player player = (Player) sender;
        String worldName = MHDFTools.instance.getConfig().getString(WORLD_CONFIG_KEY);
        double spawnX = MHDFTools.instance.getConfig().getDouble(X_CONFIG_KEY);
        double spawnY = MHDFTools.instance.getConfig().getDouble(Y_CONFIG_KEY);
        double spawnZ = MHDFTools.instance.getConfig().getDouble(Z_CONFIG_KEY);
        float spawnYaw = (float) MHDFTools.instance.getConfig().getDouble(YAW_CONFIG_KEY);
        float spawnPitch = (float) MHDFTools.instance.getConfig().getDouble(PITCH_CONFIG_KEY);
        SuperLocation spawnLocation = new SuperLocation(worldName, spawnX, spawnY, spawnZ, spawnYaw, spawnPitch);

        BungeeCordUtil.TpPlayerTo(player.getName(), MHDFTools.instance.getConfig().getString(SERVER_CONFIG_KEY), spawnLocation);
        sender.sendMessage(i18n("Spawn.TeleportDone"));

        return true;
    }
}