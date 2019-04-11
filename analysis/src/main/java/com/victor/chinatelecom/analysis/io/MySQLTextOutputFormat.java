/**
 * @author Charles
 * @create 2019/4/2
 * @since 1.0.0
 */

package com.victor.chinatelecom.analysis.io;

import com.victor.chinatelecom.common.Utils.JDBCUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.output.FileOutputCommitter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MySQLTextOutputFormat extends OutputFormat<Text,Text> {
    private FileOutputCommitter committer = null;
    private static Connection conn = null;
    private static PreparedStatement pstat = null;
    private static Map<String, Integer> dateMap = new HashMap<String, Integer>();
    private static Map<String, Integer> userMap = new HashMap<String, Integer>();

//    public MySQLTextOutputFormat(){
//    }

    protected static class MySQLRecordWriter extends RecordWriter<Text, Text>{

//        static {
//        }
        public MySQLRecordWriter(){
            try {
                conn = JDBCUtil.getConnection();

                String queryTimeSQL = "select id,year,month,day from ct_date";
                String queryUserSQL = "select id,telephone,name from ct_user";

                pstat=conn.prepareStatement(queryUserSQL);
                ResultSet userResultSet = pstat.executeQuery();
                while(userResultSet.next()){
                    int userID = userResultSet.getInt(1);
                    String tel = userResultSet.getString(2);
    //                    String username = userResultSet.getString(2);
                    userMap.put(tel.trim(),userID);
                }
                userResultSet.close();

                pstat=conn.prepareStatement(queryTimeSQL);
                ResultSet timeResultSet = pstat.executeQuery();
                while(timeResultSet.next()){
                    int dateID = timeResultSet.getInt(1);
                    String year = timeResultSet.getString(2);
                    String month = timeResultSet.getString(3);
                    String day = timeResultSet.getString(4);

                    //处理格式 20180101
                    //补全日期空位
                    if (month.length()==1)
                        month="0"+month;
                    if (day.length()==1)
                        day="0"+day;
    //                    String username = userResultSet.getString(2);
                    dateMap.put(year+month+day,dateID);
                }
                timeResultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void write(Text key, Text value) throws IOException, InterruptedException {
            try {
                String insertSQL = "insert into ct_call (telid,dateid,sumcall,sumduration) values (?,?,?,?);";

                //caller  -     calltime,  times - duration
                String[] fields = value.toString().split("-");
                int sumCall = Integer.parseInt(fields[0]);
                int sumDuration = Integer.parseInt(fields[1]);

                pstat = conn.prepareStatement(insertSQL);

                //       key            ,       Value
                //caller  -     calltime,  times - duration
                //133     -     201806,         50min

                String[] splits = key.toString().split("-");
                Integer userID = userMap.get(splits[0]);
                Integer dateID = dateMap.get(splits[1]);


                //设置传入的数据
                pstat.setInt(1,userID);
                pstat.setInt(2,dateID);
                pstat.setInt(3,sumCall);
                pstat.setInt(4,sumDuration);
//                System.out.println("=============="+2);

                pstat.executeUpdate();
//                System.out.println("=============="+pstat);

            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                try {
                    pstat.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void close(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
            if (conn!=null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    public RecordWriter<Text, Text> getRecordWriter(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        return new MySQLRecordWriter();
    }

    @Override
    public void checkOutputSpecs(JobContext jobContext) throws IOException, InterruptedException {

    }

    @Override
    public OutputCommitter getOutputCommitter(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {

        if (this.committer == null) {
            Path output = getOutputPath(taskAttemptContext);
            this.committer = new FileOutputCommitter(output, taskAttemptContext);
        }

        return this.committer;
    }

    public static Path getOutputPath(JobContext job) {
        String name = job.getConfiguration().get("mapreduce.output.fileoutputformat.outputdir");
        return name == null ? null : new Path(name);
    }
}
