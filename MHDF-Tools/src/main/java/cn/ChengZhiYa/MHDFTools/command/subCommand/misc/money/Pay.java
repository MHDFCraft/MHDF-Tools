package cn.ChengZhiYa.MHDFTools.command.subCommand.misc.money;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.utils.Util.i18n;
import static cn.ChengZhiYa.MHDFTools.utils.database.EconomyUtil.*;

public final class Pay implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 2) {
            return handlePayment(sender, args);
        }
        sender.sendMessage(i18n("Usage.Pay"));
        return false;
    }

    private boolean handlePayment(CommandSender sender, String[] args) {
        String playerName = args[0];
        double amount;
        try {
            amount = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(i18n("InvalidAmount"));
            return false;
        }

        if (amount < 0) {
            sender.sendMessage(i18n("Vault.MoneyLessThan0"));
            return false;
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (getMoney(player.getName()) < amount) {
                sender.sendMessage(i18n("Vault.PayFail"));
                return false;
            }
            if (player.getName().equalsIgnoreCase(playerName)) {
                sender.sendMessage(i18n("Vault.PaySelf"));
                return false;
            }
            Player receiver = Bukkit.getPlayer(playerName);
            if (receiver == null || Objects.requireNonNull(receiver.getAddress()).getHostString().equals(Objects.requireNonNull(player.getAddress()).getHostString())) {
                sender.sendMessage(i18n("PlayerNotOnline"));
                return false;
            }

            takeMoney(player.getName(), amount);
            addMoney(playerName, amount);

            sender.sendMessage(i18n("Vault.PayDone", args[1], playerName));
            receiver.sendMessage(i18n("Vault.Pay", player.getName(), args[1]));

            return true;
        } else {
            sender.sendMessage(i18n("PlayerOnlyCommand"));
            return false;
        }
    }
}