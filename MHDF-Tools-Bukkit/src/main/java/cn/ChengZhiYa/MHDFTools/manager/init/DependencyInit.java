package cn.ChengZhiYa.MHDFTools.manager.init;

import cn.ChengZhiYa.MHDFTools.Main;
import cn.ChengZhiYa.MHDFTools.libraries.Dependency;
import cn.ChengZhiYa.MHDFTools.libraries.DependencyManager;
import cn.ChengZhiYa.MHDFTools.libraries.DependencyManagerImpl;
import cn.ChengZhiYa.MHDFTools.libraries.classpath.ReflectionClassPathAppender;
import cn.ChengZhiYa.MHDFTools.manager.Initer;

import java.util.HashSet;
import java.util.Set;

public final class DependencyInit implements Initer {
    @Override
    public void init() {
        DependencyManager dependencyManager = new DependencyManagerImpl(
                new ReflectionClassPathAppender(Main.instance.getClass().getClassLoader())
        );

        Set<Dependency> dependencyList = new HashSet<>();
        dependencyList.add(Dependency.FAST_JSON);
        dependencyList.add(Dependency.PacketEvent);

        dependencyManager.loadDependencies(dependencyList);
    }
}
