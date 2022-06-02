# SmileX-PD
针对数据库同步、筛选、记录、过滤、导出一体化的项目。

---
- [功能](#功能)
- [使用说明](#使用说明)
---

## 背景
最近开始搭建数据仓库上的一些东西，需要做一些比如数据库结构导出、过滤表和合并表，更换表名的功能。正好借此机会把这个项目启动起来，之前零零碎碎的小工具和设想也融入到了里面。


## 功能
### 文档支持
- [x] word
- [ ] html
- [x] markdown

### 数据库支持
- [x] Mysql
- [x] MariaDB
- [ ] Oracle
- [ ] SqlServer
- [ ] TiDB
- [ ] PostgreSql

### 功能支持
- [ ] 管理面板
- [ ] 筛查器
    - [ ] 缺乏字段筛选指定字段名和类型
    - [ ] 筛查表是否没有主键或唯一索引
- [x] 过滤器
    - [x] 表过滤器
    - [x] 字段过滤器
- [x] 转换器
    - 表名转换
    - 转换记录导出
- [x] 数据库设计文档导出
    - [x] 索引导出
    - [x] 表结构导出
    - [ ] 外键导出
    - [ ] 缺乏字段导出
- [ ] 日志记录
    - [ ] 操作日志记录
    - [ ] 操作回滚记录
- [ ] 结构同步器
    - [x] 库表合并新库
        - [x] 现在是通过删除创建语句中的外键和索引。
        - [ ] 看有没有时间改个简易的解析器，方便更保险的操作。
- [ ] Api文档
- [ ] 数据结构操作 
    - [x] 添加字段
    - [x] 删除字段
    - [x] 删除索引
    - [ ] 删除外键
- [ ] 数据源
    - [x] 数据库连接池
    - [ ] 多数据源
    - [ ] 动态配置
- [ ] 关系关联记录
- [x] canal
    - [x] mysql订阅


## 说明 
### about ProcessFilter
- 忽略表和指定表都配置时：
    1. 当指定表和忽略表都符合条件则指定表优先，进行保留
    2. 当仅指定表符合条件时，则保留，其余表丢弃。
    3. 当仅忽略表符合条件时，则丢弃，其余表保留。
- 仅忽略表配置时：符合条件的丢弃，其余保留
- 仅指定表配置时：符合条件的保留，其余丢弃


## ftl数据结构
```json
{
    "tables":[
        { // TablesModel
            "columnsModelList":[ //ColumnsModel
                {
                    "columnComment":"字段注释",
                    "columnKey":"字段类型，PRI为主键",
                    "columnName":"字段名称",
                    "columnType":"字段数据信息",
                    "dataLength":"字段数据长短",
                    "dataType":"字段数据类型",
                    "extra":"",
                    "isNullable":"是否为空",
                    "tableName":"所属表"
                },
            ],
            "engine":"表引擎",
            "tableComment":"表注释",
            "tableName":"表名称"
        }
    ],
    "db":{ // databaseModel
        "describe":"数据库介绍",
        "name":"文档名称",
        "version":"版本信息"
    }
}
```

## 使用说明

> 这里主要使用的是Core模块，其它模块只是一些相关框架的使用而已，无需理会。

修改core模块下的hikaricp.properties，连接自己想要生成文档的数据库

### Demo(参考)

```java
// 获取数据库连接
MysqlDataQuery mysqlQuery = new MysqlDataQuery();

// 获取所有表模型
List<TablesModel> tablesModels = mysqlQuery.queryTables(databaseName);
// 获取所有字段模型
List<ColumnsModel> columnsModels = mysqlQuery.queryColumns(databaseName);
// 使用模型工具合并字段
List mergeList = ModelUtils.mergeTableAndColumn(tablesModels, columnsModels);
// 获取所有索引模型
List<IndexModel> indexModels = mysqlQuery.queryIndex(databaseName);
// 使用模型工具合并索引
mergeList = ModelUtils.mergeTableAndIndex(mergeList, indexModels);

// 数据库文档信息
DatabaseModel databaseModel = new DatabaseModel("演示数据库","演示数据库介绍","演示数据库版本");

// 创建FTL数据模型
HashMap<String, Object> dataMap = new HashMap<>();
// 填入表模型
dataMap.put("tables", mergeList);
// 填入数据库文档模型
dataMap.put("db", databaseModel);


// 生成markdown
try {
    Template template = FreemakerConfig.INSTANCE.getTemplate("document-md.ftl");
    Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("doc/processDoc/example.md")), "UTF-8"));
    //FreeMarker使用Word模板和数据生成Word文档
    template.process(dataMap, out);
} catch (Exception e) {
    e.printStackTrace();
}


// 生成word
try {
    Template template = FreemakerConfig.INSTANCE.getTemplate("document-word.ftl");
    Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("doc/processDoc/example.docx")), "UTF-8"));
    //FreeMarker使用Word模板和数据生成Word文档
    template.process(dataMap, out);
} catch (Exception e) {
    e.printStackTrace();
}

```



### 主要类

#### DataQuery(数据库查询)

> 数据库查询。已实现mysql数据库查询

```java
public interface DataQuery {
    /**
     * 查询表信息
     *
     * @param databaseName
     * @return
     */
    List<TablesModel> queryTables(String databaseName);

    /**
     * 查询表结构信息
     *
     * @param databaseName
     */
    List<ColumnsModel> queryColumns(String databaseName);

    /**
     * 查询建表语句
     *
     * @param tableName
     */
    String queryCreateTableSql(String databaseName, String tableName);

    /**
     * 查询索引
     */
    List<IndexModel> queryIndex(String databaseName, String tableName);

    /**
     * 查询db所有索引
     */
    List<IndexModel> queryIndex(String databaseName);

    /**
     * 执行sql
     */
    void querySql(String sql);
}
```

#### ModelUtils(模型工具)

> 模型工具。用来处理模型

```java
public class ModelUtils {

    /**
     * 合并表和字段
     * @param tableList
     * @param columnList
     * @return
     */
    public static List mergeTableAndColumn(List<TablesModel> tableList, List<ColumnsModel> columnList) {
        Map<String, List<ColumnsModel>> collect = columnList.stream().collect(Collectors.groupingBy(ColumnsModel::getTableName));
        for (TablesModel tablesModel : tableList) {
            String tableName = tablesModel.getTableName();
            List<ColumnsModel> columnsModels = collect.get(tableName);
            if (columnsModels != null) {
                tablesModel.setColumnsModelList(columnsModels);
                continue;
            }
        }
        return tableList;
    }


    /**
     * 合并表和索引
     * @param tableList
     * @param indexModelList
     * @return
     */
    public static List mergeTableAndIndex(List<TablesModel> tableList, List<IndexModel> indexModelList) {
        Map<String, List<IndexModel>> collect = indexModelList.stream().collect(Collectors.groupingBy(IndexModel::getTableName));
        for (TablesModel tablesModel : tableList) {
            String tableName = tablesModel.getTableName();
            List<IndexModel> indexModels = collect.get(tableName);
            if (indexModels != null) {
                tablesModel.setIndexModelList(indexModels);
                continue;
            }
        }
        return tableList;
    }

    /**
     * same key-name merge more field to one
     *
     * @return
     */
    public static List mergeTableIndex(List<IndexModel> indexModels) {
        List<IndexModel> mergeResult = indexModels.parallelStream().collect(Collectors.toMap(IndexModel::getKeyName, a -> a, (o1, o2) -> {
            o1.setShowColumnName(o1.getShowColumnName() + "," + o2.getShowColumnName());
            return o1;
        })).values().stream().collect(Collectors.toList());
        return mergeResult;
    }

}
```