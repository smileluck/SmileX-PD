package top.zsmile.demo;

public class StringDemo {
    public static void main(String[] args) {
        String str = "1234561231111456";
        String fff = str.replace("456", "fff");
        System.out.println(fff);

        String str1 = "123";
        String str2 = "123";
        System.out.println(str1 == str2);

//        String s = new String("abc");
//        String s1 = "abc";
//        String s2 = new String("abc");
//        System.out.println(s == s1.intern());
//        System.out.println(s == s2.intern());
//        System.out.println(s1 == s2.intern());

//        "1".intern();
        String s = "1";
        String intern = s.intern();
        String s2 = "1";
        System.out.println(s == s2);
        System.out.println(intern==s2);


        String s3 = new String("1") + new String("1");
        String s4 = "11";
        s3.intern();
        System.out.println(s3 == s4);
    }
}
