package cn.ChengZhiYa.ChengToolsReloaded.Commands.Tpa;

import cn.ChengZhiYa.ChengToolsReloaded.HashMap.BooleanHasMap;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.IntHasMap;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHasMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public final class TpaAccept implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull java.lang.String label, @NotNull java.lang.String[] args) {
        if (sender instanceof Player) {
            if (args.length == 1) {
                java.lang.String AcceptPlayerName = args[0];
                Player player = (Player) sender;
                if (StringHasMap.getHasMap().get(AcceptPlayerName + "_Temp_TpaPlayerName") == null || !Objects.equals(StringHasMap.getHasMap().get(AcceptPlayerName + "_Temp_TpaPlayerName"), player.getName())) {
                    sender.sendMessage(getLang("Tpa.NotSendTeleport"));
                    return false;
                }
                if (Bukkit.getPlayer(AcceptPlayerName) == null) {
                    StringHasMap.getHasMap().put(AcceptPlayerName + "_Temp_TpaPlayerName", null);
                    BooleanHasMap.getHasMap().put(AcceptPlayerName + "_Temp_Tpa", false);
                    IntHasMap.getHasMap().put(AcceptPlayerName + "_Temp_TpaTime", 0);
                    sender.sendMessage(getLang("Tpa.Offline"));
                    return false;
                }
                Player AcceptPlayer = Bukkit.getPlayer(AcceptPlayerName);
                Location SenderLocation = player.getLocation();
                Objects.requireNonNull(AcceptPlayer).teleport(SenderLocation);
                AcceptPlayer.sendMessage(getLang("Tpa.TeleportDone",AcceptPlayerName));
                player.sendMessage(getLang("Tpa.AcceptDone",AcceptPlayerName));
                StringHasMap.getHasMap().put(AcceptPlayerName + "_Temp_TpaPlayerName", null);
                BooleanHasMap.getHasMap().put(AcceptPlayerName + "_Temp_Tpa", false);
                IntHasMap.getHasMap().put(AcceptPlayerName + "_Temp_TpaTime", 0);
            } else {
                sender.sendMessage(getLang("Usage.Tpa"));
            }
        } else {
            sender.sendMessage(getLang("OnlyPlayer"));
        }
        return false;
    }
}
