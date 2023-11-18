package cn.ChengZhiYa.ChengToolsReloaded.Commands.Vault;

import cn.ChengZhiYa.ChengToolsReloaded.Utils.EconomyAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Util.getLang;

public final class Pay implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] args) {
        if (args.length == 2) {
            if (Bukkit.getPlayer(args[0]) != null) {
                double amount = Double.parseDouble(args[1]);
                if (amount < 0) {
                    sender.sendMessage(getLang("Vault.MoneyLessThan0"));
                    return false;
                }
                if (!new EconomyAPI().transferMoney(sender.getName(), args[0], amount)) {
                    sender.sendMessage(getLang("Vault.PayFail"));
                    return false;
                }
                Player receiver = Bukkit.getPlayer(args[0]);
                if (receiver == null) {
                    sender.sendMessage(getLang("PlayerNotOnline"));
                    return false;
                }
                if (Objects.requireNonNull(receiver.getAddress()).getHostString().equals(Objects.requireNonNull(((Player) sender).getAddress()).getHostString())) {
                    sender.sendMessage(getLang("Vault.PayFail"));
                    return false;
                }
                sender.sendMessage(getLang("Vault.PayDone", args[1], args[0]));
                receiver.sendMessage(getLang("Vault.Pay", sender.getName(), args[1]));
            }
            return false;
        }
        sender.sendMessage(getLang("Usage.Pay"));
        return false;
    }
}
