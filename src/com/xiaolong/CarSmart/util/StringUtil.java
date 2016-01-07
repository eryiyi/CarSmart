package com.xiaolong.CarSmart.util;

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 1.二进制转换为十六进制
 * 2.E-mail 检测
 * 3.URL检测
 * 4.text是否包含空字符串
 * 5.添加对象数组
 *
 * @author dds
 */
public class StringUtil {
    //判断是否为JSOn格式
    public static boolean isJson(String json) {
        if (StringUtil.isNullOrEmpty(json)) {
            return false;
        }
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonParseException e) {
            return false;
        }
    }

    /**
     *
     * 方法描述：取得当前日期的上月或下月日期 ,amount=-1为上月日期，amount=1为下月日期；创建人：jya
     * @return
     * @throws Exception
     */
    public static String getFrontBackStrDate(String strDate, String format, int amount) throws Exception {
        if (null == strDate) {
            return null;
        }
        try {

            DateFormat fmt = new SimpleDateFormat(format);
            Calendar c = Calendar.getInstance();
            c.setTime(fmt.parse(strDate));
            c.add(Calendar.MONTH, amount);
            return fmt.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 添加对象数组
     *
     * @param spliter 间隔符
     * @param arr     数组
     * @return
     */
    public static String join(String spliter, Object[] arr) {
        if (arr == null || arr.length == 0) {
            return "";
        }
        if (spliter == null) {
            spliter = "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if (i == arr.length - 1) {
                break;
            }
            if (arr[i] == null) {
                continue;
            }
            builder.append(arr[i].toString());
            builder.append(spliter);
        }
        return builder.toString();
    }

    /**
     * java去除字符串中的空格、回车、换行符、制表符
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    public final static String md5(String plainText) {
// 返回字符串
        String md5Str = null;
        try {
// 操作字符串
            StringBuffer buf = new StringBuffer();

            MessageDigest md = MessageDigest.getInstance("MD5");

// 添加要进行计算摘要的信息,使用 plainText 的 byte 数组更新摘要。
            md.update(plainText.getBytes());
// 计算出摘要,完成哈希计算。
            byte b[] = md.digest();
            int i;
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
// 将整型 十进制 i 转换为16位，用十六进制参数表示的无符号整数值的字符串表示形式。
                buf.append(Integer.toHexString(i));
            }
// 32位的加密
            md5Str = buf.toString();
// 16位的加密
// md5Str = buf.toString().md5Strstring(8,24);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5Str;
    }

    /**
     * 生成32位编码
     * @return string
     */
    public static String getUUID(){
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
        return uuid;
    }

}
