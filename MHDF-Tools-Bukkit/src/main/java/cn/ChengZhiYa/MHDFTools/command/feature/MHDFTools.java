package cn.ChengZhiYa.MHDFTools.command.feature;

import cn.ChengZhiYa.MHDFTools.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public final class MHDFTools extends Command {
    public MHDFTools(String description, String permission, boolean onlyPlayer) {
        super(description, permission, onlyPlayer);
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {

    }
}
