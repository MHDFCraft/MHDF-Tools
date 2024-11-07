package cn.ChengZhiYa.MHDFTools.manager.interfaces;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Command {
    void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args);

    void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args);

    List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args);
}
