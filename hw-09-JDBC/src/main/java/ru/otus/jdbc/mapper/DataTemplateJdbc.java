package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<?> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<?> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        final Constructor<?> constructor = entityClassMetaData.getConstructor();
        final List<Field> fields = entityClassMetaData.getAllFields();
        String selectByIdSql = entitySQLMetaData.getSelectByIdSql();

        return dbExecutor.executeSelect(connection, selectByIdSql, List.of(id), rs -> {
            try {
                T obj = null;
                if (rs.next()) {
                    obj = createInstance(fields, constructor, rs);
                }
                return obj;
            } catch (SQLException | ReflectiveOperationException e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        final Constructor<?> constructor = entityClassMetaData.getConstructor();
        final List<Field> fields = entityClassMetaData.getAllFields();

        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
            var result = new ArrayList<T>();
            try {
                while (rs.next()) {
                    result.add(createInstance(fields, constructor, rs));
                }
                return result;
            } catch (SQLException | ReflectiveOperationException e) {
                throw new DataTemplateException(e);
            }
        }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T object) {
        List<Field> fields = entityClassMetaData.getFieldsWithoutId();
        List<Object> params = new ArrayList<>();

        try {
            for (var field : fields) {
                field.setAccessible(true);
                params.add(field.get(object));
            }
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), params);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T object) {
        List<Field> fields = entityClassMetaData.getFieldsWithoutId();
        List<Object> params = new ArrayList<>();

        try {
            for (var field : fields) {
                field.setAccessible(true);
                params.add(field.get(object));
            }
            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), params);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }


    /* чтобы не трогать приватные поля, можно доставать значения через геттер */
    /*private Object getFieldValue(Field field, Object obj) {
        for (Method method : obj.getClass().getDeclaredMethods()) {
            if ((method.getName().startsWith("get") || method.getName().startsWith("is"))
                    && method.getName().toLowerCase().endsWith(field.getName().toLowerCase())) {
                try {
                    return method.invoke(obj);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }*/

    private T createInstance(List<Field> fields, Constructor<?> constructor, ResultSet rs) throws ReflectiveOperationException, SQLException {

        Object obj = constructor.newInstance();

        for (var field : fields) {
            field.setAccessible(true);
            field.set(obj, rs.getObject(field.getName()));
            /* чтобы не трогать приватные поля, можно задавать значения через сеттер */
            /*for (Method method : obj.getClass().getDeclaredMethods()) {
                if ((method.getName().startsWith("set")) && method.getName().toLowerCase().endsWith(field.getName().toLowerCase())) {
                    try {
                        method.invoke(obj, rs.getObject(field.getName()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }*/
        }
        return (T) obj;
    }
}
