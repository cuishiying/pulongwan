package com.shanglan.pulongwan.utils;

import com.shanglan.pulongwan.config.Constance;
import com.shanglan.pulongwan.entity.Field;
import org.apache.http.util.TextUtils;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cuishiying on 2017/7/24.
 * CDT解码
 */
@Component
public class DecodeUtils {

    private static StringBuilder stringBuilder = new StringBuilder("");

    /**
     * 字节数组转16进制字符串
     * @param src
     * @param len
     * @return
     */
    public static String bytesToHexString(byte[] src,int len){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < len; i++) {
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
     * 16进制的字符串表示转成字节数组
     *
     * @param hexString 16进制格式的字符串
     * @return 转换后的字节数组
     **/
    public static byte[] toByteArray(String hexString) {
        if (TextUtils.isEmpty(hexString))
            throw new IllegalArgumentException("this hexString must not be empty");

        hexString = hexString.toLowerCase();
        final byte[] byteArray = new byte[hexString.length() / 2];
        int k = 0;
        for (int i = 0; i < byteArray.length; i++) {//因为是16进制，最多只会占用4位，转换成字节需要两个16进制的字符，高位在先
            byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xff);
            byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xff);
            byteArray[i] = (byte) (high << 4 | low);
            k += 2;
        }
        return byteArray;
    }



    //字节转16进制显示数 30-->48
    public static int byte2Hex(byte b) {
        int i = b & 0xFF;
        return i;
    }
    //字节转16进制字符串
    public static String byte2HexString(byte b) {
        stringBuilder.setLength(0);
        int i = b & 0xFF;
        String s = Integer.toHexString(i);
        if(s.length()<2){
            stringBuilder.append(0);
        }
        stringBuilder.append(s);
        return stringBuilder.toString();
    }

    /**
     * 小端模式转数字
     * @param bytes
     * @return
     */
    public static int decodeHex2Int(byte[] bytes){
        int j = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
        return j;
    }

    /**
     * 封装数据
     * @param p0
     * @param p1
     * @param p2
     * @param p3
     * @param p4
     * @param p5
     * @return
     */
    public static List<Field> byte2Json(Byte p0,Byte p1,Byte p2,Byte p3,Byte p4,Byte p5){

        byte[] b = new byte[2];
        List<Field> list = new ArrayList<>();
        Field f1 = null;
        Field f2 = null;
        String desc = null;

        desc = Constance.getFieldConfig(byte2Hex(p0)*2+"");
        if(null!= desc){
            f1 = new Field();
            b[0] = p1;
            b[1] = p2;
            String s = bytesToHexString(b, 2);
            int i = decodeHex2Int(b);
            f1.setFunctionCode(byte2HexString(p0));//功能码
            f1.setTelemetrySignal(byte2Hex(p0)*2+"");//遥测号
            f1.setSrcValue(s);//源码值

            f1.setTelemetryValue(i);//遥测值
            f1.setDescriber(desc);//描述
            list.add(f1);
        }

        desc = Constance.getFieldConfig(byte2Hex(p0)*2+1+"");
        if(null!= desc){
            f2 = new Field();
            b[0] = p3;
            b[1] = p4;
            String s1 = bytesToHexString(b, 2);
            int i1 = decodeHex2Int(b);
            f2.setFunctionCode(byte2HexString(p0));
            f2.setTelemetrySignal(byte2Hex(p0)*2+1+"");
            f2.setSrcValue(s1);

            f2.setTelemetryValue(i1);
            f2.setDescriber(desc);
            list.add(f2);
        }
        return list;
    }
}
