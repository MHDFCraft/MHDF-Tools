package cn.ChengZhiYa.MHDFTools.utils.map;

import cn.ChengZhiYa.MHDFTools.entity.SuperLocation;
import lombok.experimental.UtilityClass;
import org.bukkit.scoreboard.Objective;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class MapUtil {
    static HashMap<Object, Boolean> booleanHashMap = new HashMap<>();
    static HashMap<Object, Integer> intHashMap = new HashMap<>();
    static HashMap<Object, SuperLocation> locationHashMap = new HashMap<>();
    static HashMap<Object, Objective> objectiveHashMap = new HashMap<>();
    static HashMap<Object, String> stringHashMap = new HashMap<>();

    public static Map<Object, Boolean> getBooleanHashMap() {
        return booleanHashMap;
    }

    public static Map<Object, Integer> getIntHashMap() {
        return intHashMap;
    }

    public static Map<Object, SuperLocation> getLocationHashMap() {
        return locationHashMap;
    }

    public static Map<Object, Objective> getObjectiveHashMap() {
        return objectiveHashMap;
    }

    public static Map<Object, String> getStringHashMap() {
        return stringHashMap;
    }
}