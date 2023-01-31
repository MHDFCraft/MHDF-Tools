package cn.ChengZhiYa.ChengToolsReloaded.HashMap;

import org.bukkit.scoreboard.Objective;

import java.util.HashMap;

public class ObjectiveHashMap {
    static HashMap<String, Objective> Temp = new HashMap<>();

    public static void Set(String HashMapName, Objective Value) {
        Temp.put(HashMapName, Value);
    }

    public static Objective Get(String HashMapName) {
        return Temp.get(HashMapName);
    }

    public static void Remove(String HashMapName) {
        Temp.remove(HashMapName);
    }

    public static void Clear() {
        Temp.clear();
    }
}
