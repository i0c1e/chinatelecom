/**
 * @author Charles
 * @create 2019/3/28
 * @since 1.0.0
 */

package com.victor.chinatelecom.common.Bean;

public class Data implements Val {
    private String content;



    public void setValue(Object val) {
        content = (String)val;
    }

    public Object getValue() {
        return content;
    }
}
