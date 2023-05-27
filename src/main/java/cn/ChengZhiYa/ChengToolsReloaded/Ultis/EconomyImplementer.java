package cn.ChengZhiYa.ChengToolsReloaded.Ultis;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public final class EconomyImplementer extends AbstractEconomy {
    public boolean isEnabled() {
        return true;
    }

    public String getName() {
        return "ChengTools";
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
        return multi.ChatColor(ChengToolsReloaded.instance.getConfig().getString("EconomySettings.MoneyName"));
    }

    public boolean hasAccount(String playerName) {
        return (new File(ChengToolsReloaded.instance.getDataFolder() + "/VaultData", playerName + ".yml")).exists();
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
        if (!this.hasAccount(playerName)) {
            return 0.0D;
        } else {
            YamlConfiguration config = this.loadConfig(this.getPlayerFile(playerName));
            return config.getDouble("money");
        }
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
        if (!this.hasAccount(playerName)) {
            return new EconomyResponse(amount, this.getBalance(playerName), ResponseType.FAILURE, (String) null);
        } else {
            try {
                assert amount >= 0.0D;
            } catch (AssertionError var7) {
                return new EconomyResponse(amount, this.getBalance(playerName), ResponseType.FAILURE, "数额异常");
            }

            if (this.getBalance(playerName) < amount) {
                return new EconomyResponse(amount, this.getBalance(playerName), ResponseType.FAILURE, "金钱不足！");
            } else {
                YamlConfiguration config = this.loadConfig(this.getPlayerFile(playerName));
                config.set("money", this.getBalance(playerName) - amount);

                try {
                    config.save(this.getPlayerFile(playerName));
                    return new EconomyResponse(amount, this.getBalance(playerName), ResponseType.SUCCESS, (String) null);
                } catch (IOException var6) {
                    return new EconomyResponse(amount, this.getBalance(playerName), ResponseType.FAILURE, "保存数据异常！");
                }
            }
        }
    }

    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        return this.withdrawPlayer(player.getName(), amount);
    }

    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return new EconomyResponse(amount, this.getBalance(playerName), ResponseType.NOT_IMPLEMENTED, (String) null);
    }

    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        return new EconomyResponse(amount, this.getBalance(player.getName()), ResponseType.NOT_IMPLEMENTED, (String) null);
    }

    public EconomyResponse depositPlayer(String playerName, double amount) {
        if (!this.hasAccount(playerName)) {
            return new EconomyResponse(amount, this.getBalance(playerName), ResponseType.FAILURE, (String) null);
        } else {
            try {
                assert amount >= 0.0D;
            } catch (AssertionError var7) {
                return new EconomyResponse(amount, this.getBalance(playerName), ResponseType.FAILURE, "数额异常");
            }

            YamlConfiguration config = this.loadConfig(this.getPlayerFile(playerName));
            config.set("money", this.getBalance(playerName) + amount);

            try {
                config.save(this.getPlayerFile(playerName));
                return new EconomyResponse(amount, this.getBalance(playerName), ResponseType.SUCCESS, (String) null);
            } catch (IOException var6) {
                return new EconomyResponse(amount, this.getBalance(playerName), ResponseType.FAILURE, "保存数据异常！");
            }
        }
    }

    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        return this.depositPlayer(player.getName(), amount);
    }

    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return new EconomyResponse(amount, this.getBalance(playerName), ResponseType.NOT_IMPLEMENTED, (String) null);
    }

    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        return new EconomyResponse(amount, this.getBalance(player.getName()), ResponseType.NOT_IMPLEMENTED, (String) null);
    }

    public EconomyResponse createBank(String name, String player) {
        double money = ChengToolsReloaded.instance.getConfig().getDouble("EconomySettings.InitialMoney");
        File file = this.getPlayerFile(player);
        if (!file.exists()) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.set("money", money);

            try {
                config.save(file);
            } catch (IOException var8) {
                return new EconomyResponse(0.0D, 0.0D, ResponseType.FAILURE, (String) null);
            }
        }

        return new EconomyResponse(0.0D, this.getBalance(player), ResponseType.SUCCESS, (String) null);
    }

    public EconomyResponse createBank(String name, OfflinePlayer player) {
        return this.createBank(name, player.getName());
    }

    public EconomyResponse deleteBank(String name) {
        return new EconomyResponse(0.0D, 0.0D, ResponseType.NOT_IMPLEMENTED, (String) null);
    }

    public EconomyResponse bankBalance(String name) {
        return new EconomyResponse(0.0D, 0.0D, ResponseType.FAILURE, (String) null);
    }

    public EconomyResponse bankHas(String name, double amount) {
        return new EconomyResponse(amount, this.bankBalance(name).balance, ResponseType.FAILURE, (String) null);
    }

    public EconomyResponse bankWithdraw(String name, double amount) {
        return null;
    }

    public EconomyResponse bankDeposit(String name, double amount) {
        return new EconomyResponse(0.0D, 0.0D, ResponseType.FAILURE, (String) null);
    }

    public EconomyResponse isBankOwner(String name, String playerName) {
        return new EconomyResponse(0.0D, 0.0D, ResponseType.NOT_IMPLEMENTED, (String) null);
    }

    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return new EconomyResponse(0.0D, 0.0D, ResponseType.NOT_IMPLEMENTED, (String) null);
    }

    public EconomyResponse isBankMember(String name, String playerName) {
        return new EconomyResponse(0.0D, 0.0D, ResponseType.NOT_IMPLEMENTED, (String) null);
    }

    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return new EconomyResponse(0.0D, 0.0D, ResponseType.NOT_IMPLEMENTED, (String) null);
    }

    public List<String> getBanks() {
        return null;
    }

    public boolean createPlayerAccount(String playerName) {
        double money = ChengToolsReloaded.instance.getConfig().getDouble("EconomySettings.InitialMoney");
        File file = this.getPlayerFile(playerName);
        if (!file.exists()) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.set("money", money);

            try {
                config.save(file);
            } catch (IOException var7) {
                return false;
            }
        }

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

    private YamlConfiguration loadConfig(File file) {
        return YamlConfiguration.loadConfiguration(file);
    }

    private File getPlayerFile(String player_name) {
        return new File(ChengToolsReloaded.instance.getDataFolder() + "/VaultData", player_name + ".yml");
    }
}
    