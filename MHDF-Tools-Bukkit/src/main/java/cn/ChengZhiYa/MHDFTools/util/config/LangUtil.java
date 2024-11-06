package cn.ChengZhiYa.MHDFTools.util.config;

import cn.ChengZhiYa.MHDFTools.exception.FileException;
import cn.ChengZhiYa.MHDFTools.exception.ResourceException;
import cn.ChengZhiYa.MHDFTools.util.message.ColorUtil;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

import static cn.ChengZhiYa.MHDFTools.util.config.FileUtil.saveResource;

public final class LangUtil {
    private static final File langFile = new File(ConfigUtil.getDataFolder(), "lang.yml");
    private static YamlConfiguration lang;

    /**
     * 保存初始语言文件
     */
    public static void saveDefaultLang() throws ResourceException, FileException {
        saveResource("lang.yml", "lang_zh.yml", false);
    }

    /**
     * 加载语言文件
     */
    public static void reloadLang() {
        lang = YamlConfiguration.loadConfiguration(langFile);
    }

    /**
     * 根据指定key获取语言文件对应文本
     */
    public static String i18n(String key) {
        if (lang == null) {
            reloadLang();
        }
        return ColorUtil.color(lang.getString(key));
    }
}
