package cn.ChengZhiYa.ChengToolsReloaded.HashMap;

import java.util.HashMap;

public class StringHashMap {
    static HashMap<String, String> Temp = new HashMap<>();

    public static void Set(String HashMapName, String Value) {
        Temp.put(HashMapName, Value);
    }

    public static String Get(String HashMapName) {
        return Temp.get(HashMapName);
    }

    public static void Remove(String HashMapName) {
        Temp.remove(HashMapName);
    }

    public static void Clear() {
        Temp.clear();
    }
}
