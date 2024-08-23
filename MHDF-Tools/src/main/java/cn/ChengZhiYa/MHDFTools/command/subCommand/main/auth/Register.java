package cn.ChengZhiYa.MHDFTools.command.subCommand.main.auth;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.utils.database.LoginUtil;
import cn.ChengZhiYa.MHDFTools.utils.map.MapUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.*;
import static cn.ChengZhiYa.MHDFTools.utils.database.LoginUtil.loginExists;

public final class Register implements CommandExecutor {

    JavaPlugin plugin;
    int maxPasswordLength;
    int minPasswordLength;
    List<String> easyPasswords;
    boolean autoLogin;

    public Register() {
        this.plugin = PluginLoader.INSTANCE.getPlugin();
        this.maxPasswordLength = plugin.getConfig().getInt("LoginSystemSettings.MaxPaswordLength");
        this.minPasswordLength = plugin.getConfig().getInt("LoginSystemSettings.MinPaswordLength");
        this.easyPasswords = plugin.getConfig().getStringList("LoginSystemSettings.EasyPasswords");
        this.autoLogin = plugin.getConfig().getBoolean("LoginSystemSettings.AutoLogin");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(i18n("OnlyPlayer"));
            return false;
        }

        Player player = (Player) sender;

        if (args.length != 1) {
            sender.sendMessage(i18n("Usage.Register", label));
            return false;
        }

        String password = args[0];
        Bukkit.getAsyncScheduler().runNow(plugin, task -> handleRegistration(player, password));

        return true;
    }

    private void handleRegistration(Player player, String password) {
        if (ifLogin(player)) {
            player.sendMessage(i18n("Login.AlreadyLogin"));
            return;
        }

        if (password.length() < minPasswordLength) {
            player.sendMessage(i18n("Login.LengthShort", String.valueOf(minPasswordLength)));
            return;
        }
        if (password.length() > maxPasswordLength) {
            player.sendMessage(i18n("Login.LengthLong", String.valueOf(maxPasswordLength)));
            return;
        }

        if (easyPasswords.contains(password)) {
            player.sendMessage(i18n("Login.EasyPassword"));
            return;
        }

        if (loginExists(player.getName())) {
            player.sendMessage(i18n("Login.AlreadyRegister"));
            return;
        }

        MapUtil.getStringHashMap().put(player.getName() + "_Login", "t");
        LoginUtil.register(player.getName(), sha256(password));

        if (autoLogin) {
            String ipAddress = Objects.requireNonNull(player.getAddress()).getHostName();
            MapUtil.getStringHashMap().put(player.getName() + "_LoginIP", ipAddress);
        }

        player.sendMessage(i18n("Login.RegisterDone"));
    }
}