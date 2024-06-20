package cn.ChengZhiYa.MHDFTools.command.subCommand.main.auth;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.utils.database.LoginUtil;
import cn.ChengZhiYa.MHDFTools.utils.map.MapUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.utils.Util.*;
import static cn.ChengZhiYa.MHDFTools.utils.database.LoginUtil.loginExists;

public final class Register implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(i18n("OnlyPlayer"));
            return false;
        }

        Player player = (Player) sender;

        Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
            if (args.length == 1) {
                handleRegistration(player, args[0]);
            } else {
                sender.sendMessage(i18n("Usage.Register", label));
            }
        });

        return true;
    }

    private void handleRegistration(Player player, String password) {
        if (ifLogin(player)) {
            player.sendMessage(i18n("Login.AlreadyLogin"));
            return;
        }

        int minPasswordLength = MHDFTools.instance.getConfig().getInt("LoginSystemSettings.MinPaswordLength");
        int maxPasswordLength = MHDFTools.instance.getConfig().getInt("LoginSystemSettings.MaxPaswordLength");
        List<String> easyPasswords = MHDFTools.instance.getConfig().getStringList("LoginSystemSettings.EasyPasswords");

        if (password.length() < minPasswordLength) {
            player.sendMessage(i18n("Login.LengthShort", String.valueOf(minPasswordLength)));
            return;
        }
        if (password.length() > maxPasswordLength) {
            player.sendMessage(i18n("Login.LengthLong", String.valueOf(maxPasswordLength)));
            return;
        }
        for (String easyPassword : easyPasswords) {
            if (password.equals(easyPassword)) {
                player.sendMessage(i18n("Login.EasyPassword"));
                return;
            }
        }
        if (loginExists(player.getName())) {
            player.sendMessage(i18n("Login.AlreadyRegister"));
            return;
        }

        MapUtil.getStringHasMap().put(player.getName() + "_Login", "t");
        LoginUtil.register(player.getName(), Sha256(password));

        if (MHDFTools.instance.getConfig().getBoolean("LoginSystemSettings.AutoLogin")) {
            String ipAddress = Objects.requireNonNull(player.getAddress()).getHostName();
            MapUtil.getStringHasMap().put(player.getName() + "_LoginIP", ipAddress);
        }

        player.sendMessage(i18n("Login.RegisterDone"));
    }
}