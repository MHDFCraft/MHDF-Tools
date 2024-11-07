package cn.ChengZhiYa.MHDFTools.manager.hook;

import cn.ChengZhiYa.MHDFTools.manager.interfaces.Hooker;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

@Getter
@Setter
public final class PlaceholderAPIHook implements Hooker {

    private boolean hasPlaceholderAPI;

    /**
     * 初始化PlaceholderAPI的API
     */
    @Override
    public void hook() {
        setHasPlaceholderAPI(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null);
    }

    /**
     * 卸载PlaceholderAPI的API
     */
    @Override
    public void unhook() {
        setHasPlaceholderAPI(false);
    }
}
