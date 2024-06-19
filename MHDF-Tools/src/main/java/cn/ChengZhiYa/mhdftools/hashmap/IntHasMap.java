package cn.chengzhiya.mhdftools.hashmap;

import java.util.HashMap;

public final class IntHasMap {
    static final HashMap<Object, Integer> Temp = new HashMap<>();

    public static HashMap<Object, Integer> getHasMap() {
        return Temp;
    }
}
