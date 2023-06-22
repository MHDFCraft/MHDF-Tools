package cn.ChengZhiYa.ChengToolsReloaded.Commands.Vault;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import cn.ChengZhiYa.ChengToolsReloaded.Ultis.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public final class Money implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                sender.sendMessage(getLang("Vault.Money", String.valueOf(new EconomyAPI().checkMoney(sender.getName())), ChengToolsReloaded.instance.getConfig().getString("EconomySettings.MoneyName")));
                return false;
            }
        }

        if (Bukkit.getPlayer(args[0]) != null) {
            sender.sendMessage(getLang("Vault.GetMoney", Objects.requireNonNull(Bukkit.getPlayer(args[0])).getName(), String.valueOf(new EconomyAPI().checkMoney(sender.getName())), ChengToolsReloaded.instance.getConfig().getString("EconomySettings.MoneyName")));
            return false;
        } else {
            sender.sendMessage(getLang("PlayerNotOnline"));
        }
        sender.sendMessage(getLang("Usage.Money"));
        return false;
    }
}
