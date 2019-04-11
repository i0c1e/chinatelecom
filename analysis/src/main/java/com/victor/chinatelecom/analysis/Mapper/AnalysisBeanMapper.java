/**
 * @author Charles
 * @create 2019/4/2
 * @since 1.0.0
 */

package com.victor.chinatelecom.analysis.Mapper;

import com.victor.chinatelecom.analysis.kv.AnalysisKey;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;

import java.io.IOException;

public class AnalysisBeanMapper extends TableMapper<AnalysisKey, Text> {
    @Override
    protected void map(ImmutableBytesWritable key, Result value, Context context) throws IOException, InterruptedException {

        //传入的key就是hbase表的rowkey
        String rowkey = Bytes.toString(key.get());

        //3_16852329777_20181120082547_13820717895_1716_1
        String[] fields = rowkey.split("_");

        //
        String call1 = fields[1];
        String call2 = fields[3];
        String calltime = fields[2];
        String duration = fields[4];

        //20181120082547
        String year = calltime.substring(0,4);
        String month = calltime.substring(0, 6);
        String day = calltime.substring(0,8);
        Text durationText = new Text(duration);

        //caller  -     calltime,  times - duration
        //133     -     201806,     1    -  50min
        //主叫,年度统计
        context.write(new AnalysisKey(call1,year),durationText);
        //主叫,月度统计
        context.write(new AnalysisKey(call1,month),durationText);
        //主叫,日度统计
        context.write(new AnalysisKey(call1,day),durationText);

        //被叫,年度统计
        context.write(new AnalysisKey(call2,year),durationText);
        //被叫,月度统计
        context.write(new AnalysisKey(call2,month),durationText);
        //被叫,日度统计
        context.write(new AnalysisKey(call2,day),durationText);

    }
}
