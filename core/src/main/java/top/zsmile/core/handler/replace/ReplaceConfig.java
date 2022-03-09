package top.zsmile.core.handler.replace;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReplaceConfig {

    public static ReplaceConfigBuilder builder(String str) {
        return new ReplaceConfigBuilder().replaceStr(str);
    }

    /**
     * 替换字符串
     */
    private String replaceStr;
    /**
     * 替换表前缀
     */
    private List<String> replaceTablePrefix;
    /**
     * 替换表后缀
     */
    private List<String> replaceTableSuffix;
}
