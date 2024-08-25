package cn.ChengZhiYa.MHDFTools.listeners.player;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.utils.database.NickUtil;
import com.github.Anon8281.universalScheduler.foliaScheduler.FoliaScheduler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class PlayerNickListener implements Listener {
    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent event) {
        new FoliaScheduler(PluginLoader.INSTANCE.getPlugin()).runTaskAsynchronously(() -> {
            if (NickUtil.ifNickExists(event.getPlayer().getName())) {
                Player player = event.getPlayer();
                String nickName = NickUtil.getNickName(player.getName());
                player.setDisplayName(nickName);
                player.setCustomName(nickName);
                player.setPlayerListName(nickName);
                player.setCustomNameVisible(true);
            }
        });
    }
}
