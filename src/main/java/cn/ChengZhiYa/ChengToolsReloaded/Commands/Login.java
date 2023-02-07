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
import java.util.List;

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
                if (PasswordData.getString(player.getName() + "_Password") == null) {
                    sender.sendMessage(ChatColor("&c你还没有注册呢!"));
                    return false;
                }
                if (Sha256(Password).equals(PasswordData.getString(player.getName() + "_Password"))) {
                    StringHashMap.Set(player.getName() + "_Login", "t");
                    if (main.main.getConfig().getBoolean("LoginSystemSettings.AutoLogin")) {
                        StringHashMap.Set(player.getName() + "_LoginIP", player.getAddress().getHostName());
                    }
                    sender.sendMessage(ChatColor("&a登录成功!"));
                } else {
                    sender.sendMessage(ChatColor("&c密码错误!"));
                }
            } else {
                sender.sendMessage(ChatColor("&c用法错误,用法:/" + label + " <密码>"));
            }
        } else {
            sender.sendMessage(ChatColor("&c这个命令只有玩家才能执行"));
        }
        return false;
    }
}
