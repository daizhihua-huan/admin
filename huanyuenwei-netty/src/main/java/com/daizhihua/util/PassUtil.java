package com.daizhihua.util;

import io.netty.buffer.ByteBuf;
import org.apache.commons.lang.StringUtils;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

public class PassUtil {

    /**
     * 解析byte数组返回16进制的String类型
     *
     * @param src byte数组
     * @return
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 解析数据
     * @param buffer
     * @return
     */
    public static String bytesToHexString(ByteBuf buffer) {
        final int length = buffer.readableBytes();
        StringBuffer sb = new StringBuffer(length);
        String sTmp;

        for (int i = 0; i < length; i++) {
            byte b = buffer.readByte();
            sTmp = Integer.toHexString(0xFF & b);
            if (sTmp.length() < 2)
                sb.append(0);
            sb.append(sTmp.toUpperCase());
        }
        return sb.toString();
    }
    public static String CRC_16(byte[] bytes) {
        int i, j, lsb;
        int h = 0xffff;
        for (i = 0; i < bytes.length; i++) {
            h ^= bytes[i];
            for (j = 0; j < 8; j++) {
                lsb = h & 0x0001; // 取 CRC 的移出位
                h >>= 1;
                if (lsb == 1) {
                    h ^= 0x8408;
                }
            }
        }
        h ^= 0xffff;
        return Integer.toHexString(h).toUpperCase();
    }


    /**
     * 将解析后的16进制数据转换成10进制
     *
     * @param data 16进制的数据
     * @return
     */
    public static int intToHexString(String data) {

        return Integer.parseInt(data, 16);

    }

    public static String hexStringToByte(String hex){
        int i = Integer.parseInt(hex, 16);

        String str2 = Integer.toBinaryString(i);
        return StringUtils.leftPad(str2,8,"0");
    }
    public static byte[] hexStringToByteInfo(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }
    private static byte toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }


    /**
     * 将解析后的16进制转成10进制的long
     * @param data 16进制数据
     * @return
     */
    public static long intToLongHexString(String data){

        return  Long.parseLong(data,16);

    }


    /**
     * 将16进制的数据转换成byte的数组
     *
     * @param hex
     * @return
     */
    public static byte[] hex2Bytes(String hex) {
        if (hex == null || hex.length() == 0) {
            return null;
        }

        char[] hexChars = hex.toCharArray();
        byte[] bytes = new byte[hexChars.length / 2];   // 如果 hex 中的字符不是偶数个, 则忽略最后一个

        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt("" + hexChars[i * 2] + hexChars[i * 2 + 1], 16);
        }

        return bytes;
    }

    public static String toHexString(int number) {

        return Integer.toHexString((number & 0x000000FF) | 0xFFFFFF00).substring(6);
    }

    /**
     * 补齐低位没有0
     * @param data
     * @return
     */
    public static String toHexString(long data) {
        return Long.toHexString((data & 0x000000FF) | 0xFFFFFF00).substring(6);
    }

    /**
     * 解析mac地址
     * @param list
     * @return
     */
    public static String getMac(List<String>list){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i != 0) {
                sb.append("-");
            }
            // mac[i] & 0xFF 是为了把byte转化为正整数
            int decimal = Integer.parseInt(list.get(i), 16);
            //int a = Integer.parseInt(list.get(i)) & 0xFF;
            char result = (char)decimal;
            System.out.println(result);
            String s = Integer.toHexString(Integer.parseInt(list.get(i)) & 0xFF);
            sb.append(s.length() == 1 ? 0 + s : s);
        }
        return sb.toString();
    }


    /**
     * 将byte的字节倒叙用来获取大端模式
     * @param a
     * @return
     */
    public static byte[] reverse(byte[] a) {
        byte[] b=a;
        for(int start=0,end=b.length-1;start<end;start++,end--) {
            byte temp=b[start];
            b[start]=b[end];
            b[end]=temp;
        }
        return b;
    }




    /**
     * 将long的数值变成小端模式
     * @param var0
     * @return
     */
    public static String lowHigh(Long var0) {
        int var1 = 1;
        Long var2 = var0 >> 8;
        Long var3 = var0 & 0x000000FF;
        String var4 = StringUtils.leftPad(Long.toHexString(var2),2,"0");
        String var5 = StringUtils.leftPad(Long.toHexString(var3),2,"0");
        if (var4.length() > 2) {
            do {
                if (var1 > 1) {
                    var2 >>= 8;
                }
                var4 = Long.toHexString(var2 >> 8);
                /**
                 * 因为java提供的转16进制的时候如果 前面是0会省略
                 * 使用apach提供的
                 */
//                var5 = var5 +StringUtils.leftPad(Long.toHexString(var2), 2,"0");
              /*  System.out.println("apach提供的"+StringUtils.leftPad(Long.toHexString(var2 & 0x000000FF), 2,"0"));
                System.out.println("java提供过的"+Long.toHexString(var2 & 0x000000FF));*/
                var5 = var5 + StringUtils.leftPad(Long.toHexString(var2 & 0x000000FF), 2,"0");
                ++var1;
            } while (var4.length() > 2);
        }
        if (var4.length() < 2) {
            var4 = "0" + var4;
        }
        if (var5.length() < 2) {
            var5 = "0" + var5;
        }
//        System.out.println(var5+var4);
        return var5 + var4;
    }

    /**
     * Reformats a Little Endian value to bigEndian
     * 将小端模式的数据变成大端模式
     *
     * @param val the value to transform
     * @return Big Endian Value
     */
    public static long long2LittleEndian(long val) {
        return
                ((val & 0xff00000000000000L) >>> 56) +
                        (((val & 0x00ff000000000000L) >>> 48) << 8) +
                        (((val & 0x0000ff0000000000L) >>> 40) << 16) +
                        (((val & 0x000000ff00000000L) >>> 32) << 24) +
                        (((val & 0x00000000ff000000L) >>> 24) << 32) +
                        (((val & 0x0000000000ff0000L) >>> 16) << 40) +
                        (((val & 0x000000000000ff00L) >>> 8) << 48) +
                        (((val & 0x00000000000000ffL)) << 56)
                ;
    }

    /**
     * 将int变成小端模式
     * @param var0
     * @return
     */
    public static String lowHigh(int var0) {
        int var1 = 1;
        int var2 = var0 >> 8;
        int var3 = var0 & 255;
        String var4 = toHexString(var2);
        String var5 = toHexString(var3);
        if (var4.length() > 2) {
            do {
                if (var1 > 1) {
                    var2 >>= 8;
                }
                var4 = toHexString(var2 >> 8);
                var5 = var5 + toHexString(var2 & 255);
                ++var1;
            } while (var4.length() > 2);
        }
        if (var4.length() < 2) {
            var4 = "0" + var4;
        }
        if (var5.length() < 2) {
            var5 = "0" + var5;
        }
        return var5 + var4;
    }

    /**
     * 将字符串转成assci码
     * 在转换成16进制的string字符串进行发送
     *
     * @param name
     * @return
     */
    public static String StringToAsc(String name) { //字符串转换为ASCII码
        char[] chars = name.toCharArray();
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
//            System.out.println(" " + chars[i] + " " + (int)chars[i] );
            result.append(Integer.toHexString(chars[i]));
        }
        return result.toString();
    }



    public static void main(String[] args) throws UnknownHostException, SocketException {
        System.out.println(StringToAsc("daizhihua"));
    }

}
