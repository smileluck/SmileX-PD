# SmileX-PD
针对数据库同步、筛选、记录、过滤、导出一体化的项目。

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

