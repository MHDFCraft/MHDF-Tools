package cn.ChengZhiYa.MHDFTools.manager.init.start;

import cn.ChengZhiYa.MHDFTools.manager.init.Starter;
import cn.ChengZhiYa.MHDFTools.util.config.ConfigUtil;
import cn.ChengZhiYa.MHDFTools.util.download.exception.FilesUtil;
import cn.ChengZhiYa.MHDFTools.util.download.exception.ResourceUtil;

public class ConfigInit implements Starter {

    @Override
    public void init() {
        try {
            ConfigUtil.saveDefaultConfig();
            ConfigUtil.saveDefaultLang();
        } catch (ResourceUtil | FilesUtil e) {
            throw new RuntimeException(e);
        }
    }
}
