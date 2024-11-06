package cn.ChengZhiYa.MHDFTools.util.libraries.dependencies.relocation;

import cn.ChengZhiYa.MHDFTools.util.libraries.dependencies.Dependency;
import cn.ChengZhiYa.MHDFTools.util.libraries.dependencies.DependencyManager;
import cn.ChengZhiYa.MHDFTools.util.libraries.dependencies.classloader.DependencyClassLoader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.*;

public final class RelocationHandler {
    public static final Set<Dependency> defaultDependencies = EnumSet.of(Dependency.ASM, Dependency.ASM_COMMONS, Dependency.JAR_RELOCATOR);

    private final Constructor<?> jarRelocatorConstructor;
    private final Method jarRelocatorRunMethod;

    public RelocationHandler(DependencyManager dependencyManager) {
        ClassLoader classLoader = null;
        try {
            dependencyManager.loadDependencies(defaultDependencies);
            classLoader = dependencyManager.obtainClassLoaderWith(defaultDependencies);

            Class<?> jarRelocatorClass = classLoader.loadClass("me.lucko.jarrelocator.JarRelocator");

            this.jarRelocatorConstructor = jarRelocatorClass.getDeclaredConstructor(File.class, File.class, Map.class);
            this.jarRelocatorConstructor.setAccessible(true);

            this.jarRelocatorRunMethod = jarRelocatorClass.getDeclaredMethod("run");
            this.jarRelocatorRunMethod.setAccessible(true);
        } catch (Exception e) {
            try {
                if (classLoader instanceof DependencyClassLoader) {
                    ((DependencyClassLoader) classLoader).close();
                }
            } catch (IOException ex) {
                e.addSuppressed(ex);
            }

            throw new RuntimeException(e);
        }
    }

    public void remap(Path input, Path output, List<Relocation> relocations) throws Exception {
        Map<String, String> mappings = new HashMap<>();
        for (Relocation relocation : relocations) {
            mappings.put(relocation.getPattern(), relocation.getRelocatedPattern());
        }

        this.jarRelocatorRunMethod.invoke(
                this.jarRelocatorConstructor.newInstance(input.toFile(), output.toFile(), mappings)
        );
    }
}
