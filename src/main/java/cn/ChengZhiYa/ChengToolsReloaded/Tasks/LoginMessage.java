package cn.ChengZhiYa.ChengToolsReloaded.Tasks;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public final class LoginMessage extends BukkitRunnable {

    public LoginMessage() {
    }

    public void run() {
        File Login_File = new File(ChengToolsReloaded.instance.getDataFolder(), "LoginData.yml");
        YamlConfiguration PasswordData = YamlConfiguration.loadConfiguration(Login_File);
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!getLogin(player)) {
                if (PasswordData.getString(player.getName() + "_Password") != null) {
                    player.sendMessage(getLang("Login.LoginMessage"));
                } else {
                    player.sendMessage(getLang("Login.RegisterMessage"));
                }
            }
        }
    }
}
