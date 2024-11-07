package cn.ChengZhiYa.MHDFTools.manager.init;

import cn.ChengZhiYa.MHDFTools.exception.FileException;
import cn.ChengZhiYa.MHDFTools.exception.ResourceException;
import cn.ChengZhiYa.MHDFTools.manager.interfaces.Init;
import cn.ChengZhiYa.MHDFTools.util.config.ConfigUtil;
import cn.ChengZhiYa.MHDFTools.util.config.FileUtil;
import cn.ChengZhiYa.MHDFTools.util.config.LangUtil;

public final class ConfigManager implements Init {
    /**
     * 初始化默认配置文件
     */
    @Override
    public void init() {
        try {
            FileUtil.createFolder(ConfigUtil.getDataFolder());
            ConfigUtil.saveDefaultConfig();
            LangUtil.saveDefaultLang();
        } catch (ResourceException | FileException e) {
            throw new RuntimeException(e);
        }
    }
}
