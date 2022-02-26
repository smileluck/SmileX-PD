package top.zsmile.core.query;

import top.zsmile.core.entity.dto.ColumnAddDTO;
import top.zsmile.core.entity.dto.ColumnChangeDTO;
import top.zsmile.core.model.IndexModel;

import java.util.List;

public interface DataManipulation {


    /**
     * 删除表所有索引
     */
    boolean dropIndex(String databaseName, String tableName);

    /**
     * 删除表制定索引列表
     */
    boolean dropIndex(String databaseName, String tableName, List<IndexModel> indexModels);

    /**
     * 删除表单个索引
     */
    boolean dropIndex(String databaseName, String tableName, String indexName);

    /**
     * 删除字段
     */
    boolean dropColumn(String databaseName, String tableName, String columnName);

    /**
     * 添加字段
     */
    boolean addColumn(String databaseName, String tableName, ColumnAddDTO columnAddDTO);
    /**
     * 修改字段
     */
    boolean changeColumn(String databaseName, String tableName, ColumnChangeDTO columnChangeDTO);
}
