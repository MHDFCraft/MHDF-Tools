package cn.ChengZhiYa.MHDFTools.util;

import cn.ChengZhiYa.MHDFTools.Main;
import cn.ChengZhiYa.MHDFTools.command.AbstractCommand;
import org.bukkit.command.PluginCommand;

import java.util.Arrays;

public final class CommandUtil {
    /**
     * 注册命令
     *
     * @param abstractCommand 命令实例
     */
    public static void registerCommand(AbstractCommand abstractCommand) {
        PluginCommand command = Main.instance.getCommand(abstractCommand.getCommands()[0]);
        command.setAliases(Arrays.asList(abstractCommand.getCommands()));
        command.setDescription(abstractCommand.getDescription());
        Main.instance.getServer().getCommandMap().register(Main.instance.getDescription().getName(), command);
        command.setExecutor(abstractCommand);
        command.setTabCompleter(abstractCommand);
    }
}
