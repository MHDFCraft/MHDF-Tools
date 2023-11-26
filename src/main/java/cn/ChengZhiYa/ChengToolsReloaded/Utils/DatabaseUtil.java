package cn.ChengZhiYa.ChengToolsReloaded.Utils;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded.dataSource;

public final class DatabaseUtil {
    public static boolean DataExists(String Table, String Field, String Value) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM ? WHERE ? = ? LIMIT 1");
            ps.setString(1, Table);
            ps.setString(2, Field);
            ps.setString(3, Value);
            ResultSet rs = ps.executeQuery();
            boolean 结果 = rs.next();
            rs.close();
            ps.close();
            connection.close();
            return 结果;
        } catch (SQLException e) {
            return false;
        }
    }

    public static Object GetData(String Table, String WhereField, String WhereValue, String GetField) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM ? WHERE ? = ? LIMIT 1");
            ps.setString(1, Table);
            ps.setString(2, WhereField);
            ps.setString(3, WhereValue);
            ResultSet rs = ps.executeQuery();
            Object Data = "";
            if (rs.next()) {
                Data = rs.getDouble(GetField);
            }
            rs.close();
            ps.close();
            connection.close();
            return Data;
        } catch (SQLException e) {
            return "";
        }
    }

    public static void SetData(String Table, String WhereField, String WhereValue, String SetField, Object SetValue) {
        Bukkit.getScheduler().runTaskAsynchronously(ChengToolsReloaded.instance, () -> {
            if (DataExists(Table, WhereField, WhereValue)) {
                try {
                    Connection connection = dataSource.getConnection();
                    PreparedStatement ps = connection.prepareStatement("UPDATE ? SET ? = ? WHERE ? = ?");
                    ps.setString(1, Table);
                    ps.setString(2, SetField);
                    ps.setObject(3, SetValue);
                    ps.setString(4, WhereField);
                    ps.setString(5, WhereValue);
                    ps.executeUpdate();
                    ps.close();
                    connection.close();
                } catch (SQLException ignored) {}
            }
        });
    }

    public static void Add(String Table, String WhereField, String WhereValue, String AddField, Object AddValue) {
        Bukkit.getScheduler().runTaskAsynchronously(ChengToolsReloaded.instance, () -> {
            if (DataExists(Table, WhereField, WhereValue)) {
                try {
                    Connection connection = dataSource.getConnection();
                    PreparedStatement ps = connection.prepareStatement("UPDATE ? SET ? = ?+? WHERE ? = ?");
                    ps.setString(1, Table);
                    ps.setString(2, AddField);
                    ps.setString(3, AddField);
                    ps.setObject(4, AddValue);
                    ps.setString(5, WhereField);
                    ps.setString(6, WhereValue);
                    ps.executeUpdate();
                    ps.close();
                    connection.close();
                } catch (SQLException ignored) {
                }
            }
        });
    }

    public static void Take(String Table, String WhereField, String WhereValue, String TakeField, Object TakeValue) {
        Bukkit.getScheduler().runTaskAsynchronously(ChengToolsReloaded.instance, () -> {
            if (DataExists(Table, WhereField, WhereValue)) {
                try {
                    Connection connection = dataSource.getConnection();
                    PreparedStatement ps = connection.prepareStatement("UPDATE ? SET ? = ?-? WHERE ? = ?");
                    ps.setString(1, Table);
                    ps.setString(2, TakeField);
                    ps.setString(3, TakeField);
                    ps.setObject(4, TakeValue);
                    ps.setString(5, WhereField);
                    ps.setString(6, WhereValue);
                    ps.executeUpdate();
                    ps.close();
                    connection.close();
                } catch (SQLException ignored) {
                }
            }
        });
    }
}
