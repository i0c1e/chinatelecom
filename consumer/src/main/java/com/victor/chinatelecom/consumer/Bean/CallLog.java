/**
 * @author Charles
 * @create 2019/3/31
 * @since 1.0.0
 */

package com.victor.chinatelecom.consumer.Bean;

import com.victor.chinatelecom.common.API.Column;
import com.victor.chinatelecom.common.API.Rowkey;
import com.victor.chinatelecom.common.API.TargetRef;
import com.victor.chinatelecom.common.Bean.DaoBase;

@TargetRef("ct:callLog")
public class CallLog {
    @Rowkey
    private String rowkey;
    @Column(family = "caller")
    private String call1;
    @Column(family = "caller")
    private String call2;
    @Column(family = "caller")
    private String calltime;
    @Column(family = "caller")
    private String duration;
    @Column(family = "caller")
    private String flag;

    public CallLog() {
    }

    public CallLog(String value) {
        String[] fields = value.split("\t");
        this.call1 = fields[0];
        this.call2 = fields[1];
        this.calltime = fields[2];
        this.duration = fields[3];
        setRowkey(DaoBase.getRegionNum(call1, calltime) + "_" + call1 + "_"
                + calltime + "_" + call2 + "_" + duration + "_" + flag);
//        System.out.println("rk: " + getRowkey());
    }

    public String getRowkey() {
        return rowkey;
    }

    public void setRowkey(String rowkey) {
        this.rowkey = rowkey;
    }

    public String getCall1() {
        return call1;
    }

    public void setCall1(String call1) {
        this.call1 = call1;
    }

    public String getCall2() {
        return call2;
    }

    public void setCall2(String call2) {
        this.call2 = call2;
    }

    public String getCalltime() {
        return calltime;
    }

    public void setCalltime(String calltime) {
        this.calltime = calltime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
