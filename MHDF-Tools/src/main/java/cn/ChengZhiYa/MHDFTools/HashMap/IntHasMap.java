package cn.ChengZhiYa.MHDFTools.HashMap;

import java.util.HashMap;

public final class IntHasMap {
    static final HashMap<Object, Integer> Temp = new HashMap<>();

    public static HashMap<Object, Integer> getHasMap() {
        return Temp;
    }
}
