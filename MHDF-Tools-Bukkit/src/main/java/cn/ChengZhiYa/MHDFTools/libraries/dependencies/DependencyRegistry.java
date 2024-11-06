package cn.ChengZhiYa.MHDFTools.libraries.dependencies;

import com.google.gson.JsonElement;

public class DependencyRegistry {

    @SuppressWarnings("ConstantConditions")
    public static boolean isGsonRelocated() {
        return JsonElement.class.getName().startsWith("cn.ChengZhiYa");
    }

    public boolean shouldAutoLoad(Dependency dependency) {
        return dependency != Dependency.ASM &&
                dependency != Dependency.ASM_COMMONS &&
                dependency != Dependency.JAR_RELOCATOR;
    }
}
