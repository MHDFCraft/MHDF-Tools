package cn.ChengZhiYa.MHDFTools.manager.init;

import cn.ChengZhiYa.MHDFTools.exception.FileException;
import cn.ChengZhiYa.MHDFTools.exception.ResourceException;
import cn.ChengZhiYa.MHDFTools.manager.Initer;
import cn.ChengZhiYa.MHDFTools.util.config.ConfigUtil;
import cn.ChengZhiYa.MHDFTools.util.config.FileUtil;
import cn.ChengZhiYa.MHDFTools.util.config.LangUtil;

public final class ConfigManager implements Initer {
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
