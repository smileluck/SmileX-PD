package top.zsmile.core.entity.dto;

import lombok.Builder;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;

@Data
@Builder
public class ColumnAddDTO {
    private String columnName;
    private String dataType;
    private Integer dataLen;
    private Integer dataPoint;
    private Integer nullable;
    private String defaultStr;
    private String comment;
    private String orderBy;

    public String toSql() {
        StringBuffer sb = new StringBuffer();
        sb.append(" ADD COLUMN `" + columnName + "` ");
        sb.append(dataType + "(" + dataLen + (dataPoint != null ? "," + dataPoint : "") + ") ");
        sb.append(nullable != null && nullable != 0 ? "NOT NULL " : "NULL ");
        if (Strings.isNotBlank(defaultStr)) {
            sb.append(" DEFAULT \"" + defaultStr + "\" ");
        }
        if (Strings.isNotBlank(comment)) {
            sb.append(" COMMENT \"" + comment + "\" ");
        }
        if (Strings.isNotBlank(orderBy)) {
            sb.append(orderBy);
        }
        //ALTER TABLE `smilex-boot`.`demo` ADD COLUMN `testadd` varchar(255) NOT NULL DEFAULT 666 COMMENT '123123' AFTER `name`

        return sb.toString();
    }
}
