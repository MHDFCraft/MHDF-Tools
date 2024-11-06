package cn.ChengZhiYa.MHDFTools.manager;

import lombok.Getter;
import org.bukkit.Bukkit;

@Getter
public final class VersionManager {
    private final String version = Bukkit.getVersion().split("MC: ")[1]
            .replace(")", "");

    private boolean isVersionAtLeast(String versionPrefix) {
        return version.matches("^" + versionPrefix + "(\\.\\d+)?(\\..+)?$");
    }

    public boolean is1_8() {
        return version.startsWith("1.8");
    }

    public boolean is1_9orAbove() {
        return isVersionAtLeast("1.9");
    }

    public boolean is1_12orAbove() {
        return isVersionAtLeast("1.12");
    }

    public boolean is1_13orAbove() {
        return isVersionAtLeast("1.13");
    }

    public boolean is1_14orAbove() {
        return isVersionAtLeast("1.14");
    }

    public boolean is1_15orAbove() {
        return isVersionAtLeast("1.15");
    }

    public boolean is1_16orAbove() {
        return isVersionAtLeast("1.16");
    }

    public boolean is1_17orAbove() {
        return isVersionAtLeast("1.17");
    }

    public boolean is1_18orAbove() {
        return isVersionAtLeast("1.18");
    }

    public boolean is1_19orAbove() {
        return isVersionAtLeast("1.19");
    }

    public boolean is1_20orAbove() {
        return isVersionAtLeast("1.20");
    }
}
