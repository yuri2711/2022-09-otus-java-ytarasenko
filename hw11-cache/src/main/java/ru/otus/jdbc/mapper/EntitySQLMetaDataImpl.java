package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private final String selectAllSql;
    private final String selectByIdSql;
    private final String insertSql;
    private final String updateSql;


    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
        String className = entityClassMetaData.getName();
        String idFieldName = entityClassMetaData.getIdField().getName();
        List<Field> fields = entityClassMetaData.getFieldsWithoutId();

        this.selectAllSql = "SELECT * FROM " + className;

        this.selectByIdSql = "SELECT * FROM " + className
                + " WHERE " + idFieldName + " = ?";

        this.insertSql = "INSERT INTO " + className
                + fields.stream()
                        .map(Field::getName)
                        .collect(Collectors.joining(",", "(", ")"))
                + " VALUES (?" + (fields.size() > 1 ? ", ?".repeat(fields.size() - 1) + ")" : ")");

        this.updateSql = "UPDATE " + className
                + " SET " + fields.stream()
                        .map(field -> field.getName() + " = ?")
                        .collect(Collectors.joining(","))
                + " WHERE ID = " + idFieldName;
    }

    @Override
    public String getSelectAllSql() {
        return selectAllSql;
    }

    @Override
    public String getSelectByIdSql() {
        return selectByIdSql;
    }

    @Override
    public String getInsertSql() {
        return insertSql;
    }

    @Override
    public String getUpdateSql() {
        return updateSql;
    }
}
