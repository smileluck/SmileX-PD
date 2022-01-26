package top.zsmile.core.entity.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class ReplaceTableVO {

    @ExcelProperty("原始表")
    private String fromDatabaseName;
    @ExcelProperty("原始表")
    private String fromTableName;
    @ExcelProperty("原始创建表Sql")
    private String fromTableSql;
    @ExcelProperty("目标表")
    private String toDatabaseName;
    @ExcelProperty("目标表")
    private String toTableName;
    @ExcelProperty("目标创建表Sql")
    private String toTableSql;
}
