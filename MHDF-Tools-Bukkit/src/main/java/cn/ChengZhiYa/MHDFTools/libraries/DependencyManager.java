package cn.ChengZhiYa.MHDFTools.libraries;

import java.util.Collection;
import java.util.Set;

public interface DependencyManager extends AutoCloseable {
    void loadDependencies(Collection<Dependency> dependencies);

    ClassLoader obtainClassLoaderWith(Set<Dependency> dependencies);

    @Override
    void close();
}
