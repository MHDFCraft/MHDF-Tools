package cn.ChengZhiYa.MHDFTools.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;

public class CommandRegister {

    public static void startRegister(JavaPlugin plugin) {
        registerCommand(plugin, new MainCommand(), "主命令", "mhdftool");
        registerCommand(plugin, new MainCommand(), "主命令", "mt");

    }


    public static void registerCommand(Plugin plugin, CommandExecutor commandExecutor, String description, String... aliases) {
        PluginCommand command = getCommand(aliases[0], plugin);
        command.setAliases(Arrays.asList(aliases));
        command.setDescription(description);
        getCommandMap().register(plugin.getDescription().getName(), command);
        command.setExecutor(commandExecutor);
    }

    private static PluginCommand getCommand(String name, Plugin plugin) {
        PluginCommand command = null;
        try {
            Constructor<PluginCommand> c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            c.setAccessible(true);
            command = c.newInstance(name, plugin);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return command;
    }

    private static CommandMap getCommandMap() {
        CommandMap commandMap = null;
        try {
            if (Bukkit.getPluginManager() instanceof SimplePluginManager) {
                Field f = SimplePluginManager.class.getDeclaredField("commandMap");
                f.setAccessible(true);
                commandMap = (CommandMap) f.get(Bukkit.getPluginManager());
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return commandMap;
    }

    private enum CommandType {
        MAIN, CRASH
    }
}
