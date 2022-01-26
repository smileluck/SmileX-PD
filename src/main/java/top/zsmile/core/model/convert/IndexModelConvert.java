package top.zsmile.core.model.convert;

import java.util.HashMap;
import java.util.Map;

public class IndexModelConvert implements ModelConvert {
    public static final IndexModelConvert INSTANCE;
    private static final Map<String, String> keyConvertMap;

    private IndexModelConvert(){}

    static {
        keyConvertMap = new HashMap<>();
        keyConvertMap.put("Table", "tableName");
        keyConvertMap.put("Key_name", "keyName");
        keyConvertMap.put("Non_unique", "nonUnique");
        keyConvertMap.put("Seq_in_index", "seqInIndex");
        keyConvertMap.put("Column_name", "columnName");
        keyConvertMap.put("Null", "izNull");
        keyConvertMap.put("Index_type", "indexType");
        keyConvertMap.put("Comment", "comment");
        keyConvertMap.put("Index_comment", "indexComment");
        INSTANCE = new IndexModelConvert();
    }

    @Override
    public Map getConvertMap() {
        return keyConvertMap;
    }
}
