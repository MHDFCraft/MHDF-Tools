package cn.ChengZhiYa.MHDFTools.command.feature;

import cn.ChengZhiYa.MHDFTools.command.AbstractCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public final class MHDFTools extends AbstractCommand {
    public MHDFTools() {
        super(
                "梦之工具主命令",
                "mhdftools.commands.mhdftools",
                false,
                "mhdftools", "mt"
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {

    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return Arrays.asList("help", "about", "reload");
        }
        return null;
    }
}
