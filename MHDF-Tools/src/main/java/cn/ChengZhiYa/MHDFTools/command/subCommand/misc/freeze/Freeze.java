package cn.ChengZhiYa.MHDFTools.command.subCommand.misc.freeze;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.i18n;
import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.ifLogin;

public class Freeze implements CommandExecutor {
    public static final List<UUID> freezeUUID = new ArrayList<>();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("MHDFTools.Command.Freeze")) {
            sender.sendMessage(i18n("NoPermission"));
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(i18n("Usage.Freeze"));
            return true;
        }

        Player senderPlayer = (Player) sender;
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(i18n("Usage.PlayerNotOnline"));
            return true;
        }

        if (!ifLogin(target)) {
            sender.sendMessage(i18n("Login.Logging", target.getName()));
            return true;
        }

        if (target.equals(senderPlayer)) {
            sender.sendMessage(i18n("dontSender"));
            return true;
        }

        if (target.hasPermission(PluginLoader.INSTANCE.getPlugin().getConfig().getString("FreezeSettings.BypassPermission", "MHDFTools.Bypass.Freeze"))) {
            sender.sendMessage(i18n("Freeze.TargetBypass", target.getName()));
            return true;
        }


        if (freezeUUID.contains(target.getUniqueId())) {
            sender.sendMessage(i18n("Freeze.AlreadyFreeze", sender.getName()));
            return true;
        }

        freezeUUID.add(target.getUniqueId());
        target.sendMessage(i18n("Freeze.PlayerGotFroze", senderPlayer.getName()));
        sender.sendMessage(i18n("Freeze.FreezeDone", target.getName(), senderPlayer.getName()));
        return true;
    }
}