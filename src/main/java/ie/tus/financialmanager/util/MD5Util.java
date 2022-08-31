package ie.tus.financialmanager.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

    /**
     * 将数据进行 MD5 加密，并以16进制字符串格式输出
     * @param data
     * @return
     */

    public static String md5(String data){
        StringBuilder s = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            byte[] md5 = md.digest(data.getBytes(StandardCharsets.UTF_8));

            // 将字节数据转换成十六进制
            for (byte b : md5){
                s.append(Integer.toHexString(b & 0xff));
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return s.toString();
    }

    public static void main(String[] args) {
        String password = "admin123";
        String md5HexStr = md5(password);
        System.out.println("==> MD5 加密前："+password);
        System.out.println("==> MD5 加密后："+md5HexStr);
    }
}
