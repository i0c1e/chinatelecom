/**
 * @author Charles
 * @create 2019/3/29
 * @since 1.0.0
 */

package com.victor.chinatelecom.producer.Bean;

public class CallLog {
    private String tel1;
    private String tel2;
    private String callTime;
    private String duration;

    public CallLog(String tel1, String tel2, String callTime, String duration) {
        this.tel1 = tel1;
        this.tel2 = tel2;
        this.callTime = callTime;
        this.duration = duration;
    }

    public String getTel1() {
        return tel1;
    }

    public void setTel1(String tel1) {
        this.tel1 = tel1;
    }

    public String getTel2() {
        return tel2;
    }

    public void setTel2(String tel2) {
        this.tel2 = tel2;
    }

    public String getCallTime() {
        return callTime;
    }

    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return tel1 + "\t" + tel2 + "\t" + callTime + "\t" + duration;
    }
}
