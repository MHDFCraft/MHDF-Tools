package cn.ChengZhiYa.ChengToolsReloaded.Commands.Other;

import cn.ChengZhiYa.ChengToolsReloaded.Ultis.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public final class Gamemode implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission(" ChengTools.Gamemode")) {
            String RunSender = getLang("Gamemode.Server");
            if (sender instanceof Player) {
                RunSender = sender.getName();
                if (args.length == 1) {
                    Player player = (Player) sender;
                    String GameModeName = args[0];
                    if (GameModeName.equals("生存") || GameModeName.equals("0") || GameModeName.equalsIgnoreCase("survival")) {
                        player.setGameMode(Objects.requireNonNull(GetGamemode(0)));
                        player.sendMessage(getLang("Gamemode.Done", GetGamemodeString(0)));
                        OpSendMessage(getLang("Gamemode.OtherOpDone", player.getName(), player.getName(), GetGamemodeString(0)), player.getName());
                    }
                    if (GameModeName.equals("冒险") || GameModeName.equals("2") || GameModeName.equalsIgnoreCase("adventure")) {
                        player.setGameMode(Objects.requireNonNull(GetGamemode(2)));
                        player.sendMessage(getLang("Gamemode.Done", GetGamemodeString(2)));
                        OpSendMessage(getLang("Gamemode.OtherOpDone", player.getName(), player.getName(), GetGamemodeString(2)), player.getName());
                    }
                    if (GameModeName.equals("创造") || GameModeName.equals("1") || GameModeName.equalsIgnoreCase("creative")) {
                        player.setGameMode(Objects.requireNonNull(GetGamemode(1)));
                        player.sendMessage(getLang("Gamemode.Done", GetGamemodeString(1)));
                        OpSendMessage(getLang("Gamemode.OtherOpDone", player.getName(), player.getName(), GetGamemodeString(1)), player.getName());
                    }
                    if (GameModeName.equals("旁观") || GameModeName.equals("3") || GameModeName.equalsIgnoreCase("spectator")) {
                        player.setGameMode(Objects.requireNonNull(GetGamemode(3)));
                        player.sendMessage(getLang("Gamemode.Done", GetGamemodeString(3)));
                        OpSendMessage(getLang("Gamemode.OtherOpDone", player.getName(), player.getName(), GetGamemodeString(3)), player.getName());
                    }
                    return false;
                }
            }
            if (args.length == 2) {
                String GameModeName = args[0];
                String PlayerName = args[1];
                if (Bukkit.getPlayer(PlayerName) == null) {
                    sender.sendMessage(getLang("PlayerNotOnline"));
                    return false;
                }
                Player player = Bukkit.getPlayer(PlayerName);
                assert player != null;
                if (GameModeName.equals("生存") || GameModeName.equals("0") || GameModeName.equalsIgnoreCase("survival")) {
                    player.setGameMode(Objects.requireNonNull(GetGamemode(0)));
                    player.sendMessage(getLang("Gamemode.Done", GetGamemodeString(0)));
                    OpSendMessage(getLang("Gamemode.OtherOpDone", RunSender, player.getName(), GetGamemodeString(0)), sender.getName());
                }
                if (GameModeName.equals("冒险") || GameModeName.equals("2") || GameModeName.equalsIgnoreCase("adventure")) {
                    player.setGameMode(Objects.requireNonNull(GetGamemode(2)));
                    player.sendMessage(getLang("Gamemode.Done", GetGamemodeString(2)));
                    OpSendMessage(getLang("Gamemode.OtherOpDone", RunSender, player.getName(), GetGamemodeString(2)), sender.getName());
                }
                if (GameModeName.equals("创造") || GameModeName.equals("1") || GameModeName.equalsIgnoreCase("creative")) {
                    player.setGameMode(Objects.requireNonNull(GetGamemode(1)));
                    player.sendMessage(getLang("Gamemode.Done", GetGamemodeString(1)));
                    OpSendMessage(getLang("Gamemode.OtherOpDone", RunSender, player.getName(), GetGamemodeString(1)), sender.getName());
                }
                if (GameModeName.equals("旁观") || GameModeName.equals("3") || GameModeName.equalsIgnoreCase("spectator")) {
                    player.setGameMode(Objects.requireNonNull(GetGamemode(3)));
                    player.sendMessage(getLang("Gamemode.Done", GetGamemodeString(3)));
                    OpSendMessage(getLang("Gamemode.OtherOpDone", RunSender, player.getName(), GetGamemodeString(3)), sender.getName());
                }
                return false;
            }
            sender.sendMessage(getLang("Usage.Gamemode"));
        } else {
            sender.sendMessage(getLang("NoPermission"));
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
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