package top.zsmile.core.handler.replace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SqlForeignKeyReplace implements BaseReplace {

    @Override
    public String replace(String text) {
        String[] split = text.split("\\r?\\n");
        int num = -1;

        List<String> strings = new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            String line = split[i];
            if (line.indexOf("UNIQUE KEY") > -1) {
                num = num == -1 ? i : num;
                continue;
            }

            if (line.indexOf("\tKEY") > -1 || line.indexOf("  KEY") > -1) {
                num = num == -1 ? i : num;
                continue;
            }

            if (line.indexOf("CONSTRAINT") > -1) {
                num = num == -1 ? i : num;
                continue;
            }

            strings.add(line);
        }

        if (num != -1) {
            String s = strings.get(num - 1);
            strings.set(num - 1, s.substring(0, s.lastIndexOf(",")));
        }
        String out = String.join("", strings);
        return out;
    }

    @Override
    public List<String> replace(List<String> list) {
        List<String> stringStream = list.stream().map(item -> replace(item)).collect(Collectors.toList());
        return stringStream;
    }

    public static void main(String[] args) {
        String str = "CREATE TABLE `tb_certificate` (\n" +
                "  `certificate_id` bigint(20) unsigned NOT NULL COMMENT '证书id',\n" +
                "  `user_id` bigint(20) unsigned DEFAULT 0 COMMENT '用户id',\n" +
                "  `name` varchar(50) DEFAULT NULL COMMENT '证书颁发的姓名',\n" +
                "  `project` varchar(50) DEFAULT '全国悬疑推理主持人' COMMENT '证书名称',\n" +
                "  `level` int(1) DEFAULT 0 COMMENT '证书等级（0：未知等级，基本不会返回0；1：初级；2：中级；3：高级；4：国家级）',\n" +
                "  `id_card` varchar(50) DEFAULT NULL COMMENT '身份证号',\n" +
                "  `code` varchar(50) DEFAULT NULL COMMENT '证书编号',\n" +
                "  `time` timestamp NULL DEFAULT '0000-00-00 00:00:00' COMMENT '颁发日期',\n" +
                "  `organization` varchar(255) DEFAULT '全国体育运动学校联合会' COMMENT '发证机构',\n" +
                "  `del` int(1) DEFAULT 0 COMMENT '是否删除',\n" +
                "  `create_time` timestamp NULL DEFAULT current_timestamp() COMMENT '创建时间',\n" +
                "  `update_time` timestamp NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',\n" +
                "  PRIMARY KEY (`certificate_id`),\n" +
                "  UNIQUE KEY `证书编号` (`code`) USING BTREE,\n" +
                "  KEY `用户id` (`user_id`) USING HASH,\n" +
                "  KEY `id_card` (`id_card`),\n" +
                "  CONSTRAINT `tb_certificate_ibfk_111` FOREIGN KEY (`id_card`) REFERENCES `tb_certificate_user` (`id_card`) ON UPDATE CASCADE\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Dm证书表'";

//        System.out.println(out);
    }
}
