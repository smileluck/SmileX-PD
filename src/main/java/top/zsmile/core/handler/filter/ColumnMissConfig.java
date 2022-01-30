package top.zsmile.core.handler.filter;

import lombok.Builder;
import lombok.Data;
import top.zsmile.core.model.ColumnsModel;

import java.util.List;

@Data
@Builder
public class ColumnMissConfig{
    /**
     * check table have primary or unique key
     */
    private Boolean checkUnique;
    /**
     * check table column by name and type
     */
    private List<ColumnsModel> columnMissModelList;
}
