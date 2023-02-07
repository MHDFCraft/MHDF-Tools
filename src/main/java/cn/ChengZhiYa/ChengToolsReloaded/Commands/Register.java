package cn.ChengZhiYa.ChengToolsReloaded.Commands;

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
                    sender.sendMessage(ChatColor("&c你已经登录了!"));
                    return false;
                }
                int MinPasswordLength = main.main.getConfig().getInt("LoginSystemSettings.MinPaswordLength");
                int MaxPasswordLength = main.main.getConfig().getInt("LoginSystemSettings.MaxPaswordLength");
                List<String> EasyPasswords = main.main.getConfig().getStringList("LoginSystemSettings.EasyPasswords");
                if (Password.length() < MinPasswordLength) {
                    sender.sendMessage(ChatColor("&c密码至少" + MinPasswordLength + "位!"));
                    return false;
                }
                if (Password.length() > MaxPasswordLength) {
                    sender.sendMessage(ChatColor("&c密码不能超过" + MinPasswordLength + "位!"));
                    return false;
                }
                for (String EasyPassword : EasyPasswords) {
                    if (Password.equals(EasyPassword)) {
                        sender.sendMessage(ChatColor("&c密码太简单了!"));
                        return false;
                    }
                }
                if (PasswordData.getString(player.getName() + "_Password") != null) {
                    sender.sendMessage(ChatColor("&c你已经注册过了!"));
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
                sender.sendMessage(ChatColor("&a注册成功!"));
            } else {
                sender.sendMessage(ChatColor("&c用法错误,用法:/" + label + " <密码>"));
            }
        } else {
            sender.sendMessage(ChatColor("&c这个命令只有玩家才能执行"));
        }
        return false;
    }
}
