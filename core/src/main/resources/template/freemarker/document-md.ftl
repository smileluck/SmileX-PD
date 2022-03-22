[toc]

---

# 设计结构

| # | 表名 | 引擎 | 表说明  |
| --- | --- | --- | --- |
<#list tables as table>
| ${table_index+1} | <a href="#${table.tableName}">${table.tableName}</a> | ${table.engine} | ${table.tableComment?replace("\r\n","")} |
</#list>

# 表结构说明
<#list tables as table>

## <a name="${table.tableName}">${table.tableName}</a>

- 表说明：${table.tableComment?replace("\r\n","")}
- 字段筛查：${table.missField!""}
- 描述：${table.tableDesc!""}

| 字段名 | 类型 | 长度 | 允许空 | 主键 | 默认值 | 说明 |
| ------ | ---- | ---- | ------ | ---- | ------ | ---- |
<#if table.columnsModelList ??>
<#list table.columnsModelList as column >
|${column.columnName} |   ${column.dataType}   |   ${column.dataLength!""}   |    ${column.isNullable}    |   ${column.columnKey}   |    ${column.columnDefault!""}    |   ${column.columnComment}   |
</#list>
</#if>

</#list>



