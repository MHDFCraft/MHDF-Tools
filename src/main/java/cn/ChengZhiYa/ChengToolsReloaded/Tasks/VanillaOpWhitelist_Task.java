package cn.ChengZhiYa.ChengToolsReloaded.Tasks;

import cn.ChengZhiYa.ChengToolsReloaded.main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public class VanillaOpWhitelist_Task extends BukkitRunnable {
    main pluginmain;

    public VanillaOpWhitelist_Task(main main1) {
        this.pluginmain = main1;
    }

    public void run() {
        if (main.main.getConfig().getBoolean("VanillaOpWhitelist.Enable")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.isOp()) {
                    for (String OpWhitelits : main.main.getConfig().getStringList("VanillaOpWhitelist.Whitelist")) {
                        if (!player.getName().equals(OpWhitelits)) {
                            player.setOp(false);
                            OpSendMessage(ChatColor("&c" + player.getName() + "非法获取了op权限,已踢出!"), player.getName());
                            player.kickPlayer("连接中止");
                        }
                    }
                }
            }
        }
    }
}
