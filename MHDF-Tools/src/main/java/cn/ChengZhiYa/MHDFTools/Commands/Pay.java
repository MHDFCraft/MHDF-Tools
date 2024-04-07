package cn.ChengZhiYa.MHDFTools.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.Utils.Database.EconomyUtil.*;
import static cn.ChengZhiYa.MHDFTools.Utils.Util.i18n;

public final class Pay implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] args) {
        if (args.length == 2) {
            if (Bukkit.getPlayer(args[0]) != null) {
                double amount = Double.parseDouble(args[1]);
                if (amount < 0) {
                    sender.sendMessage(i18n("Vault.MoneyLessThan0"));
                    return false;
                }
                if (getMoney(sender.getName()) < amount) {
                    sender.sendMessage(i18n("Vault.PayFail"));
                    return false;
                }
                takeMoney(sender.getName(), amount);
                addMoney(args[0], amount);
                Player receiver = Bukkit.getPlayer(args[0]);
                if (receiver == null) {
                    sender.sendMessage(i18n("PlayerNotOnline"));
                    return false;
                }
                if (Objects.requireNonNull(receiver.getAddress()).getHostString().equals(Objects.requireNonNull(((Player) sender).getAddress()).getHostString())) {
                    sender.sendMessage(i18n("Vault.PayFail"));
                    return false;
                }
                sender.sendMessage(i18n("Vault.PayDone", args[1], args[0]));
                receiver.sendMessage(i18n("Vault.Pay", sender.getName(), args[1]));
            }
            return false;
        }
        sender.sendMessage(i18n("Usage.Pay"));
        return false;
    }
}
