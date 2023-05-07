package cn.ChengZhiYa.ChengToolsReloaded.Commands.TpaHere;

import cn.ChengZhiYa.ChengToolsReloaded.HashMap.BooleanHasMap;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.IntHasMap;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHasMap;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public final class TpaHereDefuse implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull java.lang.String label, @NotNull java.lang.String[] args) {
        if (sender instanceof Player) {
            if (args.length == 1) {
                java.lang.String AcceptPlayerName = args[0];
                Player player = (Player) sender;
                if (StringHasMap.getHasMap().get(AcceptPlayerName + "_Temp_TpaherePlayerName") == null || !Objects.equals(StringHasMap.getHasMap().get(AcceptPlayerName + "_Temp_TpaherePlayerName"), player.getName())) {
                    sender.sendMessage(getLang("TpaHere.NotSendTeleport"));
                    return false;
                }
                if (Bukkit.getPlayer(AcceptPlayerName) == null) {
                    StringHasMap.getHasMap().put(AcceptPlayerName + "_Temp_TpaHerePlayerName", null);
                    BooleanHasMap.getHasMap().put(AcceptPlayerName + "_Temp_TpaHere", false);
                    IntHasMap.getHasMap().put(AcceptPlayerName + "_Temp_TpaHereTime", 0);
                    sender.sendMessage(getLang("TpaHere.Offline"));
                    return false;
                }
                Player AcceptPlayer = Bukkit.getPlayer(AcceptPlayerName);
                Objects.requireNonNull(AcceptPlayer).sendMessage(getLang("TpaHere.Defuse"));
                player.sendMessage(getLang("TpaHere.DefuseDone"));
                StringHasMap.getHasMap().put(AcceptPlayerName + "_Temp_TpaherePlayerName", null);
                BooleanHasMap.getHasMap().put(AcceptPlayerName + "_Temp_Tpahere", false);
                IntHasMap.getHasMap().put(AcceptPlayerName + "_Temp_TpaTimehere", 0);
            } else {
                sender.sendMessage(getLang("Usage.TpaHere"));
            }
        } else {
            sender.sendMessage(getLang("OnlyPlayer"));
        }
        return false;
    }
}
