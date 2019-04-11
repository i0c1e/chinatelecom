package com.victor.chinatelecom.common.Constant;

import com.victor.chinatelecom.common.Bean.Val;

public enum Names implements Val {
    NAMESPACE("ct"),
    TOPIC("ct"),
    TABLE("ct:callLog"),
    CF_CALLER("caller");


    private String name;

    Names(String name) {
        this.name = name;
    }

    public void setValue(Object val) {
        name = (String)val;
    }

    public String getValue() {
        return name;
    }
}
