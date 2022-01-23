package top.zsmile.core.model;

import lombok.Data;

@Data
public class QueryModel {
    private String databaseName;
    private String tableName;
    private String columnName;
}
