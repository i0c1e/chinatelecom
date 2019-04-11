/**
 * @author Charles
 * @create 2019/3/28
 * @since 1.0.0
 */

package com.victor.chinatelecom.common.Bean;

import java.io.Closeable;

public interface DataOut extends Closeable  {
    public void setPath(String path);
    public void write(Object object);
    public void write(String string);
}
