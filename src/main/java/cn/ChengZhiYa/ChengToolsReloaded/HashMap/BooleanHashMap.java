package cn.ChengZhiYa.ChengToolsReloaded.HashMap;

import java.util.HashMap;

public class BooleanHashMap {
    static final HashMap<String, Boolean> Temp = new HashMap<>();

    public static void Set(String HashMapName, Boolean Value) {
        Temp.put(HashMapName, Value);
    }

    public static Boolean Get(String HashMapName) {
        return Temp.get(HashMapName);
    }

    public static void Clear() {
        try {
            Temp.clear();
        } catch (Exception ignored) {
        }
    }
}
