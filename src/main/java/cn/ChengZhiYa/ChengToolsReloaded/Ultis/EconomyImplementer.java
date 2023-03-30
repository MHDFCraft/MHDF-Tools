package cn.ChengZhiYa.ChengToolsReloaded.Ultis;

import cn.ChengZhiYa.ChengToolsReloaded.main;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public class EconomyImplementer extends AbstractEconomy {

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "ChengTools";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 2;
    }

    @Override
    public String format(double amount) {
        return String.format("%.2f", amount);
    }

    @Override
    public String currencyNamePlural() {
        return currencyNameSingular();
    }

    @Override
    public String currencyNameSingular() {
        return ChatColor(main.main.getConfig().getString("EconomySettings.MoneyName"));
    }

    @Override
    public boolean hasAccount(String playerName) {
        return new File(main.main.getDataFolder() + "/VaultData", playerName + ".yml").exists();
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return hasAccount(player.getName());
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return false;
    }

    @Override
    public double getBalance(String playerName) {
        if (!hasAccount(playerName)) {
            return 0;
        }
        YamlConfiguration config = loadConfig(getPlayerFile(playerName));
        return config.getDouble("money");
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return getBalance(player.getName());
    }

    @Override
    public double getBalance(String playerName, String world) {
        return 0;
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return 0;
    }

    @Override
    public boolean has(String playerName, double amount) {
        return getBalance(playerName) >= amount;
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return has(player.getName(), amount);
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return false;
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return false;
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        if (!hasAccount(playerName)) {
            return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.FAILURE, null);
        }
        try {
            assert amount >= 0;
        } catch (AssertionError e) {
            return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.FAILURE, "数额异常");
        }
        if (getBalance(playerName) < amount) {
            return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.FAILURE, "金钱不足！");
        }
        YamlConfiguration config = loadConfig(getPlayerFile(playerName));
        config.set("money", getBalance(playerName) - amount);
        try {
            config.save(getPlayerFile(playerName));
            return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.SUCCESS, null);
        } catch (IOException e) {
            return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.FAILURE, "保存数据异常！");
        }
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        return withdrawPlayer(player.getName(), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        return new EconomyResponse(amount, getBalance(player.getName()), EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        if (!hasAccount(playerName)) {
            return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.FAILURE, null);
        }
        try {
            assert amount >= 0;
        } catch (AssertionError e) {
            return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.FAILURE, "数额异常");
        }
        YamlConfiguration config = loadConfig(getPlayerFile(playerName));
        config.set("money", getBalance(playerName) + amount);
        try {
            config.save(getPlayerFile(playerName));
            return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.SUCCESS, null);
        } catch (IOException e) {
            return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.FAILURE, "保存数据异常！");
        }
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        return depositPlayer(player.getName(), amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        return new EconomyResponse(amount, getBalance(player.getName()), EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse createBank(String name, String player) {
        double money = main.main.getConfig().getDouble("EconomySettings.InitialMoney");
        File file = getPlayerFile(player);
        if (!file.exists()) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.set("money", money);
            try {
                config.save(file);
            } catch (IOException e) {
                return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, null);
            }
        }
        return new EconomyResponse(0, getBalance(player), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        return createBank(name, player.getName());
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, null);
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return new EconomyResponse(amount, bankBalance(name).balance, EconomyResponse.ResponseType.FAILURE, null);
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, null);
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public List<String> getBanks() {
        return null;
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        double money = main.main.getConfig().getDouble("EconomySettings.InitialMoney");
        File file = getPlayerFile(playerName);
        if (!file.exists()) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.set("money", money);
            try {
                config.save(file);
            } catch (IOException e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        return createPlayerAccount(player.getName());
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return false;
    }

    private YamlConfiguration loadConfig(File file) {
        return YamlConfiguration.loadConfiguration(file);
    }

    private File getPlayerFile(String player_name) {
        return new File(main.main.getDataFolder() + "/VaultData", player_name + ".yml");
    }
}
