package cn.ChengZhiYa.ChengToolsReloaded.Tasks;

import cn.ChengZhiYa.ChengToolsReloaded.main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.ChatColor;
import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.getLogin;

public class LoginMessage_Task extends BukkitRunnable {
    main pluginmain;

    public LoginMessage_Task(main main) {
        this.pluginmain = main;
    }

    public void run() {
        File Login_File = new File(main.main.getDataFolder(), "LoginData.yml");
        YamlConfiguration PasswordData = YamlConfiguration.loadConfiguration(Login_File);
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!getLogin(player)) {
                if (PasswordData.getString(player.getName() + "_Password") != null) {
                    player.sendMessage(ChatColor("&e请使用/l <密码>以登录"));
                } else {
                    player.sendMessage(ChatColor("&e请使用/reg <密码>以注册"));
                }
            }
        }
    }
}
