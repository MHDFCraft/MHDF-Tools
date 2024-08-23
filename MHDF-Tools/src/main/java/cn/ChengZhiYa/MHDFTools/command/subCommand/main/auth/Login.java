package cn.ChengZhiYa.MHDFTools.command.subCommand.main.auth;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
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
        Bukkit.getAsyncScheduler().runNow(PluginLoader.INSTANCE.getPlugin(), task -> {
            if (args.length != 1) {
                sender.sendMessage(i18n("Usage.Login", label));
                return;
            }

            String password = args[0];
            if (ifLogin(player)) {
                sender.sendMessage(i18n("Login.AlreadyLogin"));
                return;
            }

            int maxPasswordLength = PluginLoader.INSTANCE.getPlugin().getConfig().getInt("LoginSystemSettings.MaxPaswordLength");
            int minPasswordLength = PluginLoader.INSTANCE.getPlugin().getConfig().getInt("LoginSystemSettings.MinPaswordLength");
            List<String> easyPasswords = PluginLoader.INSTANCE.getPlugin().getConfig().getStringList("LoginSystemSettings.EasyPasswords");

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
                if (PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("LoginSystemSettings.AutoLogin")) {
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