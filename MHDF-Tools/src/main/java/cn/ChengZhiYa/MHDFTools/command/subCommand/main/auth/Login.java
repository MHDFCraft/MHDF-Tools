package cn.ChengZhiYa.MHDFTools.command.subCommand.main.auth;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.utils.map.MapUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.*;
import static cn.ChengZhiYa.MHDFTools.utils.database.LoginUtil.checkPassword;
import static cn.ChengZhiYa.MHDFTools.utils.database.LoginUtil.loginExists;

public final class Login implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(i18n("OnlyPlayer"));
            return false;
        }

        Player player = (Player) sender;
        Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
            if (args.length != 1) {
                sender.sendMessage(i18n("Usage.Login", label));
                return;
            }

            String password = args[0];
            if (ifLogin(player)) {
                sender.sendMessage(i18n("Login.AlreadyLogin"));
                return;
            }

            int maxPasswordLength = MHDFTools.instance.getConfig().getInt("LoginSystemSettings.MaxPaswordLength");
            int minPasswordLength = MHDFTools.instance.getConfig().getInt("LoginSystemSettings.MinPaswordLength");
            List<String> easyPasswords = MHDFTools.instance.getConfig().getStringList("LoginSystemSettings.EasyPasswords");

            if (password.length() < minPasswordLength) {
                sender.sendMessage(i18n("Login.LengthShort", String.valueOf(minPasswordLength)));
                return;
            }
            if (password.length() > maxPasswordLength) {
                sender.sendMessage(i18n("Login.LengthLong", String.valueOf(maxPasswordLength)));
                return;
            }
            if (easyPasswords.contains(password)) {
                sender.sendMessage(i18n("Login.EasyPassword"));
                return;
            }
            if (!loginExists(player.getName())) {
                sender.sendMessage(i18n("Login.NoRegister"));
                return;
            }

            if (checkPassword(player.getName(), sha256(password))) {
                MapUtil.getStringHashMap().put(player.getName() + "_Login", "t");
                if (MHDFTools.instance.getConfig().getBoolean("LoginSystemSettings.AutoLogin")) {
                    String playerIP = Objects.requireNonNull(player.getAddress()).getHostString();
                    MapUtil.getStringHashMap().put(player.getName() + "_LoginIP", playerIP);
                }
                sender.sendMessage(i18n("Login.PasswordRight"));
            } else {
                sender.sendMessage(i18n("Login.PasswordError"));
            }
        });
        return true;
    }
}