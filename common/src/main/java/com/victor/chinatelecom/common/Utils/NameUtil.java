/**
 * @author Charles
 * @create 2019/3/29
 * @since 1.0.0
 */

package com.victor.chinatelecom.common.Utils;

import java.text.DecimalFormat;

public class NameUtil {
    public static String format(int num, int length){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append("0");
        }
        DecimalFormat df = new DecimalFormat(sb.toString());
        return df.format(num);
    }

    public static void main(String[] args) {
        int a = 10;
        String format = NameUtil.format(10, 10);
        System.out.println(format);
    }
}
