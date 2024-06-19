package cn.chengzhiya.mhdftools.hashmap;


import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;

public final class ScoreboardHasMap {
    static final HashMap<Object, Scoreboard> Temp = new HashMap<>();

    public static HashMap<Object, Scoreboard> getHasMap() {
        return Temp;
    }
}
