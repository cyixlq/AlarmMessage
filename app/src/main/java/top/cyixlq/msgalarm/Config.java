package top.cyixlq.msgalarm;

public class Config {

    public static final String TAG = "msg";

    public static final int TYPE_AS_PHONE = 0; // 仅仅根据电话号码判断是否符合
    public static final int TYPE_AS_CONTENT = 1; // 仅仅根据内容关键字判断是否符合
    public static final int TYPE_AS_PHONE_AND_CONTENT = 2; // 电话号码符合之后再根据内容关键字判断是否符合

    private static volatile String phone = null;
    private static String keyword = null;
    private static int type = 0;

    public static synchronized String getPhone() {
        return phone;
    }

    public static synchronized String getKeyword() {
        return keyword;
    }

    public static synchronized int getType() {
        return type;
    }

    public static void setPhone(String phone1) {
        phone = phone1;
    }

    public static void setKeyword(String keyword1) {
        keyword = keyword1;
    }

    public static void setType(int type1) {
        type = type1;
    }
}
