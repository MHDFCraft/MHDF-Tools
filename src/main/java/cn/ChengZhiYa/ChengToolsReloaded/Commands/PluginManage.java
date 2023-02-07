package cn.ChengZhiYa.ChengToolsReloaded.Commands;

import cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public class PluginManage implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("ChengTools.PLuginManage")) {
            if (args.length == 2) {
                if (args[0].equals("help")) {
                    Help(sender, label);
                }
                if (args[0].equals("load")) {
                    Plugin potential = getPluginName(args, 1);

                    if (potential != null) {
                        sender.sendMessage(ChatColor("&c" + args[1] + "已经被加载了!"));
                        return false;
                    }

                    String name = consolidateStrings(args, 1);

                    String Message = load(name);
                    if (Message != null) {
                        sender.sendMessage(ChatColor("&c" + Message));
                        return false;
                    }

                    sender.sendMessage(ChatColor("&a&l" + args[1] + "加载成功!"));
                }
                if (args[0].equals("unload")) {
                    Plugin target = getPluginName(args, 1);

                    if (target == null) {
                        sender.sendMessage(ChatColor("&c" + args[1] + "已经被卸载了!"));
                        return false;
                    }

                    unload(target);

                    sender.sendMessage(ChatColor("&a&l" + args[1] + "卸载成功!"));
                }
                if (args[0].equals("reload")) {
                    Plugin target = getPluginName(args, 1);

                    if (target == null) {
                        sender.sendMessage(ChatColor("&c用法错误,用法:/" + label + " reload <玩家插件>"));
                        return false;
                    }

                    if (Bukkit.getPluginManager().getPlugin(args[1]) == null) {
                        sender.sendMessage(ChatColor("&c这个插件没有被加载,无法重载!"));
                        return false;
                    }

                    reload(target);

                    sender.sendMessage(ChatColor("&a&l" + args[1] + "重载成功!"));
                }
            } else {
                Help(sender, label);
            }
        } else {
            sender.sendMessage(multi.ChatColor("&c您没有权限怎么做!"));
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> TabList = new ArrayList<>();
            TabList.add("load");
            TabList.add("unload");
            TabList.add("reload");
            return TabList;
        }
        if (args.length == 2) {
            List<String> TabList = new ArrayList<>();
            String partialPlugin = args[1];
            List<String> plugins = getPluginNames(false);
            StringUtil.copyPartialMatches(partialPlugin, plugins, TabList);

            Collections.sort(TabList);
            return TabList;
        }
        return null;
    }

    public void Help(CommandSender sender, String Command) {
        sender.sendMessage(ChatColor("\n&7-------&6PluginManage&7-------\n" +
                "&6/" + Command + " help &7 &e帮助界面\n" +
                "&6/" + Command + " load <插件名称> &7 &e加载插件\n" +
                "&6/" + Command + " unload <插件名称> &7 &e卸载插件\n" +
                "&6/" + Command + " reload <插件名称> &7 &e重载插件\n" +
                "&7-------&6PluginManage&7-------"));
    }
}
