package cn.ChengZhiYa.MHDFTools.command.feature;

import cn.ChengZhiYa.MHDFTools.command.AbstractCommand;
import cn.ChengZhiYa.MHDFTools.util.config.LangUtil;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class MHDFTools extends AbstractCommand {
    public MHDFTools() {
        super(
                null,
                "梦之工具主命令",
                "mhdftools.commands.mhdftools",
                false,
                "mhdftools", "mt"
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            // 查看插件信息
            if (args[0].equalsIgnoreCase("about")) {
                sender.sendMessage(
                        LangUtil.i18n("commands.mhdftools.subCommands.about.message")
                                .replace("{command}", label)
                );
                return;
            }

            // 重载插件配置
            if (args[0].equalsIgnoreCase("reload")) {
                sender.sendMessage(
                        LangUtil.i18n("commands.mhdftools.subCommands.reload.message")
                                .replace("{command}", label)
                );
                return;
            }
        }

        // 输出帮助信息
        {
            sender.sendMessage(
                    LangUtil.i18n("commands.mhdftools.subCommands.help.message")
                            .replace("{helpList}", getHelpList())
                            .replace("{command}", label)
            );
        }
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return Arrays.asList("help", "about", "reload");
        }
        return null;
    }

    private String getHelpList() {
        StringBuilder stringBuilder = new StringBuilder();

        List<String> keys = new ArrayList<>(LangUtil.getKeys("commands.mhdftools.subCommands"));
        for (String key : keys) {
            stringBuilder.append(
                    LangUtil.i18n("commands.mhdftools.subCommands.help.commandFormat")
                            .replace("{usage}",
                                    LangUtil.i18n("commands.mhdftools.subCommands." + key + ".usage")
                            )
                            .replace("{description}",
                                    LangUtil.i18n("commands.mhdftools.subCommands." + key + ".description")
                            )
            );
            if (!key.equals(keys.get(keys.size() - 1))) {
                stringBuilder.append("\n");
            }
        }
        return stringBuilder.toString();
    }
}
