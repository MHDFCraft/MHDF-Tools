package cn.ChengZhiYa.MHDFTools.hooks;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.utils.message.MessageUtil;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
import org.bukkit.OfflinePlayer;

import java.io.File;
import java.util.List;

import static cn.ChengZhiYa.MHDFTools.utils.database.EconomyUtil.*;

public final class Economy extends AbstractEconomy {
    public boolean isEnabled() {
        return true;
    }

    public String getName() {
        return "MHDFTools";
    }

    public boolean hasBankSupport() {
        return false;
    }

    public int fractionalDigits() {
        return 2;
    }

    public String format(double amount) {
        return String.format("%.2f", amount);
    }

    public String currencyNamePlural() {
        return this.currencyNameSingular();
    }

    public String currencyNameSingular() {
        return MessageUtil.colorMessage(MHDFTools.instance.getConfig().getString("EconomySettings.MoneyName"));
    }

    public boolean hasAccount(String playerName) {
        return (new File(MHDFTools.instance.getDataFolder() + "/VaultData", playerName + ".yml")).exists();
    }

    public boolean hasAccount(OfflinePlayer player) {
        return this.hasAccount(player.getName());
    }

    public boolean hasAccount(String playerName, String worldName) {
        return false;
    }

    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return false;
    }

    public double getBalance(String playerName) {
        if (ifPlayerFileExists(playerName)) {
            return getMoney(playerName);
        }
        return 0.0D;
    }

    public double getBalance(OfflinePlayer player) {
        return this.getBalance(player.getName());
    }

    public double getBalance(String playerName, String world) {
        return 0.0D;
    }

    public double getBalance(OfflinePlayer player, String world) {
        return 0.0D;
    }

    public boolean has(String playerName, double amount) {
        return this.getBalance(playerName) >= amount;
    }

    public boolean has(OfflinePlayer player, double amount) {
        return this.has(player.getName(), amount);
    }

    public boolean has(String playerName, String worldName, double amount) {
        return false;
    }

    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return false;
    }

    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        if (ifPlayerFileExists(playerName)) {
            takeMoney(playerName, amount);
            return new EconomyResponse(amount, this.getBalance(playerName), ResponseType.SUCCESS, null);
        }
        return new EconomyResponse(amount, this.getBalance(playerName), ResponseType.FAILURE, null);
    }

    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        return this.withdrawPlayer(player.getName(), amount);
    }

    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return new EconomyResponse(amount, this.getBalance(playerName), ResponseType.NOT_IMPLEMENTED, null);
    }

    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        return new EconomyResponse(amount, this.getBalance(player.getName()), ResponseType.NOT_IMPLEMENTED, null);
    }

    public EconomyResponse depositPlayer(String playerName, double amount) {
        if (ifPlayerFileExists(playerName)) {
            addMoney(playerName, amount);
            return new EconomyResponse(amount, this.getBalance(playerName), ResponseType.SUCCESS, null);
        }
        return new EconomyResponse(amount, this.getBalance(playerName), ResponseType.FAILURE, null);
    }

    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        return this.depositPlayer(player.getName(), amount);
    }

    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return new EconomyResponse(amount, this.getBalance(playerName), ResponseType.NOT_IMPLEMENTED, null);
    }

    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        return new EconomyResponse(amount, this.getBalance(player.getName()), ResponseType.NOT_IMPLEMENTED, null);
    }

    public EconomyResponse createBank(String name, String player) {
        return new EconomyResponse(0.0D, this.getBalance(player), ResponseType.SUCCESS, null);
    }

    public EconomyResponse createBank(String name, OfflinePlayer player) {
        return this.createBank(name, player.getName());
    }

    public EconomyResponse deleteBank(String name) {
        return new EconomyResponse(0.0D, 0.0D, ResponseType.NOT_IMPLEMENTED, null);
    }

    public EconomyResponse bankBalance(String name) {
        return new EconomyResponse(0.0D, 0.0D, ResponseType.FAILURE, null);
    }

    public EconomyResponse bankHas(String name, double amount) {
        return new EconomyResponse(amount, this.bankBalance(name).balance, ResponseType.FAILURE, null);
    }

    public EconomyResponse bankWithdraw(String name, double amount) {
        return null;
    }

    public EconomyResponse bankDeposit(String name, double amount) {
        return new EconomyResponse(0.0D, 0.0D, ResponseType.FAILURE, null);
    }

    public EconomyResponse isBankOwner(String name, String playerName) {
        return new EconomyResponse(0.0D, 0.0D, ResponseType.NOT_IMPLEMENTED, null);
    }

    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return new EconomyResponse(0.0D, 0.0D, ResponseType.NOT_IMPLEMENTED, null);
    }

    public EconomyResponse isBankMember(String name, String playerName) {
        return new EconomyResponse(0.0D, 0.0D, ResponseType.NOT_IMPLEMENTED, null);
    }

    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return new EconomyResponse(0.0D, 0.0D, ResponseType.NOT_IMPLEMENTED, null);
    }

    public List<String> getBanks() {
        return null;
    }

    public boolean createPlayerAccount(String playerName) {
        return true;
    }

    public boolean createPlayerAccount(OfflinePlayer player) {
        return this.createPlayerAccount(player.getName());
    }

    public boolean createPlayerAccount(String playerName, String worldName) {
        return false;
    }

    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return false;
    }
}
    