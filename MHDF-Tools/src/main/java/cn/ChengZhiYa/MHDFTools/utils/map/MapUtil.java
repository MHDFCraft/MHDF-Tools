package cn.ChengZhiYa.MHDFTools.utils.map;

import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class MapUtil {
    static HashMap<Object, Boolean> booleanHasMap = new HashMap<>();
    static HashMap<Object, Integer> intHasMap = new HashMap<>();
    static HashMap<Object, Location> locationHasMap = new HashMap<>();
    static HashMap<Object, Objective> objectiveHasMap = new HashMap<>();
    static HashMap<Object, Scoreboard> scoreboardHasMap = new HashMap<>();
    static HashMap<Object, String> stringHasMap = new HashMap<>();

    public static Map<Object, Boolean> getBooleanHasMap() {
        return booleanHasMap;
    }

    public static Map<Object, Integer> getIntHasMap() {
        return intHasMap;
    }

    public static Map<Object, Location> getLocationHasMap() {
        return locationHasMap;
    }

    public static Map<Object, Objective> getObjectiveHasMap() {
        return objectiveHasMap;
    }

    public static Map<Object, Scoreboard> getScoreboardHasMap() {
        return scoreboardHasMap;
    }

    public static Map<Object, String> getStringHasMap() {
        return stringHasMap;
    }
}