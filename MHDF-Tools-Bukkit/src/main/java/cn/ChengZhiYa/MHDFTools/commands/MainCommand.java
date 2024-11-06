package cn.ChengZhiYa.MHDFTools.commands;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MainCommand implements CommandExecutor, TabExecutor {

    JavaPlugin plugin = PluginLoader.INSTANCE.getPlugin();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            return true;
        }

        String subCommand = args[0].toLowerCase();
        switch (subCommand) {
            case "version" -> {
                if (sender.hasPermission("mhdftool.commands.version")) {
                    String version = PluginLoader.INSTANCE.getVersion();
                    //你自己写提示和配置那些
                }
            }
            case "reload" -> {
                if (sender.hasPermission("mhdftool.commands.reload")) {
                    plugin.reloadConfig();
                }
            }

            //你自己写提示和配置那些
            default -> {
            }
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            if (sender.hasPermission("mhdftool.commands.version")) {
                completions.add("version");
            }
            if (sender.hasPermission("mhdftool.commands.reload")) {
                completions.add("reload");
            }

            String input = args[0].toLowerCase();
            completions.removeIf(completion -> !completion.startsWith(input));
        }

        return completions;
    }
}