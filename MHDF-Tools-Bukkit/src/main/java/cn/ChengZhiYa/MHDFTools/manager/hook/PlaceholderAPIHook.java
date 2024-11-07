package cn.ChengZhiYa.MHDFTools.manager.hook;

import cn.ChengZhiYa.MHDFTools.manager.AbstractHook;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

@Getter
@Setter
public final class PlaceholderAPIHook extends AbstractHook {
    /**
     * 初始化PlaceholderAPI的API
     */
    @Override
    public void hook() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            super.enable = true;
        }
    }

    /**
     * 卸载PlaceholderAPI的API
     */
    @Override
    public void unhook() {
        super.enable = false;
    }
}
