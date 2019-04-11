/**
 * @author Charles
 * @create 2019/3/29
 * @since 1.0.0
 */

package com.victor.chinatelecom.consumer.Bean;

import com.victor.chinatelecom.common.Bean.Consumer;
import com.victor.chinatelecom.common.Constant.Names;
import com.victor.chinatelecom.consumer.Dao.HBaseDao;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

public class CalllogConsumer implements Consumer {
    private KafkaConsumer<String, String> consumer;

    public void consume() throws Exception {
        Properties prop = new Properties();
        HBaseDao hBaseDao = new HBaseDao();
        hBaseDao.init();
        try {
                //调用类加载器加载配置文件
                prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("consumer.properties"));
                //new consumer并订阅ct topic
                consumer = new KafkaConsumer<String, String>(prop);
                consumer.subscribe(Arrays.asList(Names.TOPIC.getValue()));
                while(true) {
                    //拉取消息
                    ConsumerRecords<String, String> records = consumer.poll(100);

                    for (ConsumerRecord<String, String> record : records) {
                        System.out.println(record.value());
                        CallLog log = new CallLog(record.value());
//                        hBaseDao.insert(log);
                        hBaseDao.insert(record.value());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


    }

    public void close() throws IOException {
        if (consumer != null)
            consumer.close();
    }
}
