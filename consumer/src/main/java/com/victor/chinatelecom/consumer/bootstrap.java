/**
 * @author Charles
 * @create 2019/3/29
 * @since 1.0.0
 */

package com.victor.chinatelecom.consumer;


import com.victor.chinatelecom.consumer.Bean.CalllogConsumer;


/**
 * 1.使用kafka消费者获取flume采集的数据
 * 2.将获取到的数据存储到hbase
 */
public class bootstrap {
    public static void main(String[] args) throws Exception {
        //1.使用kafka消费者获取flume采集的数据
        CalllogConsumer consumer = new CalllogConsumer();

        //消费数据
        consumer.consume();

        //关闭资源
        consumer.close();
        // 2.将获取到的数据存储到hbase
    }
}
