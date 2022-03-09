package top.zsmile.core.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import top.zsmile.core.constant.DefaultConstants;

@Data
public class DatabaseModel {
    private String name = DefaultConstants.NAME;
    private String describe = DefaultConstants.DESCRIPTION;
    private String version = DefaultConstants.VERSION;
}
