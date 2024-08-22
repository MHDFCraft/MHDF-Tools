package cn.ChengZhiYa.MHDFTools.manager.init.load;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.manager.init.Invitable;
import cn.ChengZhiYa.MHDFTools.utils.libraries.classpath.ReflectionClassPathAppender;
import cn.ChengZhiYa.MHDFTools.utils.libraries.dependencies.Dependency;
import cn.ChengZhiYa.MHDFTools.utils.libraries.dependencies.DependencyManager;
import cn.ChengZhiYa.MHDFTools.utils.libraries.dependencies.DependencyManagerImpl;

import java.util.ArrayList;

public class Dependencies implements Invitable {
    @Override
    public void start() {
        DependencyManager dependencyManager = new DependencyManagerImpl(new ReflectionClassPathAppender(PluginLoader.INSTANCE.getPlugin().getClass().getClassLoader()));
        java.util.List<Dependency> dependencies = new ArrayList<>();
        dependencies.add(Dependency.FAST_JSON);
        dependencies.add(Dependency.HikariCP);
        dependencies.add(Dependency.HTTPCLIENT);
        dependencies.add(Dependency.COMMONS_LANG);
        dependencyManager.loadDependencies(dependencies);
    }
}
