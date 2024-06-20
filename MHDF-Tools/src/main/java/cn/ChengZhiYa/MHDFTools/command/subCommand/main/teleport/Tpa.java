package cn.ChengZhiYa.MHDFTools.command.subCommand.main.teleport;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.utils.map.MapUtil;
import cn.ChengZhiYa.MHDFTools.utils.message.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

import static cn.ChengZhiYa.MHDFTools.utils.BCUtil.*;
import static cn.ChengZhiYa.MHDFTools.utils.Util.i18n;

public final class Tpa implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(i18n("OnlyPlayer"));
            return false;
        }

        Player player = (Player) sender;

        if (args.length == 1) {
            String playerName = args[0];

            if (playerName.equalsIgnoreCase(player.getName())) {
                player.sendMessage(i18n("Tpa.SendMe"));
                return false;
            }

            if (!ifPlayerOnline(playerName)) {
                player.sendMessage(i18n("PlayerNotOnline"));
                return false;
            }

            if (MapUtil.getIntHasMap().get(player.getName() + "_TPATime") != null && MapUtil.getIntHasMap().get(player.getName() + "_TPATime") >= 0) {
                player.sendMessage(i18n("Tpa.RepectSend"));
                return false;
            }

            SendTpa(playerName, player.getName());
            player.sendMessage(MessageUtil.colorMessage(i18n("Tpa.SendDone")));
            return false;
        }

        if (args.length == 2) {
            String action = args[0];
            String targetPlayer = args[1];

            if (action.equalsIgnoreCase("accept")) {
                handleAccept(player, targetPlayer);
                return false;
            }

            if (action.equalsIgnoreCase("defuse")) {
                handleDefuse(player, targetPlayer);
                return false;
            }
        }

        player.sendMessage(i18n("Usage.Tpa"));
        return false;
    }

    private void handleAccept(Player player, String targetPlayer) {
        if (MapUtil.getStringHasMap().get(targetPlayer + "_TPAPlayerName") != null && MapUtil.getStringHasMap().get(targetPlayer + "_TPAPlayerName").equals(player.getName())) {
            if (MapUtil.getIntHasMap().get(targetPlayer + "_TPATime") != null && MapUtil.getIntHasMap().get(targetPlayer + "_TPATime") >= 0) {
                CancelTpa(targetPlayer);

                if (!ifPlayerOnline(targetPlayer)) {
                    player.sendMessage(i18n("Tpa.Offline", targetPlayer));
                    return;
                }

                TpPlayer(targetPlayer, player.getName());
                SendMessage(targetPlayer, i18n("Tpa.TeleportDone", player.getName()));
                player.sendMessage(i18n("Tpa.AcceptDone", targetPlayer));
                return;
            } else {
                CancelTpa(targetPlayer);
                player.sendMessage(i18n("Tpa.NotSendTeleport"));
                return;
            }
        }
        player.sendMessage(i18n("Tpa.NotSendTeleport"));
    }

    private void handleDefuse(Player player, String targetPlayer) {
        if (MapUtil.getStringHasMap().get(targetPlayer + "_TPAPlayerName") != null && MapUtil.getStringHasMap().get(targetPlayer + "_TPAPlayerName").equals(player.getName())) {
            if (MapUtil.getIntHasMap().get(targetPlayer + "_TPATime") != null && MapUtil.getIntHasMap().get(targetPlayer + "_TPATime") >= 0) {
                CancelTpa(targetPlayer);

                if (!ifPlayerOnline(targetPlayer)) {
                    player.sendMessage(i18n("Tpa.Offline", targetPlayer));
                    return;
                }

                SendMessage(targetPlayer, i18n("Tpa.Defuse", player.getName()));
                player.sendMessage(i18n("Tpa.DefuseDone", targetPlayer));
                return;
            } else {
                CancelTpa(targetPlayer);
                player.sendMessage(i18n("Tpa.NotSendTeleport"));
                return;
            }
        }
        player.sendMessage(i18n("Tpa.NotSendTeleport"));
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1 && MHDFTools.instance.getConfig().getBoolean("BungeecordSettings.Enable")) {
            return Arrays.asList(PlayerList);
        }
        return null;
    }
}