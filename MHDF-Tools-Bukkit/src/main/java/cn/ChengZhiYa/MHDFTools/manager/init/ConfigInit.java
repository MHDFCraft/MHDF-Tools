package cn.ChengZhiYa.MHDFTools.manager.init;

import cn.ChengZhiYa.MHDFTools.exception.FileException;
import cn.ChengZhiYa.MHDFTools.exception.ResourceException;
import cn.ChengZhiYa.MHDFTools.manager.Initer;
import cn.ChengZhiYa.MHDFTools.util.config.ConfigUtil;
import cn.ChengZhiYa.MHDFTools.util.config.LangUtil;

import static cn.ChengZhiYa.MHDFTools.util.config.FileUtil.createFolder;

public final class ConfigInit implements Initer {
    @Override
    public void init() {
        try {
            createFolder(ConfigUtil.getDataFolder());
            ConfigUtil.saveDefaultConfig();
            LangUtil.saveDefaultLang();
        } catch (ResourceException | FileException e) {
            throw new RuntimeException(e);
        }
    }
}
