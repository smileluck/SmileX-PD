package top.zsmile.core.model;

import lombok.Data;

@Data
public class IndexModel {
    private String tableName;
    private String keyName;
    private Long nonUnique;
    private Long seqInIndex;
    private String columnName;
    private String izNull;
    private String indexType;
    private String comment;
    private String indexComment;
}
