package top.zsmile.core.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class TablesModel {
    private String tableName;

    private String tableComment;

    private String engine;

    private List<ColumnsModel> columnsModelList;

    private List<IndexModel> indexModelList;

    /**
     * 缺少字段
     */
    private String missField;
}
