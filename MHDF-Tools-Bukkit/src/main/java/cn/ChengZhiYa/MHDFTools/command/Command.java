package cn.ChengZhiYa.MHDFTools.command;

import cn.ChengZhiYa.MHDFTools.manager.CommandManager;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class Command implements CommandExecutor, CommandManager {
    private final String description;
    private final String permission;
    private final boolean onlyPlayer;

    public Command(String description,String permission, boolean onlyPlayer) {
        this.description = description;
        this.permission = permission;
        this.onlyPlayer = onlyPlayer;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (onlyPlayer) {
            if (sender instanceof Player player) {
                execute(player, label, args);
                return false;
            }
        }
        execute(sender, label, args);
        return false;
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {

    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {

    }
}
