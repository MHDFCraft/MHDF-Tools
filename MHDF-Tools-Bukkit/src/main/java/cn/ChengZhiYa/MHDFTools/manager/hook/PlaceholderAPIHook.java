package cn.ChengZhiYa.MHDFTools.manager.hook;

import cn.ChengZhiYa.MHDFTools.Main;
import cn.ChengZhiYa.MHDFTools.manager.interfaces.Hooker;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceholderAPIHook implements Hooker {

    private boolean hasPlaceholderAPI;

    @Override
    public void hook() {
        setHasPlaceholderAPI(Main.instance.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null);
    }

    @Override
    public void unhook() {
        if (isHasPlaceholderAPI()) {
            setHasPlaceholderAPI(false);
        }
    }
}
