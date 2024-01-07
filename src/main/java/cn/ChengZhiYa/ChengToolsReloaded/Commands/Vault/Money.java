package cn.ChengZhiYa.ChengToolsReloaded.Commands.Vault;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Database.EconomyUtil.getMoney;
import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Util.i18n;

public final class Money implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                sender.sendMessage(i18n("Vault.Money", String.valueOf(getMoney(sender.getName())), ChengToolsReloaded.instance.getConfig().getString("EconomySettings.MoneyName")));
                return false;
            }
        }

        if (args.length == 1) {
            sender.sendMessage(i18n("Vault.GetMoney", Objects.requireNonNull(Bukkit.getPlayer(args[0])).getName(), String.valueOf(getMoney(sender.getName())), ChengToolsReloaded.instance.getConfig().getString("EconomySettings.MoneyName")));
            return false;
        }
        sender.sendMessage(i18n("Usage.Money"));
        return false;
    }
}
