package cn.ChengZhiYa.MHDFTools.manager.init;

import cn.ChengZhiYa.MHDFTools.Main;
import cn.ChengZhiYa.MHDFTools.libraries.Dependency;
import cn.ChengZhiYa.MHDFTools.libraries.DependencyManager;
import cn.ChengZhiYa.MHDFTools.libraries.DependencyManagerImpl;
import cn.ChengZhiYa.MHDFTools.libraries.classpath.ReflectionClassPathAppender;
import cn.ChengZhiYa.MHDFTools.manager.interfaces.Init;

import java.util.Arrays;

public final class LibrariesManager implements Init {
    /**
     * 下载并加载所有所需依赖
     */
    @Override
    public void init() {
        DependencyManager dependencyManager = new DependencyManagerImpl(
                new ReflectionClassPathAppender(Main.class.getClassLoader())
        );

        dependencyManager.loadDependencies(
                Arrays.stream(Dependency.values())
                        .filter(dependency -> dependency != Dependency.ASM &&
                                dependency != Dependency.ASM_COMMONS &&
                                dependency != Dependency.JAR_RELOCATOR)
                        .toList()
        );
    }
}
