package cn.ChengZhiYa.MHDFTools.manager;

import lombok.Getter;
import org.bukkit.Bukkit;

@Getter
public class ServerManager {
    private final String version;

    public ServerManager() {
        version = Bukkit.getVersion().split("MC: ")[1].replace(")", "");
    }

    public boolean is1_8() {
        return version.startsWith("1.8");
    }

    public boolean is1_9orAbove() {
        return !version.startsWith("1.7") || !version.startsWith("1.8");
    }

    public boolean is1_12orAbove() {
        return version.startsWith("1.12") || version.startsWith("1.13") || version.startsWith("1.14") || version.startsWith("1.15") || version.startsWith("1.16") || version.startsWith("1.17") || version.startsWith("1.18") || version.startsWith("1.19") || version.startsWith("1.20");
    }

    public boolean is1_13orAbove() {
        return version.startsWith("1.13") || version.startsWith("1.14") || version.startsWith("1.15") || version.startsWith("1.16") || version.startsWith("1.17") || version.startsWith("1.18") || version.startsWith("1.19") || version.startsWith("1.20");
    }

    public boolean is1_14orAbove() {
        return version.startsWith("1.14") || version.startsWith("1.15") || version.startsWith("1.16") || version.startsWith("1.17") || version.startsWith("1.18") || version.startsWith("1.19") || version.startsWith("1.20");
    }

    public boolean is1_15orAbove() {
        return version.startsWith("1.15") || version.startsWith("1.16") || version.startsWith("1.17") || version.startsWith("1.18") || version.startsWith("1.19") || version.startsWith("1.20");
    }

    public boolean is1_16orAbove() {
        return version.startsWith("1.16") || version.startsWith("1.17") || version.startsWith("1.18") || version.startsWith("1.19") || version.startsWith("1.20");
    }

    public boolean is1_17orAbove() {
        return version.startsWith("1.17") || version.startsWith("1.18") || version.startsWith("1.19") || version.startsWith("1.20");
    }

    public boolean is1_18orAbove() {
        return version.startsWith("1.18") || version.startsWith("1.19") || version.startsWith("1.20");
    }

    public boolean is1_19orAbove() {
        return version.startsWith("1.19") || version.startsWith("1.20");
    }

    public boolean is1_20orAbove() {
        return version.startsWith("1.20") || version.startsWith("1.21");
    }

}