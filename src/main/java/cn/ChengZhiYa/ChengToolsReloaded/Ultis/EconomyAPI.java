package cn.ChengZhiYa.ChengToolsReloaded.Ultis;

import cn.ChengZhiYa.ChengToolsReloaded.main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class EconomyAPI {

    public File getPlayerFile(String PlayerName) {
        return new File(main.main.getDataFolder() + "/VaultData", PlayerName + ".yml");
    }

    private YamlConfiguration loadConfig(File file) {
        return YamlConfiguration.loadConfiguration(file);
    }

    private Boolean playerFileExists(String PlayerName) {
        return getPlayerFile(PlayerName).exists();
    }

    public Double checkMoney(String PlayerName) {
        if (playerFileExists(PlayerName)) {
            YamlConfiguration config = loadConfig(getPlayerFile(PlayerName));
            return config.getDouble("money");
        }
        return -1.0;
    }

    public Boolean setMoney(String PlayerName, Double amount) {
        try {
            assert amount >= 0;
        } catch (AssertionError e) {
            return false;
        }
        if (!playerFileExists(PlayerName)) {
            return false;
        }
        YamlConfiguration config = loadConfig(getPlayerFile(PlayerName));
        config.set("money", amount);
        try {
            config.save(getPlayerFile(PlayerName));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public Boolean addTo(String PlayerName, Double amount) {
        try {
            assert amount >= 0;
        } catch (AssertionError e) {
            return false;
        }
        if (!playerFileExists(PlayerName)) {
            return false;
        }
        YamlConfiguration config = loadConfig(getPlayerFile(PlayerName));
        config.set("money", checkMoney(PlayerName) + amount);
        try {
            config.save(getPlayerFile(PlayerName));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public Boolean takeFrom(String PlayerName, Double amount) {
        try {
            assert amount >= 0;
        } catch (AssertionError e) {
            return false;
        }
        if (checkMoney(PlayerName) < amount) {
            return false;
        }
        YamlConfiguration config = loadConfig(getPlayerFile(PlayerName));
        config.set("money", checkMoney(PlayerName) - amount);
        try {
            config.save(getPlayerFile(PlayerName));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public Boolean transferMoney(String payer, String payee, Double amount) {
        try {
            assert amount >= 0;
        } catch (AssertionError e) {
            return false;
        }
        if (!(playerFileExists(payer) && playerFileExists(payee) && checkMoney(payer) >= amount)) {
            return false;
        }
        return takeFrom(payer, amount) && addTo(payee, amount);
    }
}
