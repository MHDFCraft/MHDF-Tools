package cn.ChengZhiYa.MHDFTools.utils.databases;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
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

import static cn.ChengZhiYa.MHDFTools.MHDFTools.dataSource;
import static cn.ChengZhiYa.MHDFTools.utils.databases.DatabaseUtil.*;

public final class EconomyUtil {
    static final Map<Object, BigDecimal> MoneyHashMap = new HashMap<>();

    public static Map<Object, BigDecimal> getMoneyHashMap() {
        return MoneyHashMap;
    }

    public static File getPlayerFile(String PlayerName) {
        return new File(MHDFTools.instance.getDataFolder() + "/VaultData", PlayerName + ".yml");
    }

    public static Boolean ifPlayerFileExists(String PlayerName) {
        if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
            return DataExists("MHDFTools_Economy", "PlayerName", PlayerName);
        }
        return getPlayerFile(PlayerName).exists();
    }

    public static void initializationPlayerData(String PlayerName) {
        if (!ifPlayerFileExists(PlayerName)) {
            if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
                Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
                    try {
                        Connection connection = dataSource.getConnection();
                        PreparedStatement ps = connection.prepareStatement("INSERT INTO MHDFTools_Economy (PlayerName, Money) VALUES (?,?)");
                        ps.setString(1, PlayerName);
                        ps.setDouble(2, MHDFTools.instance.getConfig().getDouble("EconomySettings.InitialMoney"));
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
                VaultData.set("money", MHDFTools.instance.getConfig().getDouble("EconomySettings.InitialMoney"));
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
        if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
            if (getMoneyHashMap().get(PlayerName) == null) {
                try {
                    BigDecimal bg = getBigDecimal((Double) GetData("MHDFTools_Economy", "PlayerName", PlayerName, "Money"));
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
        Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
            if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
                if (ifPlayerFileExists(PlayerName)) {
                    getMoneyHashMap().put(PlayerName, getBigDecimal(Money));
                    Set("MHDFTools_Economy", "PlayerName", PlayerName, "Money", Money);
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
        Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
            if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
                if (ifPlayerFileExists(PlayerName)) {
                    getMoneyHashMap().put(PlayerName, getMoneyHashMap().get(PlayerName).add(getBigDecimal(Money)));
                    Add("MHDFTools_Economy", "PlayerName", PlayerName, "Money", Money);
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
        Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
            if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
                if (ifPlayerFileExists(PlayerName)) {
                    getMoneyHashMap().put(PlayerName, getMoneyHashMap().get(PlayerName).subtract(getBigDecimal(Money)));
                    Take("MHDFTools_Economy", "PlayerName", PlayerName, "Money", Money);
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
