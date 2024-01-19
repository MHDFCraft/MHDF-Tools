package cn.ChengZhiYa.MHDFTools.Utils.Database;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static cn.ChengZhiYa.MHDFTools.MHDFTools.dataSource;

public final class DatabaseUtil {
    public static boolean DataExists(String Table, String Field, String Value) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + Table + " WHERE " + Field + " = ? LIMIT 1");
            ps.setString(1, Value);
            ResultSet rs = ps.executeQuery();
            boolean 结果 = rs.next();
            rs.close();
            ps.close();
            connection.close();
            return 结果;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Object GetData(String Table, String WhereField, String WhereValue, String GetField) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + Table + " WHERE " + WhereField + " = ? LIMIT 1");
            ps.setString(1, WhereValue);
            ResultSet rs = ps.executeQuery();
            Object Data = "";
            if (rs.next()) {
                Data = rs.getObject(GetField);
            }
            rs.close();
            ps.close();
            connection.close();
            return Data;
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void SetData(String Table, String WhereField, String WhereValue, String SetField, Object SetValue) {
        Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
            if (DataExists(Table, WhereField, WhereValue)) {
                try {
                    Connection connection = dataSource.getConnection();
                    PreparedStatement ps = connection.prepareStatement("UPDATE " + Table + " SET " + SetField + " = ? WHERE " + WhereField + " = ?");
                    ps.setObject(1, SetValue);
                    ps.setString(2, WhereValue);
                    ps.executeUpdate();
                    ps.close();
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void Add(String Table, String WhereField, String WhereValue, String AddField, Object AddValue) {
        Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
            if (DataExists(Table, WhereField, WhereValue)) {
                try {
                    Connection connection = dataSource.getConnection();
                    PreparedStatement ps = connection.prepareStatement("UPDATE " + Table + " SET " + AddField + " = " + AddField + "+? WHERE " + WhereField + " = ?");
                    ps.setObject(1, AddValue);
                    ps.setString(2, WhereValue);
                    ps.executeUpdate();
                    ps.close();
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void Take(String Table, String WhereField, String WhereValue, String TakeField, Object TakeValue) {
        Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
            if (DataExists(Table, WhereField, WhereValue)) {
                try {
                    Connection connection = dataSource.getConnection();
                    PreparedStatement ps = connection.prepareStatement("UPDATE " + Table + " SET " + TakeField + " = " + TakeField + "-? WHERE " + WhereField + " = ?");
                    ps.setObject(1, TakeValue);
                    ps.setString(2, WhereValue);
                    ps.executeUpdate();
                    ps.close();
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
