package top.zsmile.core.utils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameStyleUtils {
    private static Pattern linePattern = Pattern.compile("_(\\w)");
    private static Pattern humpPattern = Pattern.compile("[A-Z]");

    public static String lineToHump(String str, boolean state) {
        str = str.toLowerCase();
        StringBuffer sb = new StringBuffer();

        Matcher matcher = linePattern.matcher(str);
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);

        if (state) {
            sb.replace(0, 1, String.valueOf(sb.charAt(0)).toUpperCase());
        }

        return sb.toString();
    }

    public static String humpToLine(String str) {
        StringBuffer sb = new StringBuffer();

        Matcher matcher = humpPattern.matcher(str);
        while (matcher.find()) {
            if (sb.length() != 0) {
                matcher.appendReplacement(sb, "_" + matcher.group(0));
            } else {
                matcher.appendReplacement(sb, matcher.group(0));
            }
        }
        matcher.appendTail(sb);


        return sb.toString().toLowerCase();
    }


    public static void main(String[] args) {
        String str1 = lineToHump("sdlfks_sdf_222", true);
        String str2 = lineToHump("Non_Unique", false);
        String str3 = lineToHump("Table", false);
        System.out.println(str1);
        System.out.println(str2);
        System.out.println(str3);
        System.out.println(humpToLine(str1));
        System.out.println(humpToLine(str2));
        System.out.println(humpToLine(str3));

    }
}
