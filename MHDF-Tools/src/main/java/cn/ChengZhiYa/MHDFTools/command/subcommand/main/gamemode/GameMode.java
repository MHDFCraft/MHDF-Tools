package cn.ChengZhiYa.MHDFTools.command.subcommand.main.gamemode;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.*;

public final class GameMode implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        String RunSender = i18n("GameMode.Server");
        Player player;

        if (args.length == 1 || args.length == 2) {
            if (args.length == 2) {
                if (Bukkit.getPlayer(args[1]) == null) {
                    sender.sendMessage(i18n("PlayerNotOnline"));
                    return false;
                }
                player = Bukkit.getPlayer(args[1]);
            } else {
                player = (Player) sender;
            }

            if (player != null) {
                boolean wasFlying = player.isFlying();
                boolean allowFlying = player.getAllowFlight();

                switch (args[0].toLowerCase()) {
                    case "生存", "0", "survival" -> {
                        setGameMode(player, 0, RunSender);
                        player.setFlying(allowFlying && wasFlying);
                    }
                    case "冒险", "2", "adventure" -> {
                        setGameMode(player, 2, RunSender);
                        player.setFlying(allowFlying && wasFlying);
                    }
                    case "创造", "1", "creative" -> {
                        setGameMode(player, 1, RunSender);
                        player.setAllowFlight(true);
                        if (wasFlying) player.setFlying(true);
                    }
                    case "旁观", "3", "spectator" -> {
                        setGameMode(player, 3, RunSender);
                        player.setAllowFlight(true);
                        if (wasFlying) player.setFlying(true);
                    }
                    default -> {
                        sender.sendMessage(i18n("Usage.GameMode"), label);
                        return false;
                    }
                }

                return true;
            }
        }

        sender.sendMessage(i18n("Usage.GameMode"), label);
        return false;
    }


    private void setGameMode(Player player, int gameMode, String runSender) {
        player.setGameMode(Objects.requireNonNull(getGamemode(gameMode)));
        player.sendMessage(i18n("GameMode.Done", getGamemodeString(gameMode)));
        adminSendMessage(i18n("GameMode.OtherOpDone", runSender, player.getName(), getGamemodeString(gameMode)), player.getName());
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 1) {
            ArrayList<String> tablist = new ArrayList<>();
            tablist.add("冒险");
            tablist.add("创造");
            tablist.add("旁观");
            tablist.add("生存");
            return tablist;
        }
        return null;
    }
}