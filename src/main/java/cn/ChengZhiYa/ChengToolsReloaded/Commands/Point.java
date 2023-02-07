package cn.ChengZhiYa.ChengToolsReloaded.Commands;

import cn.ChengZhiYa.ChengToolsReloaded.main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.ChatColor;

public class Point implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        File Point_File = new File(main.main.getDataFolder(), "PointData.yml");
        YamlConfiguration PointData = YamlConfiguration.loadConfiguration(Point_File);
        if (sender.hasPermission("ChengTools.Point")) {
            if (args.length == 2) {
                String PlayerName = args[1];
                if (args[0].equals("get")) {
                    sender.sendMessage(ChatColor("&e" + PlayerName + "的点券数量为: " + PointData.getInt(PlayerName + "_Point")));
                    return false;
                }
                if (args[0].equals("clear")) {
                    PointData.set(PlayerName + "_Point", null);
                    try {
                        PointData.save(Point_File);
                    } catch (IOException ignored) {}
                    sender.sendMessage(ChatColor("&a已清空" + PlayerName + "的所有点券!"));
                    return false;
                }
            }
            if (args.length == 3) {
                String PlayerName = args[1];
                int Point = Integer.parseInt(args[2]);
                int PlayerPoint = PointData.getInt(PlayerName + "_Point");
                if (args[0].equals("add")) {
                    PointData.set(PlayerName + "_Point", PlayerPoint+Point);
                    try {
                        PointData.save(Point_File);
                    } catch (IOException ignored) {}
                    sender.sendMessage(ChatColor("&a已给" + PlayerName + "增加" + Point + "个点券!"));
                    return false;
                }
                if (args[0].equals("take")) {
                    PointData.set(PlayerName + "_Point", PlayerPoint-Point);
                    try {
                        PointData.save(Point_File);
                    } catch (IOException ignored) {}
                    sender.sendMessage(ChatColor("&a已给" + PlayerName + "减少" + Point + "个点券!"));
                    return false;
                }
                if (args[0].equals("set")) {
                    PointData.set(PlayerName + "_Point", Point);
                    try {
                        PointData.save(Point_File);
                    } catch (IOException ignored) {}
                    sender.sendMessage(ChatColor("&a已将" + PlayerName + "的点券数设置为" + Point + "!"));
                    return false;
                }
            }
            helpCommand(sender,label);
        }
        if (sender instanceof Player) {
            Player player = (Player) sender;
            sender.sendMessage(ChatColor("&e您的点券数量为: " + PointData.getInt(player.getName() + "_Point")));
        }else {
            sender.sendMessage(ChatColor("&a命令帮助:/" + label + " help"));
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
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

    public void helpCommand(CommandSender sender,String label) {
        sender.sendMessage(ChatColor("\n&7=========&6点券系统&7=========\n" +
                "/" + label + " help - 查询插件帮助\n" +
                "/" + label + " get <玩家ID> - 查询指定玩家的点券数\n" +
                "/" + label + " clear <玩家ID> - 清空制动玩家的点券数\n" +
                "/" + label + " add <玩家ID> - 增加指定玩家点券数\n" +
                "/" + label + " take <玩家ID> - 减少指定玩家的点券数\n" +
                "/" + label + " set <玩家ID> - 设定指定玩家的点券数\n" +
                "&7=========&6点券系统&7========="));
    }
}
