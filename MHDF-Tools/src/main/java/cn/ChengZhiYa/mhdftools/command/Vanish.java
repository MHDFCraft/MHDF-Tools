package cn.chengzhiya.mhdftools.command;

import cn.chengzhiya.mhdftools.MHDFTools;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static cn.chengzhiya.mhdftools.util.Util.*;

public final class Vanish implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                if (sender.hasPermission("MHDFTools.Vanish.Give")) {
                    if (Bukkit.getPlayer(args[0]) == null) {
                        sender.sendMessage(i18n("PlayerNotOnline"));
                        return false;
                    }
                    player = Bukkit.getPlayer(args[0]);
                }
            }
            if (!VanishList.contains(Objects.requireNonNull(player).getName())) {
                for (Player OnlinePlayer : Bukkit.getOnlinePlayers()) {
                    try {
                        Player.class.getDeclaredMethod("hidePlayer", Plugin.class, Player.class);
                        OnlinePlayer.hidePlayer(MHDFTools.instance, player);
                    } catch (NoSuchMethodException e) {
                        OnlinePlayer.hidePlayer(player);
                    }
                }
                PotionEffect INVISIBILITY = new PotionEffect(PotionEffectType.INVISIBILITY, 99999, 255, true);
                player.addPotionEffect(INVISIBILITY);
                player.showBossBar(getVanishBossBar());
                player.sendMessage(i18n("Vanish.Done", i18n("Vanish.Enable")));
                if (!player.getName().equals(sender.getName())) {
                    sender.sendMessage(i18n("Vanish.SetDone", player.getName(), i18n("Vanish.Enable")));
                }
                VanishList.add(player.getName());
            } else {
                for (Player OnlinePlayer : Bukkit.getOnlinePlayers()) {
                    try {
                        Player.class.getDeclaredMethod("hidePlayer", Plugin.class, Player.class);
                        OnlinePlayer.hidePlayer(MHDFTools.instance, player);
                    } catch (NoSuchMethodException e) {
                        OnlinePlayer.hidePlayer(player);
                    }
                }
                player.removePotionEffect(PotionEffectType.INVISIBILITY);
                player.hideBossBar(getVanishBossBar());
                player.sendMessage(i18n("Vanish.Done", i18n("Vanish.Disabled")));
                if (!player.getName().equals(sender.getName())) {
                    sender.sendMessage(i18n("Vanish.SetDone", player.getName(), i18n("Vanish.Disabled")));
                }
                VanishList.remove(player.getName());
            }
            File VanishCacheFile = new File(MHDFTools.instance.getDataFolder(), "Cache/VanishCache.yml");
            YamlConfiguration VanishCache = YamlConfiguration.loadConfiguration(VanishCacheFile);
            VanishCache.set("VanishList", VanishList);
            try {
                VanishCache.save(VanishCacheFile);
            } catch (IOException ignored) {
            }
        }
        return false;
    }
}
