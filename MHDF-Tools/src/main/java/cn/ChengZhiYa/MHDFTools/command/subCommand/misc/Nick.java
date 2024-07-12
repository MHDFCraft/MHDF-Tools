package cn.ChengZhiYa.MHDFTools.command.subCommand.misc;

import cn.ChengZhiYa.MHDFTools.utils.database.NickUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.i18n;

public final class Nick implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 1) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args[0].equals("off")) {
                    NickUtil.removeNickName(player.getName());
                    player.setDisplayName(player.getName());
                    player.setCustomName(player.getName());
                    player.setPlayerListName(player.getName());
                    player.setCustomNameVisible(false);
                    player.sendMessage(i18n("Nick.SetDone", player.getName()));
                } else {
                    NickUtil.setNickName(player.getName(), args[0]);
                    player.setDisplayName(args[0]);
                    player.setCustomName(args[0]);
                    player.setPlayerListName(args[0]);
                    player.setCustomNameVisible(true);
                    player.sendMessage(i18n("Nick.SetDone", args[0]));
                }
            } else {
                sender.sendMessage(i18n("OnlyPlayer"));
            }
        } else {
            sender.sendMessage(i18n("Usage.Nick"));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        return Collections.singletonList("off");
    }
}