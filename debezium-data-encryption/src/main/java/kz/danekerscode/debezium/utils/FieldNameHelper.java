package kz.danekerscode.debezium.utils;

public class FieldNameHelper {

    private final static String FIELD_PATH_REGEX = "^[a-zA-Z_][a-zA-Z0-9_]*\\.[a-zA-Z_][a-zA-Z0-9_]*\\.[a-zA-Z_][a-zA-Z0-9_]*$";

    /**
     * Метод который валидирует правильность наименования формата поле
     * Ожидаемый формат schemaName.tableName.columnName
     */
    public static void validateFieldPath(String fieldPath) {
        if (!fieldPath.matches(FIELD_PATH_REGEX)) {
            throw new IllegalArgumentException("Field path format is invalid. Expected format: schemaName.tableName.columnName");
        }
    }

    /**
     * Метод который создает строку с полным путем поля
     */
    public static String constructFieldPath(String schemaName, String tableName, String columnName) {
        return schemaName + "." + tableName + "." + columnName;
    }

}