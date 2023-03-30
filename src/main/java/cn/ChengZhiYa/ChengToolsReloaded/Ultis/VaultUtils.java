package cn.ChengZhiYa.ChengToolsReloaded.Ultis;

import cn.ChengZhiYa.ChengToolsReloaded.main;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public class VaultUtils {
    public static boolean setMoney(CommandSender commandSender, String[] strings) {
        double amount;
        try {
            amount = Double.parseDouble(strings[1]);
        } catch (NumberFormatException e) {
            return false;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                if (amount < 0) {
                    commandSender.sendMessage(getLang("Vault.MoneyLessThan0"));
                    return;
                }
                if (!new EconomyAPI().setMoney(strings[0], amount)) {
                    return;
                }
                commandSender.sendMessage(getLang("Vault.SetDone",strings[0], String.valueOf(amount)));
            }
        }.runTaskAsynchronously(main.main);
        return true;
    }

    public static boolean giveMoney(CommandSender commandSender, String[] strings) {
        double amount;
        try {
            amount = Double.parseDouble(strings[1]);
        } catch (NumberFormatException e) {
            return false;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                if (amount < 0) {
                    commandSender.sendMessage(getLang("Vault.MoneyLessThan0"));
                    return;
                }
                if (!new EconomyAPI().addTo(strings[0], amount)) {
                    commandSender.sendMessage(getLang("Vault.PayFail"));
                    return;
                }
                commandSender.sendMessage(getLang("Vault.PayDone", strings[0],String.valueOf(amount)));
                for (Player players : Bukkit.getOnlinePlayers()) {
                    if (strings[0].equals(players.getName())) {
                        players.sendMessage(getLang("Vault.Pay", commandSender.getName(), String.valueOf(amount)));
                    }
                }
            }
        }.runTaskAsynchronously(main.main);
        return true;
    }

    public static boolean takeMoney(CommandSender commandSender, String[] strings) {
        double amount;
        try {
            amount = Double.parseDouble(strings[1]);
        } catch (NumberFormatException e) {
            return false;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                if (amount < 0) {
                    commandSender.sendMessage(getLang("Vault.MoneyLessThan0"));
                    return;
                }
                if (new EconomyAPI().takeFrom(strings[0], amount)) {
                    commandSender.sendMessage(String.format(getLang("Vault.TakeDone", strings[0], String.valueOf(amount))));
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        if (strings[0].equals(players.getName())) {
                            players.sendMessage(String.format(getLang("Vault.Take", String.valueOf(amount))));
                        }
                    }
                } else {
                    commandSender.sendMessage(getLang("Vault.TakeFail"));
                }
            }
        }.runTaskAsynchronously(main.main);
        return true;
    }
}
