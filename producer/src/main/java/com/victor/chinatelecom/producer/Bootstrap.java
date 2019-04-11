/**
 * @author Charles
 * @create 2019/3/28
 * @since 1.0.0
 */

package com.victor.chinatelecom.producer;

import com.victor.chinatelecom.producer.Bean.LocalFileProducer;
import com.victor.chinatelecom.producer.IO.LocalFileDataIn;
import com.victor.chinatelecom.producer.IO.LocalFileDataOut;

import java.io.IOException;

public class Bootstrap {
    public static void main(String[] args) throws IOException {
        if (args == null || args.length < 2) {
            System.out.println("arguments error");
            System.out.println("java -jar produce.jar input output");
            System.exit(1);
        }
        //构建生产者对象
        LocalFileProducer producer = new LocalFileProducer();

//        producer.setDataIn(new LocalFileDataIn("/opt/data/Contact.txt"));
//        producer.setDataOut(new LocalFileDataOut("/opt/data/Call.txt"));
        producer.setDataIn(new LocalFileDataIn(args[0]));
        producer.setDataOut(new LocalFileDataOut(args[1]));


        //生产数据
        producer.produce();

        //关闭对象
        producer.close();
    }
}
