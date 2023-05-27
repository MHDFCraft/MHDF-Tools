package cn.ChengZhiYa.ChengToolsReloaded.Commands.Other;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import cn.ChengZhiYa.ChengToolsReloaded.Ultis.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.util.StringUtil;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarFile;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public final class PluginManage implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("ChengTools.PLuginManage")) {
            if (args.length == 2) {
                if (args[0].equals("help")) {
                    Help(sender, label);
                }
                if (args[0].equals("load")) {
                    Plugin potential = getPluginName(args, 1);

                    if (ChengToolsReloaded.instance.getConfig().getStringList("PluginManageSettings.WhiteListPluginList").contains(args[1])) {
                        sender.sendMessage(getLang("PluginManage.WhiteListPlugin"));
                        return false;
                    }

                    if (potential != null) {
                        sender.sendMessage(getLang("PluginManage.AlreadyLoad", args[1]));
                        return false;
                    }

                    String name = consolidateStrings(args, 1);

                    String Message = load(name);
                    if (Message != null) {
                        sender.sendMessage(ChatColor("&c" + Message));
                        return false;
                    }

                    sender.sendMessage(getLang("PluginManage.LoadDone", args[1]));
                }
                if (args[0].equals("unload")) {
                    Plugin target = getPluginName(args, 1);

                    if (ChengToolsReloaded.instance.getConfig().getStringList("PluginManageSettings.WhiteListPluginList").contains(args[1])) {
                        sender.sendMessage(getLang("PluginManage.WhiteListPlugin"));
                        return false;
                    }

                    if (target == null) {
                        sender.sendMessage(getLang("PluginManage.AlreadyUnLoad", args[1]));
                        return false;
                    }

                    unload(target);

                    sender.sendMessage(getLang("PluginManage.UnLoadDone", args[1]));
                }
                if (args[0].equals("reload")) {
                    Plugin target = getPluginName(args, 1);

                    if (ChengToolsReloaded.instance.getConfig().getStringList("PluginManageSettings.WhiteListPluginList").contains(args[1])) {
                        sender.sendMessage(getLang("PluginManage.WhiteListPlugin"));
                        return false;
                    }

                    if (target == null) {
                        sender.sendMessage(getLang("PluginManage.NotLoad"));
                        return false;
                    }

                    if (Bukkit.getPluginManager().getPlugin(args[1]) == null) {
                        sender.sendMessage(getLang("PluginManage.NotLoad"));
                        return false;
                    }

                    reload(target);

                    sender.sendMessage(getLang("PluginManage.ReLoadDone", args[1]));
                }
            } else {
                Help(sender, label);
            }
        } else {
            sender.sendMessage(getLang("NoPermission"));
        }
        return false;
    }

    @SuppressWarnings("resource")
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
            if (args[0].equals("load")) {
                List<String> TabList = new ArrayList<>();
                for (File pluginFile : Objects.requireNonNull(new File("plugins").listFiles())) {
                    try {
                        if (pluginFile.isDirectory()) continue;

                        if (!pluginFile.getName().toLowerCase().endsWith(".jar"))
                            if (!new File("plugins", pluginFile.getName() + ".jar").exists()) continue;

                        JarFile jarFile;
                        try {
                            jarFile = new JarFile(pluginFile);
                        } catch (Exception ignored) {
                            continue;
                        }

                        if (jarFile.getEntry("plugin.yml") == null) continue;

                        InputStream stream;
                        try {
                            stream = jarFile.getInputStream(jarFile.getEntry("plugin.yml"));
                        } catch (Exception ignored) {
                            continue;
                        }

                        if (stream == null) {
                            continue;
                        }

                        PluginDescriptionFile descriptionFile;
                        try {
                            descriptionFile = new PluginDescriptionFile(stream);
                        } catch (Exception ignored) {
                            continue;
                        }

                        TabList.add(pluginFile.getName().substring(0, pluginFile.getName().length() - ".jar".length()));

                        for (Plugin plugin : Bukkit.getPluginManager().getPlugins())
                            if (plugin.getName().equalsIgnoreCase(descriptionFile.getName()))
                                TabList.remove(pluginFile.getName().substring(0, pluginFile.getName().length() - ".jar".length()));
                    } catch (Exception ignored) {
                    }
                }
                return TabList;
            }
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
        sender.sendMessage(getLang("PluginManage.HelpMessage", Command));
    }
}
