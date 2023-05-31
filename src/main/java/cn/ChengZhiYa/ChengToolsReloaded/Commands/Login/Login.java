package cn.ChengZhiYa.ChengToolsReloaded.Commands.Login;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHasMap;
import cn.ChengZhiYa.ChengToolsReloaded.Ultis.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;
import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public final class Login implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull java.lang.String label, @NotNull java.lang.String[] args) {
        if (sender instanceof Player) {
            if (args.length == 1) {
                File Login_File = new File(ChengToolsReloaded.instance.getDataFolder(), "LoginData.yml");
                YamlConfiguration PasswordData = YamlConfiguration.loadConfiguration(Login_File);
                java.lang.String Password = args[0];
                Player player = (Player) sender;
                if (getLogin(player)) {
                    sender.sendMessage(getLang("Login.AlreadyLogin"));
                    return false;
                }
                int MinPasswordLength = ChengToolsReloaded.instance.getConfig().getInt("LoginSystemSettings.MinPaswordLength");
                int MaxPasswordLength = ChengToolsReloaded.instance.getConfig().getInt("LoginSystemSettings.MaxPaswordLength");
                List<java.lang.String> EasyPasswords = ChengToolsReloaded.instance.getConfig().getStringList("LoginSystemSettings.EasyPasswords");
                if (Password.length() < MinPasswordLength) {
                    sender.sendMessage(getLang("Login.LengthShort", java.lang.String.valueOf(MinPasswordLength)));
                    return false;
                }
                if (Password.length() > MaxPasswordLength) {
                    sender.sendMessage(getLang("Login.LengthLong", java.lang.String.valueOf(MaxPasswordLength)));
                    return false;
                }
                for (java.lang.String EasyPassword : EasyPasswords) {
                    if (Password.equals(EasyPassword)) {
                        sender.sendMessage(getLang("Login.EasyPassword"));
                        return false;
                    }
                }
                if (PasswordData.getString(player.getName() + "_Password") == null) {
                    sender.sendMessage(getLang("Login.NoRegister"));
                    return false;
                }
                if (Sha256(Password).equals(PasswordData.getString(player.getName() + "_Password"))) {
                    StringHasMap.getHasMap().put(player.getName() + "_Login", "t");
                    if (ChengToolsReloaded.instance.getConfig().getBoolean("LoginSystemSettings.AutoLogin")) {
                        StringHasMap.getHasMap().put(player.getName() + "_LoginIP", Objects.requireNonNull(player.getAddress()).getHostName());
                    }
                    sender.sendMessage(getLang("Login.PasswordRight"));
                } else {
                    sender.sendMessage(getLang("Login.PasswordError"));
                }
            } else {
                sender.sendMessage(getLang("Usage.Login", label));
            }
        } else {
            sender.sendMessage(getLang("OnlyPlayer"));
        }
        return false;
    }
}