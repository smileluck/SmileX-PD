package top.zsmile.core.filter;


import top.zsmile.core.model.TablesModel;

import java.util.List;

public interface BaseFilter {
    public List<TablesModel> filter(List<TablesModel> list);
}
