/**
 * @author Charles
 * @create 2019/3/29
 * @since 1.0.0
 */

package com.victor.chinatelecom.consumer.Dao;

import com.victor.chinatelecom.common.Bean.DaoBase;
import com.victor.chinatelecom.common.Constant.ConfigConstant;
import com.victor.chinatelecom.common.Constant.Names;
import com.victor.chinatelecom.consumer.Bean.CallLog;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.ArrayList;
import java.util.List;


public class HBaseDao extends DaoBase {
    public void init() throws Exception {
        start();

        createNamespaceNX(ConfigConstant.getVal("ct.namespace"));
        createTableXX(ConfigConstant.getVal("ct.table"),
                ConfigConstant.getVal("ct.coprocessorName"),
                Integer.parseInt(ConfigConstant.getVal("ct.regionNumber")),
                ConfigConstant.getVal("ct.cf.caller"),
                ConfigConstant.getVal("ct.cf.reciever")
        );

        end();
    }

    /**
     * 将通话数据保存到HBase中
     *
     * @param value
     */
    public void insert(String value) throws Exception{
        //获取数据
        String[] fields = value.split("\t");
        //call1 call2 calltime duration
        String call1 = fields[0];
        String call2 = fields[1];
        String calltime = fields[2];
        String duration = fields[3];
        List<Put> puts = new ArrayList<Put>();

        //rowkey设计 分区号+call1+calltime+call2+duration+_1(主叫标记)
        String callerRowkey = getRegionNum(call1, calltime) + "_" + call1 + "_"
                + calltime + "_" + call2 + "_" + duration + "_1";

        //创建数据对象
        Put callerPut = new Put(Bytes.toBytes(callerRowkey));
        byte[] callerFamily = Bytes.toBytes(ConfigConstant.getVal("ct.cf.caller"));


        callerPut.addColumn(callerFamily, Bytes.toBytes("call1"), Bytes.toBytes(call1));
        callerPut.addColumn(callerFamily, Bytes.toBytes("call2"), Bytes.toBytes(call2));
        callerPut.addColumn(callerFamily, Bytes.toBytes("calltime"), Bytes.toBytes(calltime));
        callerPut.addColumn(callerFamily, Bytes.toBytes("duration"), Bytes.toBytes(duration));
        callerPut.addColumn(callerFamily, Bytes.toBytes("flag"), Bytes.toBytes("1"));
        //插入数据
        putData(ConfigConstant.getVal("ct.table"), callerPut);

        //改用协处理器实现, 减少发送负担
//        puts.add(callerPut);

//        //rowkey设计 分区号+call1+calltime+call2+duration+_1(主叫标记)
//        String recieverRowkey = getRegionNum(call1, calltime) + "_" + call1 + "_"
//                + calltime + "_" + call2 + "_" + duration + "_0";
//
//        //创建数据对象
//        Put recieverPut = new Put(Bytes.toBytes(recieverRowkey));
//        byte[] recieverFamily = Bytes.toBytes(ConfigConstant.getVal("ct.cf.reciever"));
//
//
//        recieverPut.addColumn(recieverFamily, Bytes.toBytes("call1"), Bytes.toBytes(call1));
//        recieverPut.addColumn(recieverFamily, Bytes.toBytes("call2"), Bytes.toBytes(call2));
//        recieverPut.addColumn(recieverFamily, Bytes.toBytes("calltime"), Bytes.toBytes(calltime));
//        recieverPut.addColumn(recieverFamily, Bytes.toBytes("duration"), Bytes.toBytes(duration));
//        recieverPut.addColumn(recieverFamily, Bytes.toBytes("flag"), Bytes.toBytes("0"));
//        //插入数据
//        puts.add(recieverPut);
//        putData(ConfigConstant.getVal("ct.table"),puts);


//        putData(ConfigConstant.getVal("ct.table"), recieverPut);

    }


    public void insert(CallLog log) throws Exception {
//        log.setRowkey(getRegionNum(log.getCall1(), log.getCalltime())
//                + "_" + log.getCall1()
//                + "_" + log.getCalltime()
//                + "_" + log.getCall2()
//                + "_" + log.getDuration() );
        putData(log);

    }
}
