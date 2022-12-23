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

public class PlayerTitle implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        File TitleData = new File(main.main.getDataFolder() + "/TitleData.yml");
        if (!TitleData.exists()) {
            try {
                TitleData.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        YamlConfiguration TitleFileData = YamlConfiguration.loadConfiguration(TitleData);
        if (sender.hasPermission("ChengTools.PlayerTitle")) {
            if (args.length == 1) {
                if (args[0].equals("help")) {
                    HelpCommand(sender, label);
                    return false;
                }
            }
            if (args.length == 2) {
                if (args[0].equals("delPrefix")) {
                    if (TitleFileData.getString(args[1] + "_Prefix") != null) {
                        TitleFileData.set(args[1] + "_Prefix","");
                        try {
                            TitleFileData.save(TitleData);
                        } catch (IOException ignored) {}
                        sender.sendMessage(ChatColor("&a删除成功!"));
                    }else {
                        sender.sendMessage(ChatColor("&c这个玩家没有前缀!"));
                    }
                    return false;
                }
                if (args[0].equals("delSuffix")) {
                    if (TitleFileData.getString(args[1] + "_Suffix") != null) {
                        TitleFileData.set(args[1] + "_Suffix","");
                        try {
                            TitleFileData.save(TitleData);
                        } catch (IOException ignored) {}
                        sender.sendMessage(ChatColor("&a删除成功!"));
                    }else {
                        sender.sendMessage(ChatColor("&c这个玩家没有后缀!"));
                    }
                    return false;
                }
            }
            if (args.length == 3) {
                if (args[0].equals("setPrefix")) {
                    TitleFileData.set(args[1] + "_Prefix",args[2]);
                    try {
                        TitleFileData.save(TitleData);
                    } catch (IOException ignored) {}
                    sender.sendMessage(ChatColor("&a设置成功!"));
                    return false;
                }
                if (args[0].equals("setSuffix")) {
                    TitleFileData.set(args[1] + "_Suffix",args[2]);
                    try {
                        TitleFileData.save(TitleData);
                    } catch (IOException ignored) {}
                    sender.sendMessage(ChatColor("&a设置成功!"));
                    return false;
                }
            }
            HelpCommand(sender, label);
            return false;
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> TabList = new ArrayList<>();
            TabList.add("help");
            TabList.add("setPrefix");
            TabList.add("setSuffix");
            TabList.add("delPrefix");
            TabList.add("delSuffix");
            return TabList;
        }

        if (args.length == 2) {
            List<String> TabList = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                TabList.add(player.getName());
            }
            return TabList;
        }

        if (args.length == 3) {
            List<String> TabList = new ArrayList<>();
            if (args[0].equals("setPrefix")) {
                TabList.add("前缀变量:%ChengTools_Prefix%");
            }
            if (args[0].equals("setSuffix")) {
                TabList.add("后缀变量:%ChengTools_Suffix%");
            }
            return TabList;
        }

        return null;
    }

    public void HelpCommand(CommandSender sender,String Command) {
        sender.sendMessage(ChatColor("\n&7-------&6称号系统&7-------\n" +
                "&6/" + Command + " help &7 &e帮助界面\n" +
                "&6/" + Command + " setPrefix <玩家ID> <称号> &e设置前缀\n" +
                "&6/" + Command + " setSuffix <玩家ID> <称号> &e设置后缀\n" +
                "&6/" + Command + " delPrefix <玩家ID> &e删除前缀\n" +
                "&6/" + Command + " delSuffix <玩家ID> &e删除后缀\n" +
                "&7-------&6称号系统&7-------"));
    }
}
