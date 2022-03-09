package top.zsmile.core.config;

import freemarker.template.Configuration;
import top.zsmile.core.constant.DefaultConstants;
import top.zsmile.core.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class FreemakerConfig {
    public static final Configuration INSTANCE;

    static {
        INSTANCE = new Configuration();
        try {
            String path = FreemakerConfig.class.getResource("/").getPath();
            INSTANCE.setDirectoryForTemplateLoading(new File(path+ "/template/freemarker/"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        INSTANCE.setDefaultEncoding(DefaultConstants.ENCODEING);
        INSTANCE.setURLEscapingCharset(DefaultConstants.ENCODEING);
        INSTANCE.setDateFormat(DefaultConstants.DATE_FORMAT);
        INSTANCE.setDateTimeFormat(DefaultConstants.DATETIME_FORMAT);
        INSTANCE.setTimeFormat(DefaultConstants.TIME_FORMAT);
        INSTANCE.setLocale(Locale.CHINA);

    }

}
