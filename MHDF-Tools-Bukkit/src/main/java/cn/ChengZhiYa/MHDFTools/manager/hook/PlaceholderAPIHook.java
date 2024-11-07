package cn.ChengZhiYa.MHDFTools.manager.hook;

import cn.ChengZhiYa.MHDFTools.manager.interfaces.Hooker;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

@Getter
@Setter
public final class PlaceholderAPIHook implements Hooker {

    private boolean hasPlaceholderAPI;

    @Override
    public void hook() {
        setHasPlaceholderAPI(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null);
    }

    @Override
    public void unhook() {
        if (isHasPlaceholderAPI()) {
            setHasPlaceholderAPI(false);
        }
    }
}
