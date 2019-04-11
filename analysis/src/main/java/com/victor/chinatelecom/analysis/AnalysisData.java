/**
 * @author Charles
 * @create 2019/4/2
 * @since 1.0.0
 */

package com.victor.chinatelecom.analysis;

import com.victor.chinatelecom.analysis.tool.AnalysisDataTool;
import org.apache.hadoop.util.ToolRunner;

public class AnalysisData {
    public static void main(String[] args) throws Exception {
        ToolRunner.run(new AnalysisDataTool(), args);


    }
}
