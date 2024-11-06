package cn.ChengZhiYa.MHDFTools.libraries.classpath;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;

public abstract class URLClassLoaderAccess {
    private final URLClassLoader classLoader;

    private URLClassLoaderAccess(URLClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public static URLClassLoaderAccess create(URLClassLoader classLoader) {
        if (Reflection.isSupported()) {
            return new Reflection(classLoader);
        } else if (Unsafe.isSupported()) {
            return new Unsafe(classLoader);
        } else {
            throw new RuntimeException("不支持该操作");
        }
    }

    public abstract void addURL(URL url);

    private static class Reflection extends URLClassLoaderAccess {
        private static final Method METHOD;

        static {
            Method method;
            try {
                method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                method.setAccessible(true);
            } catch (Exception e) {
                method = null;
            }
            METHOD = method;
        }

        Reflection(URLClassLoader classLoader) {
            super(classLoader);
        }

        private static boolean isSupported() {
            return METHOD != null;
        }

        @Override
        public void addURL(URL url) {
            try {
                METHOD.invoke(super.classLoader, url);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class Unsafe extends URLClassLoaderAccess {
        private static final sun.misc.Unsafe UNSAFE;

        static {
            sun.misc.Unsafe unsafe;
            try {
                Field unsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
                unsafeField.setAccessible(true);
                unsafe = (sun.misc.Unsafe) unsafeField.get(null);
            } catch (Throwable t) {
                unsafe = null;
            }
            UNSAFE = unsafe;
        }

        private final Collection<URL> unopenedURLs;
        private final Collection<URL> pathURLs;

        @SuppressWarnings("unchecked")
        Unsafe(URLClassLoader classLoader) {
            super(classLoader);

            Collection<URL> unopenedURLs;
            Collection<URL> pathURLs;
            try {
                Object ucp = fetchField(URLClassLoader.class, classLoader, "ucp");
                unopenedURLs = (Collection<URL>) fetchField(ucp.getClass(), ucp, "unopenedUrls");
                pathURLs = (Collection<URL>) fetchField(ucp.getClass(), ucp, "path");
            } catch (Throwable e) {
                unopenedURLs = null;
                pathURLs = null;
            }

            this.unopenedURLs = unopenedURLs;
            this.pathURLs = pathURLs;
        }

        private static boolean isSupported() {
            return UNSAFE != null;
        }

        private static Object fetchField(final Class<?> clazz, final Object object, final String name) throws NoSuchFieldException {
            Field field = clazz.getDeclaredField(name);
            long offset = UNSAFE.objectFieldOffset(field);
            return UNSAFE.getObject(object, offset);
        }

        @Override
        public void addURL(URL url) {
            if (this.unopenedURLs == null || this.pathURLs == null) {
                throw new RuntimeException("载入的地址不存在");
            }

            synchronized (this.unopenedURLs) {
                this.unopenedURLs.add(url);
                this.pathURLs.add(url);
            }
        }
    }
}
