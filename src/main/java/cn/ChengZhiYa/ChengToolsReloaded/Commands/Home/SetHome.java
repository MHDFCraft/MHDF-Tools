package cn.ChengZhiYa.ChengToolsReloaded.Commands.Home;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Database.HomeUtil.AddHome;
import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Database.HomeUtil.getPlayerHomeTime;
import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Util.getLang;

public final class SetHome implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 1) {
                Player player = (Player) sender;
                String HomeName = args[0];
                if (ChengToolsReloaded.instance.getConfig().getInt("HomeSystemSettings.MaxHomeTime") <= getPlayerHomeTime(player.getName())) {
                    sender.sendMessage(getLang("Home.HomeListFull", String.valueOf(ChengToolsReloaded.instance.getConfig().getInt("HomeSystemSettings.MaxHomeTime"))));
                    return false;
                }
                AddHome(player.getName(),HomeName,player.getLocation());
                sender.sendMessage(getLang("Home.SetDone", label));
            } else {
                sender.sendMessage(getLang("Usage.Home", label));
            }
        } else {
            sender.sendMessage(getLang("OnlyPlayer"));
        }
        return false;
    }
}
