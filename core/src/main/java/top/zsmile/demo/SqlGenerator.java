package top.zsmile.demo;

import com.google.common.collect.Lists;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import javax.xml.bind.annotation.XmlElement;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlGenerator {
    private static Map<String, String> javaTypeCast = new HashMap<>();
    private static Map<String, String> filedMap;
    private static String symbol = ",";
    private static String space = "\n";


    static {
        space = "\n";
        symbol = ",";
        filedMap = new HashMap<>();
        javaTypeCast.put("Integer", "smallint");
        javaTypeCast.put("java.lang.Integer", "smallint");
        javaTypeCast.put("Short", "tinyint");
        javaTypeCast.put("Long", "bigint(20)");
        javaTypeCast.put("java.lang.Long", "bigint(20)");
        javaTypeCast.put("BigDecimal", "decimal(10,2)");
        javaTypeCast.put("java.math.BigDecimal", "decimal(10,2)");
//        javaTypeCast.put("Double", "");
        javaTypeCast.put("Float", "float");
        javaTypeCast.put("Boolean", "smallint");
        javaTypeCast.put("Timestamp", "datetime");
        javaTypeCast.put("LocalDateTime", "datetime");
        javaTypeCast.put("java.time.LocalDateTime", "datetime");
        javaTypeCast.put("java.lang.String", "VARCHAR(255)");
        javaTypeCast.put("String", "VARCHAR(255)");
        javaTypeCast.put("Date", "datetime");
    }

    //
    public static String createTable(Class clazz) throws IOException {

        Field[] fields = null;
        fields = clazz.getDeclaredFields();
        String param = null;
        String column = null;
        Class annotationType = null;
        XmlElement xmlElement = null;
//        TableName annotation = (TableName) clazz.getAnnotation(TableName.class);
        StringBuilder stb = new StringBuilder(50);
//        String tableName = annotation.value();
//        if (tableName == null || tableName.equals("")) {
//            // 未传表明默认用类名
//            tableName = clazz.getName();
//            tableName = tableName.substring(tableName.lastIndexOf(".") + 1);
//        }
//
//        stb.append("create table ").append(tableName).append(" (\r\n");
//        Map<String, String> map = getAnnoValue(clazz);
//        ArrayList<Object> counter = Lists.newArrayList();
//
//        map.entrySet().stream().forEach(op -> {
//            if (!op.getKey().equalsIgnoreCase("serialVersionUID")) {
//                counter.add(op.getKey());
////                symbol = counter.size() - 1 == map.entrySet().size() - 1 ? "" : symbol;
//                stb.append(humpToLineName(op.getKey()) + " " + javaTypeCast.get(op.getValue()).concat(symbol));
//                stb.append("\r\n");
//            }
//
//        });

        String sql = null;
        sql = stb.toString();

        // 去掉最后一个逗号
        int lastIndex = sql.lastIndexOf(",");
        sql = sql.substring(0, lastIndex) + sql.substring(lastIndex + 1);
        sql = sql                + ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;\r\n";

        return sql;
    }

    public static Map<String, String> getAnnoValue(Class clazz) {
        List<Field> fields = Arrays.asList(clazz.getDeclaredFields());
        for (Field opt : fields) {
            String value;
            String name = null;
            name = opt.getType().getName();
//                    name = name.substring(10, name.length());
//            TableId tableId = opt.getAnnotation(TableId.class);
//            if (tableId == null) {
//                TableField tableField = opt.getAnnotation(TableField.class);
//                if (tableField != null && tableField.exist()) {
//                    value = tableField.value();
//                } else {
//                    if (tableField != null && !tableField.exist()) {
//                        continue;
//                    }
//                    value = opt.getName();
//                }
//            } else {
//                value = opt.getName();
//            }
//            filedMap.put(value, name);
        }
        return filedMap;
    }

    public static void main(String[] args) throws IOException {

//        Map<String, Class> classByAnnotation = getClassByAnnotation(" com/amiba/heitan/mall/admin/entity/*.class", TableName.class);

//        Set<Map.Entry<String, Class>> entries = classByAnnotation.entrySet();
//        for (Map.Entry<String, Class> key : entries) {
//            if(key.getKey().contains("KbMessage")) {
////                System.out.println("======" + key.getKey());
//                String table = createTable(key.getValue());
//                System.out.println(table);
//                System.out.println("\n\n\n");
//            }
//        }

    }

    private static Pattern humpPattern = Pattern.compile("[A-Z]");

    public static String humpToLineName(String fieldName) {
        String UNDERSCORE = "_";
//            String res = columnNameMap.computeIfAbsent(fieldName, k -> {
        StringBuffer sb = new StringBuffer();
        Matcher matcher = humpPattern.matcher(fieldName);
        while (matcher.find()) {
            if (matcher.start() > 0) {
                matcher.appendReplacement(sb, UNDERSCORE + matcher.group(0).toLowerCase());
            } else {
                matcher.appendReplacement(sb, matcher.group(0));
            }
        }
        matcher.appendTail(sb);
        return sb.toString().toLowerCase().intern();
//            });
//            return res;
    }


    /**
     * 查找包下面的类
     * 规则 top.zsmile\**\*.class
     *
     * @param searchPath 路径，支持ANT
     * @return
     */
    public static Map<String, Class> getClassByAnnotation(String searchPath, Class annotationClass) throws IOException {
        Map<String, Class> handlerMap = new HashMap<String, Class>();
        //spring工具类，可以获取指定路径下的全部类
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        try {
            String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                    searchPath;
            Resource[] resources = resourcePatternResolver.getResources(pattern);
            //MetadataReader 的工厂类
            MetadataReaderFactory readerfactory = new CachingMetadataReaderFactory(resourcePatternResolver);
            for (Resource resource : resources) {
                //用于读取类信息
                MetadataReader reader = readerfactory.getMetadataReader(resource);
                //扫描到的class
                String classname = reader.getClassMetadata().getClassName();
                // 记载class类
                Class<?> clazz = Class.forName(classname);
                //判断是否有指定注解
                if (annotationClass != null) {
                    Annotation annotation = clazz.getAnnotation(annotationClass);
                    if (annotation != null) {
                        //将注解中的类型值作为key，对应的类作为 value
                        handlerMap.put(classname, clazz);
                    }
                } else {
                    handlerMap.put(classname, clazz);
                }
            }
            return handlerMap;
        } catch (IOException | ClassNotFoundException e) {
            throw new IOException("找不到指定Class");
        }
    }

}
