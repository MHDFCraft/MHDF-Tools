package cn.ChengZhiYa.ChengToolsReloaded.Commands;

import cn.ChengZhiYa.ChengToolsReloaded.HashMap.BooleanHashMap;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.IntHashMap;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.ChatColor;

public class TpaHereAccept implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            if (args.length == 1) {
                String AcceptPlayerName = args[0];
                Player player = (Player) sender;
                if (StringHashMap.Get(AcceptPlayerName + "_Temp_TpaherePlayerName") == null || !Objects.equals(StringHashMap.Get(AcceptPlayerName + "_Temp_TpaherePlayerName"), player.getName())) {
                    sender.sendMessage(ChatColor("&c这个玩家没有给你发传送请求"));
                    return false;
                }
                if (Bukkit.getPlayer(AcceptPlayerName) == null) {
                    StringHashMap.Set(AcceptPlayerName + "_Temp_TpaHerePlayerName", null);
                    BooleanHashMap.Set(AcceptPlayerName + "_Temp_TpaHere", false);
                    IntHashMap.Set(AcceptPlayerName + "_Temp_TpaHereTime", 0);
                    sender.sendMessage(ChatColor("&c错误,那个玩家突然下线了!传送已取消"));
                    return false;
                }
                Player AcceptPlayer = Bukkit.getPlayer(AcceptPlayerName);
                Location AcceptPlayerLocation = Objects.requireNonNull(AcceptPlayer).getLocation();
                player.teleport(AcceptPlayerLocation);
                AcceptPlayer.sendMessage(ChatColor("&a传送成功!"));
                player.sendMessage(ChatColor("&a你传送到了" + player.getName() + "那里!"));
                StringHashMap.Set(AcceptPlayerName + "_Temp_TpaPlayerName", null);
                BooleanHashMap.Set(AcceptPlayerName + "_Temp_Tpa", false);
                IntHashMap.Set(AcceptPlayerName + "_Temp_TpaTime", 0);
            }else {
                sender.sendMessage(ChatColor("&c用法错误,用法:/tpahere <玩家ID>"));
            }
        }else {
            sender.sendMessage(ChatColor( "&c这个命令只有玩家才能执行"));
        }
        return false;
    }
}
