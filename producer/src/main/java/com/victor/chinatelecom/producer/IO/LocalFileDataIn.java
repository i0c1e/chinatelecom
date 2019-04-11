/**
 * @author Charles
 * @create 2019/3/28
 * @since 1.0.0
 */

package com.victor.chinatelecom.producer.IO;

import com.victor.chinatelecom.common.Bean.Data;
import com.victor.chinatelecom.common.Bean.DataIn;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 本地文件数据输出
 */
public class LocalFileDataIn implements DataIn {
    BufferedReader reader = null;

    public LocalFileDataIn(String path) {
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setPath(String path) {
//        try {
//            reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

    }

    public Object read() throws IOException {
        return null;
    }

    /**
     * @param clazz
     * @param <T>
     * @return
     * @throws IOException
     */
    public <T extends Data> List<T> read(Class<T> clazz) throws IOException {
        List<T> ts = new ArrayList<T>();

        try {
            String line = null;

            //从通讯录中读取所有值
            while ((line = reader.readLine()) != null) {
                T t = clazz.newInstance();
                t.setValue(line);
                ts.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ts;
    }


    public void close() throws IOException {
        if (reader!=null)
            reader.close();
    }
}
