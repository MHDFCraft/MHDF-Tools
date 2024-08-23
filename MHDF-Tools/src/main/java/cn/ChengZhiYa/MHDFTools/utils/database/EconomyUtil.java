package cn.ChengZhiYa.MHDFTools.utils.database;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.utils.database.DatabaseUtil.*;

public final class EconomyUtil {
    @Getter
    static final Map<Object, BigDecimal> MoneyHashMap = new HashMap<>();

    public static File getPlayerFile(String PlayerName) {
        return new File(PluginLoader.INSTANCE.getPlugin().getDataFolder() + "/VaultData", PlayerName + ".yml");
    }

    public static Boolean ifPlayerFileExists(String PlayerName) {
        if (Objects.equals(PluginLoader.INSTANCE.getPlugin().getConfig().getString("DataSettings.Type"), "MySQL")) {
            return dataExists("mhdftools_economy", "PlayerName", PlayerName);
        }
        return getPlayerFile(PlayerName).exists();
    }

    public static void initializationPlayerData(String PlayerName, Double money) {
        if (!ifPlayerFileExists(PlayerName)) {
            if (Objects.equals(PluginLoader.INSTANCE.getPlugin().getConfig().getString("DataSettings.Type"), "MySQL")) {
                Bukkit.getAsyncScheduler().runNow(PluginLoader.INSTANCE.getPlugin(), task -> {
                    try {
                        Connection connection = dataSource.getConnection();
                        PreparedStatement ps = connection.prepareStatement("INSERT INTO mhdftools_economy (PlayerName, Money) VALUES (?,?)");
                        ps.setString(1, PlayerName);
                        ps.setDouble(2, money);
                        ps.executeUpdate();
                        ps.close();
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            } else {
                File VaultFile = getPlayerFile(PlayerName);
                YamlConfiguration VaultData = YamlConfiguration.loadConfiguration(VaultFile);
                VaultData.set("money", PluginLoader.INSTANCE.getPlugin().getConfig().getDouble("EconomySettings.InitialMoney"));
                try {
                    VaultData.save(VaultFile);
                } catch (IOException ignored) {
                }
            }
        }
    }

    public static BigDecimal getBigDecimal(Double Money) {
        return BigDecimal.valueOf(Money).setScale(2, RoundingMode.HALF_UP);
    }

    public static double getMoney(String PlayerName) {
        if (Objects.equals(PluginLoader.INSTANCE.getPlugin().getConfig().getString("DataSettings.Type"), "MySQL")) {
            if (getMoneyHashMap().get(PlayerName) == null) {
                try {
                    BigDecimal bg = getBigDecimal((Double) getData("mhdftools_economy", "PlayerName", PlayerName, "Money"));
                    getMoneyHashMap().put(PlayerName, bg);
                    return bg.doubleValue();
                } catch (Exception e) {
                    getMoneyHashMap().put(PlayerName, BigDecimal.valueOf(0.0));
                    return 0.0;
                }
            } else {
                return getMoneyHashMap().get(PlayerName).doubleValue();
            }
        } else {
            YamlConfiguration Data = YamlConfiguration.loadConfiguration(getPlayerFile(PlayerName));
            return getBigDecimal(Data.getDouble("money")).doubleValue();
        }
    }

    public static void setMoney(String PlayerName, Double Money) {
        Bukkit.getAsyncScheduler().runNow(PluginLoader.INSTANCE.getPlugin(), task -> {
            if (Objects.equals(PluginLoader.INSTANCE.getPlugin().getConfig().getString("DataSettings.Type"), "MySQL")) {
                if (ifPlayerFileExists(PlayerName)) {
                    getMoneyHashMap().put(PlayerName, getBigDecimal(Money));
                    set("mhdftools_economy", "PlayerName", PlayerName, "Money", Money);
                }
            } else {
                YamlConfiguration Data = YamlConfiguration.loadConfiguration(getPlayerFile(PlayerName));
                Data.set("money", getBigDecimal(Money));
                try {
                    Data.save(getPlayerFile(PlayerName));
                } catch (IOException ignored) {
                }
            }
        });
    }

    public static void addMoney(String PlayerName, Double Money) {
        Bukkit.getAsyncScheduler().runNow(PluginLoader.INSTANCE.getPlugin(), task -> {
            if (Objects.equals(PluginLoader.INSTANCE.getPlugin().getConfig().getString("DataSettings.Type"), "MySQL")) {
                if (ifPlayerFileExists(PlayerName)) {
                    getMoneyHashMap().put(PlayerName, getMoneyHashMap().get(PlayerName).add(getBigDecimal(Money)));
                    add("mhdftools_economy", "PlayerName", PlayerName, "Money", Money);
                }
            } else {
                YamlConfiguration Data = YamlConfiguration.loadConfiguration(getPlayerFile(PlayerName));
                Data.set("money", getBigDecimal(Data.getDouble("money")).add(getBigDecimal(Money)));
                try {
                    Data.save(getPlayerFile(PlayerName));
                } catch (IOException ignored) {
                }
            }
        });
    }

    public static void takeMoney(String PlayerName, Double Money) {
        Bukkit.getAsyncScheduler().runNow(PluginLoader.INSTANCE.getPlugin(), task -> {
            if (Objects.equals(PluginLoader.INSTANCE.getPlugin().getConfig().getString("DataSettings.Type"), "MySQL")) {
                if (ifPlayerFileExists(PlayerName)) {
                    getMoneyHashMap().put(PlayerName, getMoneyHashMap().get(PlayerName).subtract(getBigDecimal(Money)));
                    take("mhdftools_economy", "PlayerName", PlayerName, "Money", Money);
                }
            } else {
                YamlConfiguration Data = YamlConfiguration.loadConfiguration(getPlayerFile(PlayerName));
                Data.set("money", getBigDecimal(Data.getDouble("money")).subtract(getBigDecimal(Money)));
                try {
                    Data.save(getPlayerFile(PlayerName));
                } catch (IOException ignored) {
                }
            }
        });
    }
}
