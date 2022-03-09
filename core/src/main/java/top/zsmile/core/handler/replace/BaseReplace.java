package top.zsmile.core.handler.replace;

import java.util.List;

public interface BaseReplace {
    String replace(String text);

    List<String> replace(List<String> list);
}
