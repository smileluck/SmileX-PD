package top.zsmile.core.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ResultSetUtils {
    /**
     * 需保证Result字段名称和类字段一致
     *
     * @param resultSet
     * @param clazz
     * @return
     */
    public static List convertClassList(ResultSet resultSet, Class clazz) {
        List list = new ArrayList<>();
//        Class clazz = t.getClass();

        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            log.debug("start loop metaData");
            for (int i = 1; i <= columnCount; i++) {
                log.debug(metaData.getColumnName(i) + "-" + metaData.getColumnLabel(i) + "-" + metaData.getColumnType(i));
            }
            log.debug("end loop metaData");


            Field[] fields = clazz.getDeclaredFields();
            while (resultSet.next()) {
//                T obj = (T) clazz.newInstance();
//                T obj = (T) t.getClass().newInstance();
                Object obj = clazz.newInstance();
                for (int i = 1; i <= columnCount; i++) {
                    Object resultValue = resultSet.getObject(i);
                    for (int j = 0; j < fields.length; j++) {
                        Field field = fields[j];
                        if (field.getName().equalsIgnoreCase(metaData.getColumnLabel(i))) {
                            boolean accessible = field.isAccessible();
                            field.setAccessible(true);
                            field.set(obj, resultValue);
                            field.setAccessible(accessible);
                        }
                    }
                }
                list.add(obj);
            }
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }

        return list;
    }

}
