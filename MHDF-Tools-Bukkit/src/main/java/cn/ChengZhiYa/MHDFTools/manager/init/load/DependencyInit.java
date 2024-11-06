package cn.ChengZhiYa.MHDFTools.manager.init.load;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.manager.init.Starter;
import cn.ChengZhiYa.MHDFTools.util.download.libraries.Dependency;
import cn.ChengZhiYa.MHDFTools.util.download.libraries.DependencyManager;
import cn.ChengZhiYa.MHDFTools.util.download.libraries.DependencyManagerImpl;
import cn.ChengZhiYa.MHDFTools.util.download.libraries.classpath.ReflectionClassPathAppender;

import java.util.HashSet;
import java.util.Set;

public class DependencyInit implements Starter {

    @Override
    public void init() {
        DependencyManager dependencyManager = new DependencyManagerImpl(
                new ReflectionClassPathAppender(PluginLoader.INSTANCE.getPlugin().getClass().getClassLoader())
        );

        Set<Dependency> dependencyList = new HashSet<>();
        dependencyList.add(Dependency.FAST_JSON);
        dependencyList.add(Dependency.PacketEvent);

        dependencyManager.loadDependencies(dependencyList);
    }
}
