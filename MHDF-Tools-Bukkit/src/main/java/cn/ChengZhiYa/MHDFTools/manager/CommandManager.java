package cn.ChengZhiYa.MHDFTools.manager;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface CommandManager {
    void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args);

    void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args);
}
