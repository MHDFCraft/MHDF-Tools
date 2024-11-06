package cn.ChengZhiYa.MHDFTools.util.libraries.dependencies.classloader;

import java.net.URL;
import java.net.URLClassLoader;

public class DependencyClassLoader extends URLClassLoader {
    static {
        ClassLoader.registerAsParallelCapable();
    }

    public DependencyClassLoader(URL[] urls) {
        super(urls, ClassLoader.getSystemClassLoader().getParent());
    }

}
