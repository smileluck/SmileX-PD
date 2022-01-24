package top.zsmile.core.handler.replace;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SqlReplace implements BaseReplace {

    private ReplaceConfig replaceConfig;

    public SqlReplace(ReplaceConfig replaceConfig) {
        this.replaceConfig = replaceConfig;
    }

    @Override
    public String replace(String text) {
        List<String> replaceTablePrefix = replaceConfig.getReplaceTablePrefix();
        List<String> replaceTableSuffix = replaceConfig.getReplaceTableSuffix();
        String afterText = text;
        String prefix = replaceTablePrefix.stream().filter(e -> text.startsWith(e)).findAny().get();
        if (prefix != null) {
            afterText = afterText.replaceFirst(prefix, replaceConfig.getReplaceStr());
        }

        String suffix = replaceTableSuffix.stream().filter(e -> text.endsWith(e)).findAny().get();
        if (suffix != null) {
            afterText = afterText.replaceFirst(suffix, replaceConfig.getReplaceStr());
        }
        return afterText;
    }

    @Override
    public List<String> replace(List<String> list) {
        List<String> stringStream = list.stream().map(item -> replace(item)).collect(Collectors.toList());
        return stringStream;
    }
}
