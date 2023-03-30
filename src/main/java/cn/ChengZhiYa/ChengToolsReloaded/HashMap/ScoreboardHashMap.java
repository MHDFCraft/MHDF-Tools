package cn.ChengZhiYa.ChengToolsReloaded.HashMap;

import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;

public class ScoreboardHashMap {
    static final HashMap<String, Scoreboard> Temp = new HashMap<>();

    public static void Set(String HashMapName, Scoreboard Value) {
        Temp.put(HashMapName, Value);
    }

    public static Scoreboard Get(String HashMapName) {
        return Temp.get(HashMapName);
    }

    public static void Clear() {
        try {
            Temp.clear();
        } catch (Exception ignored) {
        }
    }
}
