package top.zsmile.core.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class TablesModel {
    private String tableName;

    private String tableComment;

    private String tableDesc;

    private String engine;

    private List<ColumnsModel> columnsModelList;

    private List<IndexModel> indexModelList;

    /**
     * 缺少字段
     */
    private String missField;
    private List<String> missFieldList;
    /**
     * 是否有主键或唯一索引
     */
    private Boolean haveUnique;
}
