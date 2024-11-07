package cn.ChengZhiYa.MHDFTools.command;

import cn.ChengZhiYa.MHDFTools.manager.interfaces.Command;
import cn.ChengZhiYa.MHDFTools.util.config.ConfigUtil;
import cn.ChengZhiYa.MHDFTools.util.config.LangUtil;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class AbstractCommand implements TabExecutor, Command {
    private final boolean enable;
    private final String description;
    private final String permission;
    private final boolean onlyPlayer;
    private final String[] commands;

    public AbstractCommand(String enableKey, @NotNull String description, String permission, boolean onlyPlayer, String... commands) {
        if (enableKey != null && !enableKey.isEmpty()) {
            this.enable = ConfigUtil.getConfig().getBoolean(enableKey);
        } else {
            this.enable = true;
        }
        this.description = description;
        this.permission = permission;
        this.onlyPlayer = onlyPlayer;
        this.commands = commands;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (onlyPlayer) {
            if (sender instanceof Player player) {
                execute(player, label, args);
            } else {
                sender.sendMessage(LangUtil.i18n("onlyPlayer"));
            }
            return false;
        }
        execute(sender, label, args);
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return tabCompleter(sender, label, args);
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {

    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {

    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
