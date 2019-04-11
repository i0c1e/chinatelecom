/**
 * @author Charles
 * @create 2019/4/2
 * @since 1.0.0
 */

package com.victor.chinatelecom.analysis.tool;

import com.victor.chinatelecom.analysis.Mapper.AnalysisBeanMapper;
import com.victor.chinatelecom.analysis.Mapper.AnalysisMapper;
import com.victor.chinatelecom.analysis.Reducer.AnalysisBeanReducer;
import com.victor.chinatelecom.analysis.Reducer.AnalysisReducer;
import com.victor.chinatelecom.analysis.io.MySQLBeanOutputFormat;
import com.victor.chinatelecom.analysis.io.MySQLTextOutputFormat;
import com.victor.chinatelecom.analysis.kv.AnalysisKey;
import com.victor.chinatelecom.analysis.kv.AnalysisValue;
import com.victor.chinatelecom.common.Constant.ConfigConstant;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobStatus;

public class AnalysisDataTool implements org.apache.hadoop.util.Tool {


    @Override
    public int run(String[] strings) throws Exception {
        Job job = Job.getInstance();

        job.setJarByClass(AnalysisDataTool.class);
        //设置mapper
        Scan scan = new Scan();
        scan.addFamily(Bytes.toBytes(ConfigConstant.getVal("ct.cf.caller")));
        TableMapReduceUtil.initTableMapperJob(
                ConfigConstant.getVal("ct.table"),
                scan,
                AnalysisBeanMapper.class,
                AnalysisKey.class,
                Text.class,
                job
        );

        //设置reducer
        job.setReducerClass(AnalysisBeanReducer.class);
        job.setOutputKeyClass(AnalysisKey.class);
        job.setOutputValueClass(AnalysisValue.class);

        //关联outputformat
        job.setOutputFormatClass(MySQLBeanOutputFormat.class);
        //提交.
        boolean result = job.waitForCompletion(true);
        return result ? JobStatus.State.SUCCEEDED.getValue() :
                JobStatus.State.FAILED.getValue();

    }

    @Override
    public void setConf(Configuration configuration) {

    }

    @Override
    public Configuration getConf() {
        return null;
    }




}
