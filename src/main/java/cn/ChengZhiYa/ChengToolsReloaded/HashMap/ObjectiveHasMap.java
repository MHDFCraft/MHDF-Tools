package cn.ChengZhiYa.ChengToolsReloaded.HashMap;

import org.bukkit.scoreboard.Objective;

import java.util.HashMap;

public final class ObjectiveHasMap {
    static final HashMap<Object, Objective> Temp = new HashMap<>();

    public static HashMap<Object, Objective> getHasMap() {
        return Temp;
    }
}
