package cn.ChengZhiYa.MHDFTools.manager.init.start;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.manager.init.Invitable;
import cn.ChengZhiYa.MHDFTools.utils.database.DatabaseUtil;

import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.utils.database.DatabaseUtil.initializationDatabaseData;

public class Database implements Invitable {
    @Override
    public void start() {
        if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
            initializationDatabaseData();
        } else {
            DatabaseUtil.initializationYamlData();
        }
    }
}
