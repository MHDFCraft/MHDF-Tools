package cn.ChengZhiYa.ChengToolsReloaded.Commands.Other;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import cn.ChengZhiYa.ChengToolsReloaded.Ultis.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public final class Point implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        File Point_File = new File(ChengToolsReloaded.instance.getDataFolder(), "PointData.yml");
        YamlConfiguration PointData = YamlConfiguration.loadConfiguration(Point_File);
        if (sender.hasPermission("ChengTools.Point")) {
            if (args.length == 2) {
                String PlayerName = args[1];
                if (args[0].equals("get")) {
                    sender.sendMessage(getLang("Point.GetHavePoint", PlayerName, String.valueOf(PointData.getInt(PlayerName + "_Point"))));
                    return false;
                }
                if (args[0].equals("clear")) {
                    PointData.set(PlayerName + "_Point", null);
                    try {
                        PointData.save(Point_File);
                    } catch (IOException ignored) {
                    }
                    sender.sendMessage(getLang("Point.ClearPoint", PlayerName));
                    return false;
                }
            }
            if (args.length == 3) {
                String PlayerName = args[1];
                int Point = Integer.parseInt(args[2]);
                int PlayerPoint = PointData.getInt(PlayerName + "_Point");
                if (args[0].equals("add")) {
                    PointData.set(PlayerName + "_Point", PlayerPoint + Point);
                    try {
                        PointData.save(Point_File);
                    } catch (IOException ignored) {
                    }
                    sender.sendMessage(getLang("Point.AddPoint", PlayerName, String.valueOf(Point)));
                    return false;
                }
                if (args[0].equals("take")) {
                    PointData.set(PlayerName + "_Point", PlayerPoint - Point);
                    try {
                        PointData.save(Point_File);
                    } catch (IOException ignored) {
                    }
                    sender.sendMessage(getLang("Point.TekePoint", PlayerName, String.valueOf(Point)));
                    return false;
                }
                if (args[0].equals("set")) {
                    PointData.set(PlayerName + "_Point", Point);
                    try {
                        PointData.save(Point_File);
                    } catch (IOException ignored) {
                    }
                    sender.sendMessage(getLang("Point.SetPoint", PlayerName, String.valueOf(Point)));
                    return false;
                }
            }
            helpCommand(sender, label);
        }
        if (sender instanceof Player) {
            Player player = (Player) sender;
            sender.sendMessage(getLang("Point.GetPoint", String.valueOf(PointData.getInt(player.getName() + "_Point"))));
        }
        return false;
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("ChengTools.Point")) {
            if (args.length == 1) {
                List<String> TabList = new ArrayList<>();
                TabList.add("help");
                TabList.add("get");
                TabList.add("clear");
                TabList.add("add");
                TabList.add("take");
                TabList.add("set");
                return TabList;
            }
            if (args.length == 2) {
                List<String> TabList = new ArrayList<>();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    TabList.add(player.getName());
                }
                return TabList;
            }
        }
        List<String> TabList = new ArrayList<>();
        TabList.add(null);
        return TabList;
    }

    public void helpCommand(CommandSender sender, String Command) {
        sender.sendMessage(getLang("Point.HelpMessage", Command));
    }
}
