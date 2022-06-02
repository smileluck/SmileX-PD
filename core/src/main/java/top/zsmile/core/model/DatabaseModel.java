package top.zsmile.core.model;

import lombok.Data;
import top.zsmile.core.constant.DefaultConstants;

@Data
public class DatabaseModel {
    private String name = DefaultConstants.NAME;
    private String describe = DefaultConstants.DESCRIPTION;
    private String version = DefaultConstants.VERSION;

    public DatabaseModel() {


    }

    public DatabaseModel(String name, String describe, String version) {
        this.name = name;
        this.describe = describe;
        this.version = version;
    }
}
