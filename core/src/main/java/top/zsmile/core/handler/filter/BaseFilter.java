package top.zsmile.core.handler.filter;


import top.zsmile.core.model.TablesModel;

import java.util.List;

public interface BaseFilter<T> {
    public List<T> filter(List<T> list);
}
