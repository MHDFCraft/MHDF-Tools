package cn.ChengZhiYa.MHDFTools.manager;

import cn.ChengZhiYa.MHDFTools.manager.init.Starter;
import cn.ChengZhiYa.MHDFTools.manager.init.load.DependencyInit;
import cn.ChengZhiYa.MHDFTools.manager.init.load.PacketEventInit;
import cn.ChengZhiYa.MHDFTools.manager.init.start.CommandInit;
import cn.ChengZhiYa.MHDFTools.manager.init.start.ConfigInit;
import cn.ChengZhiYa.MHDFTools.manager.init.start.PluginHooks;
import cn.ChengZhiYa.MHDFTools.manager.init.stop.PacketEventUnload;
import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap;

public class InitManager {
    ClassToInstanceMap<Starter> initializersOnLoad;
    ClassToInstanceMap<Starter> initializersOnStart;
    ClassToInstanceMap<Starter> initializersOnStop;

    public InitManager() {
        initializersOnLoad = new ImmutableClassToInstanceMap.Builder<Starter>()
                //PacketEvent Init
                .put(PacketEventInit.class, new PacketEventInit())

                //Libraries Cloud Download
                .put(DependencyInit.class, new DependencyInit())

                .build();

        initializersOnStart = new ImmutableClassToInstanceMap.Builder<Starter>()
                //Config Init
                .put(ConfigInit.class, new ConfigInit())

                //Command Init
                .put(CommandInit.class, new CommandInit())

                //Plugin Hook
                .put(PluginHooks.class, new PluginHooks())

                .build();

        initializersOnStop = new ImmutableClassToInstanceMap.Builder<Starter>()
                //PacketEvent Unload
                .put(PacketEventUnload.class, new PacketEventUnload())

                .build();
    }

    public void load() {
        for (Starter Starter : initializersOnLoad.values()) {
            Starter.init();
        }
    }

    public void start() {
        for (Starter Starter : initializersOnStart.values()) {
            Starter.init();
        }
    }

    public void stop() {
        for (Starter Starter : initializersOnStop.values()) {
            Starter.init();
        }
    }
}