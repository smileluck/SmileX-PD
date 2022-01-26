package top.zsmile.core.handler.replace;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TableNameReplace implements BaseReplace {

    private ReplaceConfig replaceConfig;

    public TableNameReplace(ReplaceConfig replaceConfig) {
        this.replaceConfig = replaceConfig;
    }

    @Override
    public String replace(String text) {
        List<String> replaceTablePrefix = replaceConfig.getReplaceTablePrefix();
        List<String> replaceTableSuffix = replaceConfig.getReplaceTableSuffix();
        String afterText = text;
        String prefix = replaceTablePrefix != null ? replaceTablePrefix.stream().filter(e -> text.startsWith(e)).findAny().orElse(null):null;
        boolean state = false;
        if (prefix != null) {
            afterText = afterText.replaceFirst(prefix, replaceConfig.getReplaceStr());
            state = true;
        }

        String suffix = replaceTableSuffix != null ? replaceTableSuffix.stream().filter(e -> text.endsWith(e)).findAny().orElse(null) : null;
        if (suffix != null) {
            afterText = afterText.replaceFirst(suffix, replaceConfig.getReplaceStr());
            state = true;
        }

        if (!state) {
            afterText = replaceConfig.getReplaceStr() + afterText;
        }

        return afterText;
    }

    @Override
    public List<String> replace(List<String> list) {
        List<String> stringStream = list.stream().map(item -> replace(item)).collect(Collectors.toList());
        return stringStream;
    }
}
