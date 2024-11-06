package cn.ChengZhiYa.MHDFTools;

import cn.ChengZhiYa.MHDFTools.exception.FileException;
import cn.ChengZhiYa.MHDFTools.exception.ResourceException;
import cn.ChengZhiYa.MHDFTools.util.config.ConfigUtil;
import cn.ChengZhiYa.MHDFTools.libraries.classpath.ReflectionClassPathAppender;
import cn.ChengZhiYa.MHDFTools.libraries.dependencies.Dependency;
import cn.ChengZhiYa.MHDFTools.libraries.dependencies.DependencyManager;
import cn.ChengZhiYa.MHDFTools.libraries.dependencies.DependencyManagerImpl;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public final class Main extends JavaPlugin {
    public static Main instance;

    private static void initDependency() {
        DependencyManager dependencyManager = new DependencyManagerImpl(
                new ReflectionClassPathAppender(Main.instance.getClass().getClassLoader())
        );

        Set<Dependency> dependencyList = new HashSet<>();
        dependencyList.add(Dependency.FAST_JSON);

        dependencyManager.loadDependencies(dependencyList);
    }

    private static void initConfig() {
        try {
            ConfigUtil.saveDefaultConfig();
            ConfigUtil.saveDefaultLang();
        } catch (ResourceException | FileException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onLoad() {
        instance = this;

        initDependency();
        initConfig();
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        instance = null;
    }
}
