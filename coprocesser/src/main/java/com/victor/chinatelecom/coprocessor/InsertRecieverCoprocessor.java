/**
 * @author Charles
 * @create 2019/4/1
 * @since 1.0.0
 */

package com.victor.chinatelecom.coprocessor;

import com.victor.chinatelecom.common.Bean.DaoBase;
import com.victor.chinatelecom.common.Constant.ConfigConstant;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.coprocessor.BaseRegionObserver;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.regionserver.wal.WALEdit;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

//增加HBase协处理器,用于在添加主叫记录时自动添加一条被叫记录

public class InsertRecieverCoprocessor extends BaseRegionObserver {


    public void prePut(ObserverContext<RegionCoprocessorEnvironment> observerContext, Put put, WALEdit walEdit, Durability durability) throws IOException {

    }

    public void postPut(ObserverContext<RegionCoprocessorEnvironment> observerContext, Put put, WALEdit walEdit, Durability durability) throws IOException {

        TableName tableName = TableName.valueOf(ConfigConstant.getVal("ct.table"));
        HTableInterface table = observerContext.getEnvironment().getTable(tableName);
//        Put recieverPut = new Put();

        //1_17336896259_20181114073322_13579091549_2662_1
        String recieverRowkey = Bytes.toString(put.getRow());
        String[] fields = recieverRowkey.split("_");
        String flag = fields[5];
        //只有当flag=1即添加主叫记录时才出发协处理器,
        //否则会发生重复调用,死循环
        if ("1".equals(flag)) {
            String call1 = fields[1];
            String call2 = fields[3];
            String calltime = fields[2];
            String duration = fields[4];

            //rowkey设计 分区号+call1+calltime+call2+duration+_0(被叫标记)
            String newRecieverRowkey = DaoBase.getRegionNum(call2, calltime) + "_" + call2 + "_"
                    + calltime + "_" + call1 + "_" + duration + "_0";

            //创建数据对象
            Put recieverPut = new Put(Bytes.toBytes(newRecieverRowkey));
            byte[] recieverFamily = Bytes.toBytes(ConfigConstant.getVal("ct.cf.reciever"));

            recieverPut.addColumn(recieverFamily, Bytes.toBytes("call1"), Bytes.toBytes(call1));
            recieverPut.addColumn(recieverFamily, Bytes.toBytes("call2"), Bytes.toBytes(call2));
            recieverPut.addColumn(recieverFamily, Bytes.toBytes("calltime"), Bytes.toBytes(calltime));
            recieverPut.addColumn(recieverFamily, Bytes.toBytes("duration"), Bytes.toBytes(duration));
            recieverPut.addColumn(recieverFamily, Bytes.toBytes("flag"), Bytes.toBytes("0"));

            table.put(recieverPut);
        }

        table.close();
    }


}
