/**
 * @author Charles
 * @create 2019/4/3
 * @since 1.0.0
 */

package com.victor.chinatelecom.analysis.kv;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class AnalysisValue implements Writable {
    private int sumCall;
    private int sumDuration;

    public AnalysisValue() {
    }

    public AnalysisValue(int sumCall, int sumDuration) {
        this.sumCall = sumCall;
        this.sumDuration = sumDuration;
    }

    public int getSumCall() {
        return sumCall;
    }

    public void setSumCall(int sumCall) {
        this.sumCall = sumCall;
    }

    public int getSumDuration() {
        return sumDuration;
    }

    public void setSumDuration(int sumDuration) {
        this.sumDuration = sumDuration;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(sumCall+"");
        dataOutput.writeUTF(sumDuration+"");
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        sumCall = Integer.parseInt(dataInput.readUTF());
        sumDuration = Integer.parseInt(dataInput.readUTF());
    }
}
