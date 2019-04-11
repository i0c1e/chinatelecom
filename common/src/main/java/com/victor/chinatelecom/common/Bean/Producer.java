package com.victor.chinatelecom.common.Bean;

import java.io.Closeable;

public interface Producer extends Closeable {
    public void setDataIn(DataIn in);
    public void  setDataOut(DataOut out);

    /**
     * 生产数据
     */
    public void produce();
}
