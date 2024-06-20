package cn.ChengZhiYa.MHDFTools.command.subCommand.misc.money;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.i18n;
import static cn.ChengZhiYa.MHDFTools.utils.database.EconomyUtil.*;

public final class MoneyAdmin implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] args) {
        if (args.length == 3) {
            switch (args[0]) {
                case "add":
                case "take":
                case "set":
                    if (Bukkit.getPlayer(args[1]) == null) {
                        sender.sendMessage(i18n("PlayerNotOnline"));
                        return false;
                    }
                    double amount;
                    try {
                        amount = Double.parseDouble(args[2]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(i18n("InvalidAmount"));
                        return false;
                    }
                    switch (args[0]) {
                        case "add":
                            addMoney(args[1], amount);
                            sender.sendMessage(i18n("Vault.AddDone", args[1], String.valueOf(amount)));
                            break;
                        case "take":
                            takeMoney(args[1], amount);
                            sender.sendMessage(i18n("Vault.TakeDone", args[1], String.valueOf(amount)));
                            break;
                        case "set":
                            setMoney(args[1], amount);
                            sender.sendMessage(i18n("Vault.SetDone", args[1], String.valueOf(amount)));
                            break;
                    }
                    break;
                default:
                    Help(sender, s);
                    break;
            }
        } else {
            Help(sender, s);
        }
        return false;
    }

    public void Help(CommandSender sender, String command) {
        sender.sendMessage(i18n("Vault.AdminHelpMessage", command));
    }

    @Override
    public @Nullable List<String> onTabComplete(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender.hasPermission("MHDFTools.MoneyAdmin")) {
            List<String> tabList = new ArrayList<>();
            if (args.length == 1) {
                tabList.add("help");
                tabList.add("add");
                tabList.add("take");
                tabList.add("set");
                return tabList;
            }
        }
        return null;
    }
}