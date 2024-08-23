package cn.ChengZhiYa.MHDFTools.command.subCommand.misc.freeze;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.i18n;

public class UnFreeze implements CommandExecutor, TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("MHDFTools.Command.UnFreeze")) {
            sender.sendMessage(i18n("NoPermission"));
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(i18n("Usage.UnFreeze"));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !Freeze.freezeUUID.contains(target.getUniqueId())) {
            sender.sendMessage(i18n("Usage.PlayerNotOnline"));
            return true;
        }

        Freeze.freezeUUID.remove(target.getUniqueId());
        sender.sendMessage(i18n("Freeze.UnFreezeDone", target.getName(), sender.getName()));

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (Freeze.freezeUUID.contains(player.getUniqueId())) {
                    suggestions.add(player.getName());
                }
            }
            return suggestions;
        }
        return null;
    }
}