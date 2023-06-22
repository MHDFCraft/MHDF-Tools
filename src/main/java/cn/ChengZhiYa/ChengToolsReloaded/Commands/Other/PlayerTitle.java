package cn.ChengZhiYa.ChengToolsReloaded.Commands.Other;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
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

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public final class PlayerTitle implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender.hasPermission("ChengTools.PlayerTitle")) {
            File TitleData = new File(ChengToolsReloaded.instance.getDataFolder() + "/TitleData.yml");
            if (!TitleData.exists()) {
                try {
                    TitleData.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            YamlConfiguration TitleFileData = YamlConfiguration.loadConfiguration(TitleData);
            if (args.length == 1) {
                if (args[0].equals("help")) {
                    HelpCommand(sender, label);
                    return false;
                }
            }
            if (args.length == 2) {
                if (args[0].equals("delPrefix")) {
                    if (TitleFileData.getString(args[1] + "_Prefix") != null) {
                        TitleFileData.set(args[1] + "_Prefix", "");
                        try {
                            TitleFileData.save(TitleData);
                        } catch (IOException ignored) {
                        }
                        sender.sendMessage(getLang("PlayerTitle.RemoveDone"));
                        if (ChengToolsReloaded.instance.getConfig().getBoolean("PlayerTitleSettings.ShowPrefixAndSuffix")) {
                            if (Bukkit.getPlayer(args[1]) != null) {
                                Player player = Bukkit.getPlayer(args[1]);
                                assert player != null;
                                player.setDisplayName(ChatColor(GetPlt(player)[0] + player.getName() + GetPlt(player)[1]));
                                player.setPlayerListName(ChatColor(GetPlt(player)[0] + player.getName() + GetPlt(player)[1]));
                            }
                        }
                    } else {
                        sender.sendMessage(getLang("PlayerTitle.NotHavePrefix"));
                    }
                    return false;
                }
                if (args[0].equals("delSuffix")) {
                    if (TitleFileData.getString(args[1] + "_Suffix") != null) {
                        TitleFileData.set(args[1] + "_Suffix", "");
                        try {
                            TitleFileData.save(TitleData);
                        } catch (IOException ignored) {
                        }
                        sender.sendMessage(getLang("PlayerTitle.RemoveDone"));
                        if (ChengToolsReloaded.instance.getConfig().getBoolean("PlayerTitleSettings.ShowPrefixAndSuffix")) {
                            if (Bukkit.getPlayer(args[1]) != null) {
                                Player player = Bukkit.getPlayer(args[1]);
                                assert player != null;
                                player.setDisplayName(ChatColor(GetPlt(player)[0] + player.getName() + GetPlt(player)[1]));
                                player.setPlayerListName(ChatColor(GetPlt(player)[0] + player.getName() + GetPlt(player)[1]));
                            }
                        }
                    } else {
                        sender.sendMessage(getLang("PlayerTitle.NotHaveSuffix"));
                    }
                    return false;
                }
            }
            if (args.length == 3) {
                if (args[0].equals("setPrefix")) {
                    TitleFileData.set(args[1] + "_Prefix", args[2]);
                    try {
                        TitleFileData.save(TitleData);
                    } catch (IOException ignored) {
                    }
                    sender.sendMessage(getLang("PlayerTitle.SetDone"));
                    if (ChengToolsReloaded.instance.getConfig().getBoolean("PlayerTitleSettings.ShowPrefixAndSuffix")) {
                        if (Bukkit.getPlayer(args[1]) != null) {
                            Player player = Bukkit.getPlayer(args[1]);
                            assert player != null;
                            player.setDisplayName(ChatColor(GetPlt(player)[0] + player.getName() + GetPlt(player)[1]));
                            player.setPlayerListName(ChatColor(GetPlt(player)[0] + player.getName() + GetPlt(player)[1]));
                        }
                    }
                    return false;
                }
                if (args[0].equals("setSuffix")) {
                    TitleFileData.set(args[1] + "_Suffix", args[2]);
                    try {
                        TitleFileData.save(TitleData);
                    } catch (IOException ignored) {
                    }
                    sender.sendMessage(getLang("PlayerTitle.SetDone"));
                    if (ChengToolsReloaded.instance.getConfig().getBoolean("PlayerTitleSettings.ShowPrefixAndSuffix")) {
                        if (Bukkit.getPlayer(args[1]) != null) {
                            Player player = Bukkit.getPlayer(args[1]);
                            assert player != null;
                            player.setDisplayName(ChatColor(GetPlt(player)[0] + player.getName() + GetPlt(player)[1]));
                            player.setPlayerListName(ChatColor(GetPlt(player)[0] + player.getName() + GetPlt(player)[1]));
                        }
                    }
                    return false;
                }
            }
            HelpCommand(sender, label);
            return false;
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender.hasPermission("ChengTools.PlayerTitle")) {
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
        }
        return null;
    }

    public void HelpCommand(CommandSender sender, String Command) {
        sender.sendMessage(getLang("PlayerTitle.HelpMessage", Command));
    }
}
