/**
 * @author Charles
 * @create 2019/3/28
 * @since 1.0.0
 */

package com.victor.chinatelecom.producer.IO;

import com.victor.chinatelecom.common.Bean.DataOut;

import java.io.*;

/**
 * 本地文件数据输出
 */
public class LocalFileDataOut implements DataOut {
    private PrintWriter printWriter;

    public LocalFileDataOut(String path) {
        //setPath(path);
        try {
            printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(path),"UTF-8"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void setPath(String path) {

    }

    public void write(Object object) {
        write(object.toString());
    }

    public void write(String string) {
        printWriter.println(string);
        printWriter.flush();
    }

    public void close() throws IOException {
        if (printWriter != null)
            printWriter.close();

    }
}
