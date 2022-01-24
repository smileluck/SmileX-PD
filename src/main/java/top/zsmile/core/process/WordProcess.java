package top.zsmile.core.process;

import freemarker.template.Template;
import top.zsmile.core.config.FreemakerConfig;

import java.io.*;
import java.util.Map;

public class WordProcess extends AbstractProcess {
    @Override
    public void process(Map<String, Object> dataMap) {
        try {
            Template template = FreemakerConfig.INSTANCE.getTemplate("document-word.ftl");
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("/test.docx")), "UTF-8"));
            //FreeMarker使用Word模板和数据生成Word文档
            template.process(dataMap, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
