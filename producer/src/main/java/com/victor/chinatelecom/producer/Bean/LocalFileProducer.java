/**
 * @author Charles
 * @create 2019/3/28
 * @since 1.0.0
 */

package com.victor.chinatelecom.producer.Bean;

import com.victor.chinatelecom.common.Bean.DataIn;
import com.victor.chinatelecom.common.Bean.DataOut;
import com.victor.chinatelecom.common.Bean.Producer;
import com.victor.chinatelecom.common.Utils.DateUtil;
import com.victor.chinatelecom.common.Utils.NameUtil;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class LocalFileProducer implements Producer {
    private DataIn in;
    private DataOut out;
    private volatile boolean flag = true;

    public void setDataIn(DataIn in) {
        this.in = in;
    }

    public void setDataOut(DataOut out) {
        this.out = out;
    }

    /**
     * 生产数据
     */
    public void produce() {
        try {
            //读取通讯录
            List<Contact> list = in.read(Contact.class);

            while (flag) {
                //获取两条电话号码(主叫,被叫)
                int call1Index = new Random().nextInt(25);
                int call2Index;
                while (true) {
                    call2Index = new Random().nextInt(25);
                    if (call1Index != call2Index)
                        break;
                }
                Contact call1 = list.get(call1Index);
                Contact call2 = list.get(call2Index);

                //生成随机通话时间
                String startDate = "20180101000000";
                String endDate = "20190101000000";
                long startTime = DateUtil.parse(startDate,"yyyyMMddHHmmss").getTime();
                long endTime = DateUtil.parse(endDate,"yyyyMMddHHmmss").getTime();
                long callTime = startTime + (long)((endTime - startTime)* Math.random());

                String callTimeString = DateUtil.format(new Date(callTime),"yyyyMMddHHmmss");

                //生成随机通话时长
                String duration = NameUtil.format(new Random().nextInt(3000), 4);

                //生成通话记录
                CallLog callLog = new CallLog(call1.getTel(), call2.getTel(), callTimeString, duration);


                //刷写到数据文件中
                System.out.println(callLog);
                out.write(callLog);
                Thread.sleep(500);
//            for (Contact contact : list) {
//                System.out.println(contact);
//            }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void close() throws IOException {
        if (in != null)
            in.close();

        if (out != null)
            out.close();
    }
}
