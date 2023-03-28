package cn.ChengZhiYa.ChengToolsReloaded.Commands.Login;

import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHashMap;
import cn.ChengZhiYa.ChengToolsReloaded.main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;
import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public class Login implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            if (args.length == 1) {
                File Login_File = new File(main.main.getDataFolder(), "LoginData.yml");
                YamlConfiguration PasswordData = YamlConfiguration.loadConfiguration(Login_File);
                String Password = args[0];
                Player player = (Player) sender;
                if (getLogin(player)) {
                    sender.sendMessage(getLang("Login.AlreadyLogin"));
                    return false;
                }
                int MinPasswordLength = main.main.getConfig().getInt("LoginSystemSettings.MinPaswordLength");
                int MaxPasswordLength = main.main.getConfig().getInt("LoginSystemSettings.MaxPaswordLength");
                List<String> EasyPasswords = main.main.getConfig().getStringList("LoginSystemSettings.EasyPasswords");
                if (Password.length() < MinPasswordLength) {
                    sender.sendMessage(getLang("Login.LengthShort", String.valueOf(MinPasswordLength)));
                    return false;
                }
                if (Password.length() > MaxPasswordLength) {
                    sender.sendMessage(getLang("Login.LengthLong", String.valueOf(MaxPasswordLength)));
                    return false;
                }
                for (String EasyPassword : EasyPasswords) {
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
                    StringHashMap.Set(player.getName() + "_Login", "t");
                    if (main.main.getConfig().getBoolean("LoginSystemSettings.AutoLogin")) {
                        StringHashMap.Set(player.getName() + "_LoginIP", Objects.requireNonNull(player.getAddress()).getHostName());
                    }
                    sender.sendMessage(getLang("Login.PasswordRight"));
                } else {
                    sender.sendMessage(getLang("Login.PasswordError"));
                }
            } else {
                sender.sendMessage(getLang("Usage.Login",label));
            }
        } else {
            sender.sendMessage(getLang("OnlyPlayer"));
        }
        return false;
    }
}
