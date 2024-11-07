package cn.ChengZhiYa.MHDFTools.manager.init;

import cn.ChengZhiYa.MHDFTools.Main;
import cn.ChengZhiYa.MHDFTools.command.AbstractCommand;
import cn.ChengZhiYa.MHDFTools.manager.interfaces.Initer;
import cn.ChengZhiYa.MHDFTools.util.config.LangUtil;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;

@SuppressWarnings("deprecation")
public final class CommandManager implements Initer {
    /**
     * 注册所有启用的命令
     */
    @Override
    public void init() {
        try {
            Reflections reflections = new Reflections(AbstractCommand.class.getPackageName());

            for (Class<? extends AbstractCommand> clazz : reflections.getSubTypesOf(AbstractCommand.class)) {
                if (!Modifier.isAbstract(clazz.getModifiers())) {
                    AbstractCommand abstractCommand = clazz.getDeclaredConstructor().newInstance();
                    if (abstractCommand.isEnable()) {
                        registerCommand(abstractCommand);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 注册命令
     *
     * @param abstractCommand 命令实例
     */
    @SuppressWarnings("deprecation")
    private void registerCommand(AbstractCommand abstractCommand) throws Exception {
        Constructor<PluginCommand> commandConstructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
        commandConstructor.setAccessible(true);
        PluginCommand command = commandConstructor.newInstance(abstractCommand.getCommands()[0], Main.instance);

        command.setAliases(Arrays.asList(abstractCommand.getCommands()));
        command.setDescription(abstractCommand.getDescription());
        command.setPermission(abstractCommand.getPermission());
        command.setPermissionMessage(LangUtil.i18n("noPermission"));

        command.setExecutor(abstractCommand);
        command.setTabCompleter(abstractCommand);
        Main.instance.getServer().getCommandMap().register(Main.instance.getDescription().getName(), command);
    }
}
