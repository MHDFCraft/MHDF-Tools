package cn.ChengZhiYa.ChengToolsReloaded.Commands.Vault;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Utils.EconomyUtil.getMoney;
import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Util.getLang;

public final class Money implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                sender.sendMessage(getLang("Vault.Money", String.valueOf(getMoney(sender.getName())), ChengToolsReloaded.instance.getConfig().getString("EconomySettings.MoneyName")));
                return false;
            }
        }

        if (args.length == 1) {
            sender.sendMessage(getLang("Vault.GetMoney", Objects.requireNonNull(Bukkit.getPlayer(args[0])).getName(), String.valueOf(getMoney(sender.getName())), ChengToolsReloaded.instance.getConfig().getString("EconomySettings.MoneyName")));
            return false;
        }
        sender.sendMessage(getLang("Usage.Money"));
        return false;
    }
}
