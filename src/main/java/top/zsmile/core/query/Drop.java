package top.zsmile.core.query;

import top.zsmile.core.model.IndexModel;

import java.util.List;

public interface Drop {


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

}
