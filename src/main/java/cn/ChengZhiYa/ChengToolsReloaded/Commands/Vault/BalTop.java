package cn.ChengZhiYa.ChengToolsReloaded.Commands.Vault;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import cn.ChengZhiYa.ChengToolsReloaded.Tasks.Money;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Util.ChatColor;
import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Util.getLang;

public final class BalTop implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] args) {
        StringBuilder BalTop = new StringBuilder(getLang("Vault.BalTopUp"));
        for (int i = 1; i < (ChengToolsReloaded.instance.getConfig().getInt("EconomySettings.BalTopInt") + 1); i++) {
            BalTop.append("\n").append(ChatColor(getLang("Vault.BalTop", String.valueOf(i), TopName(i), getNull(i), TopMoney(i))));
        }
        sender.sendMessage(String.valueOf(BalTop));
        return false;
    }

    public String[] getTop(int top) {
        try {
            Map.Entry<String, Double> entry = Money.getPlayer(top);
            if (entry == null) {
                return new String[]{"无", "无"};
            }
            return new String[]{String.format("%s", entry.getKey()), String.format("%.2f", entry.getValue())};
        } catch (Exception e) {
            return new String[]{"无", "无"};
        }
    }

    public String TopMoney(int top) {
        String ReturnString = "无";
        if (getTop(top)[1] != null) {
            ReturnString = getTop(top)[1];
        }
        return ReturnString;
    }

    public String TopName(int top) {
        String ReturnString = "无";
        if (getTop(top)[0] != null) {
            ReturnString = getTop(top)[0];
        }
        return ReturnString;
    }

    public String getNull(int top) {
        String Name = getTop(top)[0];
        if (Name == null) {
            return "";
        }
        if (Name.equals("无")) {
            return "                 ";
        }
        int NullTime = 19 - Name.length();
        if (NullTime <= 3) {
            return "   ";
        }
        StringBuilder NullString = new StringBuilder();
        for (int i = 0; i < NullTime; i++) {
            NullString.append(" ");
        }
        return NullString.toString();
    }
}
