package cn.ChengZhiYa.MHDFTools.manager;

import lombok.Getter;
import org.bukkit.Bukkit;

@Getter
public class ServerManager {
    String version;

    public ServerManager() {
        this.version = Bukkit.getVersion().split("MC: ")[1].replace(")", "");
    }

    //    public void unSupportServer() {
//        if (!is1_16orAbove()) {
//            LogUtil.color("&e[MHDFTools] &c您的服务器版本不受我们的支持 -> " + getVersion());
//            MHDFPluginLoader.INSTANCE.disablePlugin();
//        } else {
//            LogUtil.color("&e[MHDFTools] &a您的服务器版本是受支持的 -> " + getVersion());
//        }
//    }
    public boolean is1_8() {
        return this.version.startsWith("1.8");
    }

    public boolean is1_9orAbove() {
        return !this.version.startsWith("1.7") || !this.version.startsWith("1.8");
    }

    public boolean is1_12orAbove() {
        return this.version.startsWith("1.12") || this.version.startsWith("1.13") || this.version.startsWith("1.14") || this.version.startsWith("1.15") || this.version.startsWith("1.16") || this.version.startsWith("1.17") || this.version.startsWith("1.18") || this.version.startsWith("1.19") || this.version.startsWith("1.20");
    }

    public boolean is1_13orAbove() {
        return this.version.startsWith("1.13") || this.version.startsWith("1.14") || this.version.startsWith("1.15") || this.version.startsWith("1.16") || this.version.startsWith("1.17") || this.version.startsWith("1.18") || this.version.startsWith("1.19") || this.version.startsWith("1.20");
    }

    public boolean is1_14orAbove() {
        return this.version.startsWith("1.14") || this.version.startsWith("1.15") || this.version.startsWith("1.16") || this.version.startsWith("1.17") || this.version.startsWith("1.18") || this.version.startsWith("1.19") || this.version.startsWith("1.20");
    }

    public boolean is1_15orAbove() {
        return this.version.startsWith("1.15") || this.version.startsWith("1.16") || this.version.startsWith("1.17") || this.version.startsWith("1.18") || this.version.startsWith("1.19") || this.version.startsWith("1.20");
    }

    public boolean is1_16orAbove() {
        return this.version.startsWith("1.16") || this.version.startsWith("1.17") || this.version.startsWith("1.18") || this.version.startsWith("1.19") || this.version.startsWith("1.20");
    }

    public boolean is1_17orAbove() {
        return this.version.startsWith("1.17") || this.version.startsWith("1.18") || this.version.startsWith("1.19") || this.version.startsWith("1.20");
    }

    public boolean is1_18orAbove() {
        return this.version.startsWith("1.18") || this.version.startsWith("1.19") || this.version.startsWith("1.20");
    }

    public boolean is1_19orAbove() {
        return this.version.startsWith("1.19") || this.version.startsWith("1.20");
    }

    public boolean is1_20orAbove() {
        return this.version.startsWith("1.20") || this.version.startsWith("1.21");
    }

}