package top.zsmile.core.model;

import lombok.Data;

@Data
public class IndexModel {
    private String tableName;
    private String keyName;
    /**
     * 表示该索引是否是唯一索引。若不是唯一索引，则该列的值为 1；若是唯一索引，则该列的值为 0。
     */
    private Long nonUnique;
    private Long seqInIndex;
    private String columnName;
    private String izNull;
    /**
     * 显示索引使用的类型和方法（BTREE、FULLTEXT、HASH、RTREE）。
     */
    private String indexType;
    private String comment;
    private String indexComment;
    private Long subPart;
    /**
     * columnName + subPart and exists 1+ concat
     */
    private String showColumnName;

    /**
     * 索引类型,normal,Unique
     */
    private String indexUnique;
}
