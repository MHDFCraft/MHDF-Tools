package cn.ChengZhiYa.MHDFTools.util.config;

import cn.ChengZhiYa.MHDFTools.exception.ResourceException;
import cn.ChengZhiYa.MHDFTools.util.message.ColorUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class LangUtil {
    private static final File langFile = new File(ConfigUtil.getDataFolder(), "lang.yml");
    private static YamlConfiguration lang;

    /**
     * 保存初始语言文件
     */
    public static void saveDefaultLang() throws ResourceException {
        FileUtil.saveResource("lang.yml", "lang_zh.yml", false);
        reloadLang();
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
    public static @NotNull String getString(String key) {
        if (lang == null) {
            reloadLang();
        }
        String value = lang.getString(key);
        return value != null ? value : "";
    }

    /**
     * 根据指定key获取语言文件对应文本并处理颜色
     */
    public static @NotNull String i18n(String key) {
        if (lang == null) {
            reloadLang();
        }
        return ColorUtil.color(getString(key));
    }

    /**
     * 获取指定key下的keys
     */
    public static @NotNull Set<String> getKeys(String key) {
        if (lang == null) {
            reloadLang();
        }
        return Objects.requireNonNull(lang.getConfigurationSection(key)).getKeys(false);
    }

    /**
     * 获取所有命令帮助
     *
     * @return 命令帮助文本
     */
    public static @NotNull String getHelpList(String commandKey) {
        StringBuilder stringBuilder = new StringBuilder();

        List<String> keys = new ArrayList<>(LangUtil.getKeys("commands." + commandKey + ".subCommands"));
        for (String key : keys) {
            stringBuilder.append(
                    LangUtil.i18n("commands." + commandKey + ".subCommands.help.commandFormat")
                            .replace("{usage}",
                                    LangUtil.i18n("commands." + commandKey + ".subCommands." + key + ".usage")
                            )
                            .replace("{description}",
                                    LangUtil.i18n("commands." + commandKey + ".subCommands." + key + ".description")
                            )
            );
            if (!key.equals(keys.get(keys.size() - 1))) {
                stringBuilder.append("\n");
            }
        }
        return stringBuilder.toString();
    }
}
