package top.zsmile.core.model;

import lombok.Data;

@Data
public class ColumnsModel {
    private String tableName;
    private String columnName;
    private String columnType;
    private String dataType;
    private String isNullable;
    private String dataLength;
    private String dataPoint;
    /**
     * pri表示主键
     */
    private String columnKey;
    private String extra;

    private String columnDefault;
    private String columnComment;
}
