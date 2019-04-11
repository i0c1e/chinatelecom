/**
 * @author Charles
 * @create 2019/3/30
 * @since 1.0.0
 */

package com.victor.chinatelecom.common.Constant;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ConfigConstant {
    private static Map<String, String> map = new HashMap<String, String>();

    static {
        //使用国际化加载配置文件 ct.properties
        ResourceBundle bundle = ResourceBundle.getBundle("ct");
        Enumeration<String> keys = bundle.getKeys();
        while (keys.hasMoreElements()) {
            String s = keys.nextElement();
            //封装到map集合中
            map.put(s, bundle.getString(s));
        }
    }

    //对外隐藏map集合,改用getVal方法获取集合元素,增强安全性
    public static String getVal(String key) {
        return map.get(key);
    }

    public static void main(String[] args) {
        System.out.println(getVal("ct.table"));
    }
}
