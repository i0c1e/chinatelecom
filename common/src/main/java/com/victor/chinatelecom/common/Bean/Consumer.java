/**
 * @author Charles
 * @create 2019/3/29
 * @since 1.0.0
 */

package com.victor.chinatelecom.common.Bean;

import java.io.Closeable;
import java.io.IOException;

public interface Consumer extends Closeable {


    public void consume() throws Exception;

    public void close() throws IOException;
}
