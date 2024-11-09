package cn.ChengZhiYa.MHDFTools.util.config;

import cn.ChengZhiYa.MHDFTools.exception.ResourceException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public final class SoundUtil {
    private static final File soundFile = new File(ConfigUtil.getDataFolder(), "sound.yml");
    private static YamlConfiguration sound;

    /**
     * 保存初始音效文件
     */
    public static void saveDefaultSound() throws ResourceException {
        FileUtil.saveResource("sound.yml", "sound_zh.yml", false);
        reloadSound();
    }

    /**
     * 加载音效文件
     */
    public static void reloadSound() {
        sound = YamlConfiguration.loadConfiguration(soundFile);
    }

    /**
     * 根据指定key获取对应音效
     */
    public static @NotNull String getSound(String key) {
        if (sound == null) {
            reloadSound();
        }
        String value = sound.getString(key);
        return value != null ? value : "";
    }
}
