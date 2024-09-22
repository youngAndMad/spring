package kz.danekerscode.jpareaderwriterds.config;

import org.springframework.util.Assert;

public class DatasourceTypeContextHolder {
    private final static ThreadLocal<DatasourceType> contextHolder = new ThreadLocal<>();

    static {
        setDatasourceType(DatasourceType.READER);
    }

    public static void setDatasourceType(DatasourceType datasourceType) {
        Assert.notNull(datasourceType, "DatasourceType cannot be null");
        contextHolder.set(datasourceType);
    }

    public static DatasourceType getDatasourceType() {
        return contextHolder.get();
    }

    public static void clear() {
        contextHolder.remove();
    }
}
