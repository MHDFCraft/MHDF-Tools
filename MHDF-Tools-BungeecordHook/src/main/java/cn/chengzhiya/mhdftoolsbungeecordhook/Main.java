package cn.chengzhiya.mhdftoolsbungeecordhook;

import cn.chengzhiya.mhdftoolsbungeecordhook.Listeners.PluginMessage;
import net.md_5.bungee.api.plugin.Plugin;

public final class Main extends Plugin {

    public static Main main;

    @Override
    public void onEnable() {
        main = this;

        getProxy().getPluginManager().registerListener(this, new PluginMessage());

        getProxy().registerChannel("BungeeCord");
    }

    @Override
    public void onDisable() {
        main = null;
    }
}