package cn.ChengZhiYa.MHDFTools.libraries.dependencies;

import cn.ChengZhiYa.MHDFTools.exception.FileException;
import cn.ChengZhiYa.MHDFTools.Main;
import cn.ChengZhiYa.MHDFTools.libraries.classpath.ClassPathAppender;
import cn.ChengZhiYa.MHDFTools.libraries.dependencies.classloader.DependencyClassLoader;
import cn.ChengZhiYa.MHDFTools.libraries.dependencies.relocation.Relocation;
import cn.ChengZhiYa.MHDFTools.libraries.dependencies.relocation.RelocationHandler;
import cn.ChengZhiYa.MHDFTools.util.message.LogUtil;
import com.google.common.collect.ImmutableSet;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CountDownLatch;

import static cn.ChengZhiYa.MHDFTools.util.config.FileUtil.createFolder;

public class DependencyManagerImpl implements DependencyManager {
    private final Path dependenciesFolder;
    private final DependencyRegistry registry;
    private final ClassPathAppender classPathAppender;
    private final EnumMap<Dependency, Path> loaded = new EnumMap<>(Dependency.class);
    private final Map<ImmutableSet<Dependency>, DependencyClassLoader> loaders = new HashMap<>();
    private final RelocationHandler relocationHandler;

    public DependencyManagerImpl(ClassPathAppender classPathAppender) {
        this.registry = new DependencyRegistry();
        this.dependenciesFolder = setupCacheDirectory();
        this.classPathAppender = classPathAppender;
        this.relocationHandler = new RelocationHandler(this);
    }

    private static Path setupCacheDirectory() {
        File file = new File(Main.instance.getDataFolder(), "libs");
        try {
            createFolder(file);
        } catch (FileException e) {
            throw new RuntimeException(e);
        }
        return file.toPath();
    }

    @Override
    public ClassLoader obtainClassLoaderWith(Set<Dependency> dependencies) {
        ImmutableSet<Dependency> set = ImmutableSet.copyOf(dependencies);

        for (Dependency dependency : dependencies) {
            if (!this.loaded.containsKey(dependency)) {
                throw new RuntimeException("依赖未加载: " + dependency);
            }
        }

        synchronized (this.loaders) {
            DependencyClassLoader classLoader = this.loaders.get(set);
            if (classLoader != null) {
                return classLoader;
            }

            URL[] urls = set.stream()
                    .map(this.loaded::get)
                    .map(file -> {
                        try {
                            return file.toUri().toURL();
                        } catch (MalformedURLException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toArray(URL[]::new);

            classLoader = new DependencyClassLoader(urls);
            this.loaders.put(set, classLoader);
            return classLoader;
        }
    }

    @Override
    public void loadDependencies(Collection<Dependency> dependencies) {
        CountDownLatch latch = new CountDownLatch(dependencies.size());

        for (Dependency dependency : dependencies) {
            if (this.loaded.containsKey(dependency)) {
                latch.countDown();
                continue;
            }

            try {
                LogUtil.log("正在加载依赖 " + dependency.getFileName(null));
                loadDependency(dependency);
                LogUtil.log("依赖 " + dependency.getFileName(null) + " 加载完成!");
            } catch (Throwable e) {
                throw new RuntimeException("无法下载依赖 " + dependency, e);
            } finally {
                latch.countDown();
            }
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void loadDependency(Dependency dependency) throws Exception {
        if (this.loaded.containsKey(dependency)) {
            return;
        }

        Path file = remapDependency(dependency, downloadDependency(dependency));

        this.loaded.put(dependency, file);

        if (this.classPathAppender != null && this.registry.shouldAutoLoad(dependency)) {
            this.classPathAppender.addJarToClasspath(file);
        }
    }

    private Path downloadDependency(Dependency dependency) {
        Path file = dependenciesFolder.resolve(dependency.getFileName(null));

        if (Files.exists(file)) {
            return file;
        }

        String fileName = dependency.getFileName(null);
        Repository repository = dependency.getRepository();

        LogUtil.log("正在下载依赖 " + fileName + "(" + repository.getUrl() + dependency.getMavenRepoPath() + ")");
        repository.download(dependency, file);
        LogUtil.log("依赖 " + fileName + " 下载完成!");
        return file;
    }

    private Path remapDependency(Dependency dependency, Path normalFile) throws Exception {
        List<Relocation> rules = new ArrayList<>(dependency.getRelocations());
        if (rules.isEmpty()) {
            return normalFile;
        }

        Path remappedFile = this.dependenciesFolder.resolve(
                dependency.getFileName(DependencyRegistry.isGsonRelocated() ? "remapped-legacy" : "remapped")
        );

        if (Files.exists(remappedFile)) {
            return remappedFile;
        }
        relocationHandler.remap(normalFile, remappedFile, rules);
        return remappedFile;
    }

    @Override
    public void close() {
        for (DependencyClassLoader loader : this.loaders.values()) {
            try {
                loader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
