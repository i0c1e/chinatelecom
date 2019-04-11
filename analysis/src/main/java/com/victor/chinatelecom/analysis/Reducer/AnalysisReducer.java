/**
 * @author Charles
 * @create 2019/4/2
 * @since 1.0.0
 */

package com.victor.chinatelecom.analysis.Reducer;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class AnalysisReducer extends Reducer<Text, Text, Text, Text> {
    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

        int sumCall = 0;
        int sumDuration = 0;
        //       key            ,       Value
        //caller  -     calltime,  times - duration
        //133     -     201806,         50min
        for (Text value : values) {
            sumDuration += Integer.parseInt(value.toString());
            ++sumCall;
        }
        System.out.println("reducer key:" + key +",value: " + sumCall + "-" + sumDuration);
        context.write(key, new Text(sumCall + "-" + sumDuration));
    }
}
