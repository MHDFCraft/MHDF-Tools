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
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public class Register implements CommandExecutor {
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
                if (PasswordData.getString(player.getName() + "_Password") != null) {
                    sender.sendMessage(getLang("Login.AlreadyRegister"));
                    return false;
                }
                StringHashMap.Set(player.getName() + "_Login", "t");
                PasswordData.set(player.getName() + "_Password", Sha256(Password));
                try {
                    PasswordData.save(Login_File);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (main.main.getConfig().getBoolean("LoginSystemSettings.AutoLogin")) {
                    StringHashMap.Set(player.getName() + "_LoginIP", Objects.requireNonNull(player.getAddress()).getHostName());
                }
                sender.sendMessage(getLang("Login.RegisterDone"));
            } else {
                sender.sendMessage(getLang("Usage.Register",label));
            }
        } else {
            sender.sendMessage(getLang("OnlyPlayer"));
        }
        return false;
    }
}
