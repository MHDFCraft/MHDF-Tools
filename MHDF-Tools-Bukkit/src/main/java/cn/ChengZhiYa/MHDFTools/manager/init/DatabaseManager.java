package cn.ChengZhiYa.MHDFTools.manager.init;

import cn.ChengZhiYa.MHDFTools.entity.AbstractDao;
import cn.ChengZhiYa.MHDFTools.manager.interfaces.Init;
import cn.ChengZhiYa.MHDFTools.util.config.ConfigUtil;
import com.j256.ormlite.jdbc.DataSourceConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import org.reflections.Reflections;

import java.io.File;
import java.lang.reflect.Modifier;
import java.sql.SQLException;
import java.util.Objects;
import java.util.TimeZone;

@Getter
public final class DatabaseManager implements Init {
    private String type = "none";
    private String databaseUrl = "";
    private DataSourceConnectionSource connectionSource;

    /**
     * 连接并初始化数据库
     */
    @Override
    public void init() {
        String type = ConfigUtil.getConfig().getString("databaseSettings.type");

        // 加载数据库驱动
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("数据库驱动加载失败");
        }

        if (type != null) {
            // 初始化MySQL数据库的连接
            if (type.equalsIgnoreCase("mysql")) {
                this.type = "mysql";

                databaseUrl = "jdbc:mysql://" +
                        ConfigUtil.getConfig().getString("databaseSettings.mysql.host") + "/" +
                        ConfigUtil.getConfig().getString("databaseSettings.mysql.database");
                HikariConfig config = getHikariConfig(this.databaseUrl);

                config.setUsername(ConfigUtil.getConfig().getString("databaseSettings.mysql.user"));
                config.setPassword(ConfigUtil.getConfig().getString("databaseSettings.mysql.password"));

                this.connectionSource = initDataSource(config);
                initTable();
                return;
            }

            // 初始化H2数据库的连接
            if (type.equalsIgnoreCase("h2")) {
                this.type = "h2";

                String fileName = ConfigUtil.getConfig().getString("databaseSettings.h2.file");
                File file = new File(ConfigUtil.getDataFolder(), Objects.requireNonNull(fileName));
                databaseUrl = "jdbc:h2:" + file.getAbsolutePath();

                this.connectionSource = initDataSource(getHikariConfig(this.databaseUrl));
                initTable();
                return;
            }
            throw new RuntimeException("不支持的数据库类型: " + type);
        } else {
            throw new RuntimeException("数据库类型未设置");
        }
    }

    /**
     * 关闭数据库连接源
     */
    public void close() {
        try {
            this.connectionSource.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取数据库配置
     *
     * @return 数据库配置实例
     */
    public HikariConfig getHikariConfig(String databaseUrl) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(databaseUrl);
        config.addDataSourceProperty("useUnicode", "true");
        config.addDataSourceProperty("characterEncoding", "utf8");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("autoReconnect", "true");
        config.addDataSourceProperty("serverTimezone", TimeZone.getDefault().getID());
        return config;
    }

    /**
     * 根据数据库配置实例生成数据源
     *
     * @param config 数据库配置实例
     * @return 数据源
     */
    public DataSourceConnectionSource initDataSource(HikariConfig config) {
        HikariDataSource dataSource = new HikariDataSource(config);
        try {
            return new DataSourceConnectionSource(dataSource, this.databaseUrl);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 初始化表
     */
    private void initTable() {
        try {
            Reflections reflections = new Reflections(AbstractDao.class.getPackageName());

            for (Class<? extends AbstractDao> clazz : reflections.getSubTypesOf(AbstractDao.class)) {
                if (!Modifier.isAbstract(clazz.getModifiers())) {
                    AbstractDao abstractDao = clazz.getDeclaredConstructor().newInstance();
                    TableUtils.createTableIfNotExists(this.connectionSource, abstractDao.getClass());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
