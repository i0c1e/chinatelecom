/**
 * @author Charles
 * @create 2019/4/3
 * @since 1.0.0
 */

package com.victor.chinatelecom.analysis.kv;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class AnalysisKey implements WritableComparable<AnalysisKey> {
    private String call1;
    private String tel;

    public AnalysisKey() {
    }

    public AnalysisKey(String call1, String tel) {
        this.call1 = call1;
        this.tel = tel;
    }

    public String getCall1() {
        return call1;
    }

    public void setCall1(String call1) {
        this.call1 = call1;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(call1);
        dataOutput.writeUTF(tel);

    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        call1 = dataInput.readUTF();
        tel = dataInput.readUTF();
    }

    @Override
    public int compareTo(AnalysisKey o) {
        int result = call1.compareTo(o.getCall1());
        if (result == 0) {
            result = tel.compareTo(o.getTel());
        }
        return result;
    }
}
