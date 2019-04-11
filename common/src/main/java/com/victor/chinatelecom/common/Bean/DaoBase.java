/**
 * @author Charles
 * @create 2019/3/29
 * @since 1.0.0
 */

package com.victor.chinatelecom.common.Bean;

import com.victor.chinatelecom.common.API.Column;
import com.victor.chinatelecom.common.API.Rowkey;
import com.victor.chinatelecom.common.API.TargetRef;
import com.victor.chinatelecom.common.Constant.ConfigConstant;
import com.victor.chinatelecom.common.Utils.DateUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public abstract class DaoBase {
    ThreadLocal<Connection> connHolder = new ThreadLocal<Connection>();
    ThreadLocal<Admin> adminHolder = new ThreadLocal<Admin>();

    protected void start() throws Exception {
        getConnection();
        getAdmin();
    }

    protected void end() throws Exception {
        Connection conn = getConnection();
        Admin admin = getAdmin();
        if (conn != null) {
            conn.close();
            connHolder.remove();
        }
        if (admin != null)
            admin.close();
        adminHolder.remove();
    }

    //获取连接
    protected synchronized Connection getConnection() throws Exception {
        Connection conn = null;

        conn = connHolder.get();
        if (conn == null) {
            Configuration conf = HBaseConfiguration.create();
            conn = ConnectionFactory.createConnection(conf);
            connHolder.set(conn);
        }
        conn = connHolder.get();
        return conn;
    }

    //获取Admin
    protected synchronized Admin getAdmin() throws Exception {
        Admin admin = null;

        admin = adminHolder.get();
        if (admin == null) {
            Configuration conf = HBaseConfiguration.create();
            admin = getConnection().getAdmin();
            adminHolder.set(admin);
        }
        admin = adminHolder.get();
        return admin;
    }

    /**
     * 创建表, 若存在则重建, 否则新建
     *
     * @param tableName
     * @param family
     * @throws Exception
     */
    protected void createTableXX(String tableName, String coprocessorName, String... family) throws Exception {
        //创建新表
        createTableXX(tableName,coprocessorName, null, family);

    }

//    protected void createTable(String tableName, String... family) throws Exception {
//        createTable(tableName, null, family);
//    }

    protected void createTableXX(String tableName, String coprocessorName, Integer splitCount, String... family) throws Exception {
        Admin admin = getAdmin();
        TableName table = TableName.valueOf(tableName);
        //若存在则删除
//        if (admin.tableExists(table))
//            delete(tableName);

//
//        Admin admin = getAdmin();
//        TableName table = TableName.valueOf(tableName);

        //判断family的长度
        HTableDescriptor desc = new HTableDescriptor(tableName);
        if (family == null || family.length == 0) {
            family = new String[1];
            family[0] = ConfigConstant.getVal("ct.cf.info");
        }
        for (String s : family) {
            HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(s);
            desc.addFamily(hColumnDescriptor);
        }

        if (coprocessorName != null || !"".equals(coprocessorName))
            desc.addCoprocessor(coprocessorName);

        byte[][] bytes = null;

        //判断是否需要进行分区
        if (splitCount == null || splitCount <= 1) {
            createTable(desc);
        } else {
            bytes = generateSplit(splitCount);
            createTable(desc, bytes);
        }
    }

    private void createTable(HTableDescriptor desc) throws Exception {
        createTable(desc, null);
    }

    private void createTable(HTableDescriptor desc, byte[][] bytes) throws Exception {
        Admin admin = getAdmin();
        TableName tableName = TableName.valueOf(ConfigConstant.getVal("ct.table"));
        if (admin.tableExists(tableName)) {
            admin.disableTable(tableName);
            System.out.println("table was exist, deleting");
            admin.deleteTable(tableName);
        }
        if (bytes == null) {
            admin.createTable(desc);
        } else {
            admin.createTable(desc, bytes);
        }
    }


    //生成分区键
    private byte[][] generateSplit(int splitCount) {
        byte[][] split = new byte[splitCount - 1][];
        //00011
        //11111
        //21221
        //1|2
        List<byte[]> list = new ArrayList<byte[]>();
        for (int i = 0; i < splitCount - 1; i++) {
            String tmp = i + "|";
            list.add(Bytes.toBytes(tmp));
        }
        return list.toArray(split);
    }


    private void delete(String tableName) throws Exception {
        Admin admin = getAdmin();
        TableName table = TableName.valueOf(tableName);

        admin.disableTable(table);
        admin.deleteTable(table);
    }

    /**
     * 创建命名空间,若不存在则创建
     *
     * @param nameSpace
     * @throws Exception
     */
    protected void createNamespaceNX(String nameSpace) throws Exception {
        Admin admin = getAdmin();
//        NamespaceDescriptor namespaceDescriptor ;
        try {
            admin.getNamespaceDescriptor(nameSpace);

        } catch (IOException e) {
            System.out.println("namespace not exist ,creating...");
            admin.createNamespace(NamespaceDescriptor.create(nameSpace).build());
        }


    }

//    public void insert(String value) {
//        insert(ConfigConstant.getVal("ct.table"),va);
//    }

    public static int getRegionNum(String tell, String date) {
        String usercode = tell.substring(tell.length() - 4);
        String datecode = date.substring(0, 6);
        int usercodeHash = usercode.hashCode();
        int datecodeHash = datecode.hashCode();

        //crc校验算法 异或 hash
        int crc = Math.abs(usercodeHash ^ datecodeHash);
//        System.out.println(usercodeHash + "," + datecodeHash + "," + crc);
        int len = Integer.parseInt(ConfigConstant.getVal("ct.regionNumber"));
//        System.out.println("len:" + len);
//        return (crc >>> 29) ^ len;
        return crc % len;
    }

    protected void putData(String tableName, Put put) throws Exception {
        //获取连接
        Connection conn = getConnection();
        //获取表
        Table table = conn.getTable(TableName.valueOf(tableName));
        //增加数据
        table.put(put);
        //关闭表
        table.close();

    }

    protected void putData(String tableName, List<Put> puts) throws Exception {
        //获取连接
        Connection conn = getConnection();
        //获取表
        Table table = conn.getTable(TableName.valueOf(tableName));
        //增加数据
        table.put(puts);
        //关闭表
        table.close();

    }

    /**
     * 增加对象,将对象中数据封装到hbase
     *
     * @param obj
     * @throws Exception
     */
    protected void putData(Object obj) throws Exception {
        String rk = "";

        //获取连接
        Connection conn = getConnection();
        //获取表
        //通过反射获取类的注解
        Class<?> clazz = obj.getClass();

        TargetRef targetRef = clazz.getAnnotation(TargetRef.class);
        String tableName = targetRef.value();

        Table table = conn.getTable(TableName.valueOf(tableName));
        //增加数据

        Field[] fs = clazz.getDeclaredFields();
        for (Field field : fs) {
            Rowkey annotation = field.getAnnotation(Rowkey.class);
            //遍历注解, 当注解不为null时,就是rowkey,获取后跳出
            if (annotation != null) {
                field.setAccessible(true);
                rk = (String) field.get(obj);
                break;
            }
        }
//        System.out.println("insert rk:" + rk);
        byte[] rowkey = Bytes.toBytes(rk);

        Put put = new Put(rowkey);
        //遍历注解获取column
        for (Field f : fs) {
            Column column = f.getAnnotation(Column.class);
            if (column != null) {
                String colName = column.column();
                //若colName为null或为默认值, 则使用列名
                if (colName == null || "".equals(colName))
                    colName = f.getName();

                //设置可见性
                f.setAccessible(true);
                String val = (String) f.get(obj);

                put.addColumn(
                        Bytes.toBytes(column.family()),
                        Bytes.toBytes(colName),
                        Bytes.toBytes(val)
                );
            }

        }

        table.put(put);
        //关闭表
        table.close();

    }

    /**
     * 给定查询所需开始结束日期,返回查询开始结束rowkey数组
     * 日期格式yyyyMMddHHmmss
     * 给定18610200133,start 201806,end 201807返回
     * 2_18610200133_201806~2_18610200133_201806|
     * 1_18610200133_201807~1_18610200133_201807|
     *
     * @param tel
     * @param start
     * @param end
     * @return
     */
    protected static List<String[]> getStartStopRowkeys(String tel, String start, String end) {
        ArrayList<String[]> list = new ArrayList<String[]>();

        //使用日期和电话号码拼接rowkey
//        String subtel = tel.substring(tel.length() - 4);

        //将start和end日期封装为calendar对象,以便加减操作
        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        startCal.setTime(DateUtil.parse(start, "yyyyMMddHHmmss"));
        endCal.setTime(DateUtil.parse(end, "yyyyMMddHHmmss"));

        while (startCal.getTimeInMillis() <= endCal.getTimeInMillis()) {
            //每次循环获取最新的开始日期
            Date startDate = startCal.getTime();
            String subStart = DateUtil.format(startDate, "yyyyMM");

            int regionNum = getRegionNum(tel, subStart);
            //拼接rowkey
            // e.g. 3_201802 - 3_201802|
            String startKey = regionNum + "_" + tel + "_" + subStart;
            String endKey = startKey + "|";
            String[] rks = {startKey, endKey};

            list.add(rks);
            startCal.add(Calendar.MONTH, 1);
        }


        return list;
    }

    public static void main(String[] args) {
//        byte[][] bytes = generateSplit(6);
//        for (byte[] aByte : bytes) {
//            System.out.println(Bytes.toString(aByte));
//        }
//        System.out.println(bytes);

        List<String[]> rkss = getStartStopRowkeys("18610200133", "20180603112233", "20190103112233");
        for (String[] s : rkss) {
            System.out.println(s[0] + "~" + s[1]);
        }
    }
}
