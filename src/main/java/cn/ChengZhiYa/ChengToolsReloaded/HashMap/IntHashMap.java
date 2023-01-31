package cn.ChengZhiYa.ChengToolsReloaded.HashMap;

import java.util.HashMap;

public class IntHashMap {
    static HashMap<String, Integer> Temp = new HashMap<>();

    public static void Set(String HashMapName, Integer Value) {
        Temp.put(HashMapName, Value);
    }

    public static Integer Get(String HashMapName) {
        return Temp.get(HashMapName);
    }

    public static void Remove(String HashMapName) {
        Temp.remove(HashMapName);
    }

    public static void Clear() {
        Temp.clear();
    }
}
