package kz.danekerscode.jdbc;

import lombok.experimental.UtilityClass;

import java.util.Properties;

@UtilityClass
class PropertyUtil {

    private static final Properties jdbcProperties = new Properties();

    static {
        try {
            jdbcProperties.load(PropertyUtil.class.getClassLoader().getResourceAsStream("jdbc.properties"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getJdbcProperty(String key) {
        return jdbcProperties.getProperty(key);
    }
}
