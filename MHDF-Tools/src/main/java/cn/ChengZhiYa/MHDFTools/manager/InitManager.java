package cn.ChengZhiYa.MHDFTools.manager;

import cn.ChengZhiYa.MHDFTools.command.CommandRegister;
import cn.ChengZhiYa.MHDFTools.manager.init.Invitable;
import cn.ChengZhiYa.MHDFTools.manager.init.load.Dependencies;
import cn.ChengZhiYa.MHDFTools.manager.init.start.*;
import cn.ChengZhiYa.MHDFTools.manager.init.stop.stopChannel;
import cn.ChengZhiYa.MHDFTools.manager.init.stop.stopFreeze;
import cn.ChengZhiYa.MHDFTools.manager.init.stop.stopPlaceholderAPI;
import cn.ChengZhiYa.MHDFTools.manager.init.stop.stopVault;
import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap;

public class InitManager {
    ClassToInstanceMap<Invitable> initializersOnLoad;
    ClassToInstanceMap<Invitable> initializersOnStart;
    ClassToInstanceMap<Invitable> initializersOnStop;

    public InitManager() {
        initializersOnLoad = new ImmutableClassToInstanceMap.Builder<Invitable>()
                .put(Dependencies.class, new Dependencies())
                .build();

        initializersOnStart = new ImmutableClassToInstanceMap.Builder<Invitable>()
                //Base
                .put(PluginChecker.class, new PluginChecker())

                //CommandRegister
                .put(CommandRegister.class, new CommandRegister())

                //BStats
                .put(bStats.class, new bStats())

                //BukkitEvent
                .put(BukkitEvent.class, new BukkitEvent())

                //BungeeCord
                .put(bungeeCord.class, new bungeeCord())

                //Misc
                .put(UpdateCheck.class, new UpdateCheck())
                .put(TPSCheck.class, new TPSCheck())
                .put(Menu.class, new Menu())

                //Database
                .put(Database.class, new Database())

                //Plugin Init
                .put(PlaceholderAPI.class, new PlaceholderAPI())
                .put(Vault.class, new Vault())
                .build();

        initializersOnStop = new ImmutableClassToInstanceMap.Builder<Invitable>()
                //BungeeCord
                .put(stopChannel.class, new stopChannel())

                //Plugin Init
                .put(stopPlaceholderAPI.class, new stopPlaceholderAPI())
                .put(stopVault.class, new stopVault())

                //Map Clear
                .put(stopFreeze.class, new stopFreeze())
                .build();
    }

    public void load() {
        for (Invitable invitable : initializersOnLoad.values()) {
            invitable.start();
        }
    }

    public void start() {
        for (Invitable invitable : initializersOnStart.values()) {
            invitable.start();
        }
    }

    public void stop() {
        for (Invitable invitable : initializersOnStop.values()) {
            invitable.start();
        }
    }
}
