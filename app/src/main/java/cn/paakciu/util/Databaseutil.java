package cn.paakciu.util;

import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import cn.paakciu.POJO.poster;
import cn.paakciu.POJO.task;
import cn.paakciu.config.dataBaseConfig;

public class Databaseutil {

    private static String driver = dataBaseConfig.driver;// MySql驱动名
    private static String user = dataBaseConfig.user;// 用户名
    private static String password = dataBaseConfig.password;// 密码
    private static String ip = dataBaseConfig.ip;//ip地址
    private static String port = dataBaseConfig.port;//端口
    //private static Connection connection = null;
    //private static Thread thread_getConnection=null;


    private static Connection getConn(final String dbName){
        try {
            Class.forName(driver);// 动态加载类  注册数据库，加载不同版本的驱动到jdbc核心中
        } catch (ClassNotFoundException e) {
            Log.e("Databaseutil","动态加载类错误");
            e.printStackTrace();
        }
        // 连接JDBC
        Connection connection=null;
        try {
            String url="jdbc:mysql://"+ip+":"+port+"/"+dbName;
            connection=  DriverManager.getConnection(url, user, password);
            Log.i("Databaseutil", "远程连接成功!");
            return connection;
        } catch (SQLException e) {
            Log.e("Databaseutil", "远程连接失败!");
        }
        return connection;
    }

    //Log.e("Databaseutil","");
    public static HashMap<String, Object>[] getInfo( String Tablename,String key,String value){
        // 根据数据库名称，建立连接
        Connection connection = getConn("beta");
        //Log.e("Databaseutil","建立数据库连接");
        try {
            String Separator=" and ";
            // mysql简单的查询语句。
            String sql = "select * from "+Tablename+" where 1=1"+Separator+key+" = ?";
            if (connection != null){// connection不为null表示与数据库建立了连接
                //Log.e("Databaseutil","连接成功");
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null){
                    // 设置上面的sql语句中的？的值为name
                    ps.setString(1, value);
                    // 执行sql查询语句并返回结果集
                    ResultSet rs = ps.executeQuery();
                    if (rs != null){
                        //取出数据的总列数
                        int count = rs.getMetaData().getColumnCount();
                        //Log.e("Databaseutil","列总数：" + count);

                        //取出数据的总行数
                        rs.last();// 移动到最后
                        int rcount=rs.getRow();// 获得结果集长度,要上下移动才能取得到这个行数
                        rs.beforeFirst();// 返回第一个（记住不是rs.frist()）,不写的话下面的循环里面没值
                        //Log.i("Databaseutil","行总数：" + rcount);
                        if(rcount==0)
                            return null;
                        HashMap<String, Object>[] map = new HashMap[rcount];

                        int j=0;
                        boolean flag=true;
                        //每一行
                        while (rs.next()){
                            map[j]=new HashMap<String, Object>();
                            // 每一列 --注意：下标是从1开始的
                            for (int i = 1;i <= count;i++){
                                //取出列名
                                String field = rs.getMetaData().getColumnName(i);
                                //Log.i("Databaseutil","i：" + i);
                                //Log.i("Databaseutil","field：" + field);
                                //Log.i("Databaseutil","value：" + rs.getString(field));
                                //插入其中
                                if(rs.getString(field)!=null)
                                    map[j].put(field, rs.getString(field));
                                else
                                    flag=false;
                            }
                            if(flag) j++;
                        }

                        rs.close();
                        connection.close();
                        ps.close();
                        return  map;
                    }else {
                        connection.close();
                        ps.close();
                        return null;
                    }
                }else {
                    connection.close();
                    return  null;
                }
            }else {
                //Log.e("Databaseutil","连接不成功");
                return  null;
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.e("Databaseutil","异常：" + e.getMessage());
            return null;
        }

    }

    //Log.e("Databaseutil","");
    public static HashMap<String, Object>[] getInfoByArray( String Tablename,String[] keys,String[] values){
        //如果键值对数量不一致则直接返回null
        if(keys.length!=values.length)
            return null;
        // 根据数据库名称，建立连接
        Connection connection = getConn("beta");
        //Log.e("Databaseutil","建立数据库连接");
        try {
            String Separator=" and ";
            StringBuffer sb_keys=new StringBuffer();
            for(String key:keys)
            {
                sb_keys.append(Separator);
                sb_keys.append(key);
                sb_keys.append(" = ?");
            }
            // mysql简单的查询语句。
            String sql = "select * from "+Tablename+" where 1=1"+sb_keys.toString();
            if (connection != null){// connection不为null表示与数据库建立了连接
                //Log.e("Databaseutil","连接成功");
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null){
                    for(int i=1;i<values.length;i++)
                    // 设置上面的sql语句中的？的值为name
                    ps.setString(i, values[i]);
                    // 执行sql查询语句并返回结果集
                    ResultSet rs = ps.executeQuery();
                    if (rs != null){
                        //取出数据的总列数
                        int count = rs.getMetaData().getColumnCount();
                        //Log.e("Databaseutil","列总数：" + count);

                        //取出数据的总行数
                        rs.last();// 移动到最后
                        int rcount=rs.getRow();// 获得结果集长度,要上下移动才能取得到这个行数
                        rs.beforeFirst();// 返回第一个（记住不是rs.frist()）,不写的话下面的循环里面没值
                        //Log.i("Databaseutil","行总数：" + rcount);

                        if(rcount==0)
                            return null;
                        HashMap<String, Object>[] map = new HashMap[rcount];
                        int j=0;
                        boolean flag=true;
                        //每一行
                        while (rs.next()){
                            map[j]=new HashMap<String, Object>();
                            // 每一列 --注意：下标是从1开始的
                            for (int i = 1;i <= count;i++){
                                //取出列名
                                String field = rs.getMetaData().getColumnName(i);
                                //Log.i("Databaseutil","i：" + i);
                                //Log.i("Databaseutil","field：" + field);
                                //Log.i("Databaseutil","value：" + rs.getString(field));
                                //插入其中
                                if(rs.getString(field)!=null)
                                    map[j].put(field, rs.getString(field));
                                else
                                    flag=false;
                            }
                            if(flag) j++;
                        }

                        rs.close();
                        connection.close();
                        ps.close();
                        return  map;
                    }else {
                        connection.close();
                        ps.close();
                        return null;
                    }
                }else {
                    connection.close();
                    return  null;
                }
            }else {
                //Log.e("Databaseutil","连接不成功");
                return  null;
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.e("Databaseutil","异常：" + e.getMessage());
            return null;
        }

    }

    public static HashMap<String, Object>[] getPosterInfo( String Tablename,poster p){
        // 根据数据库名称，建立连接
        Connection connection = getConn("beta");
        //Log.e("Databaseutil","建立数据库连接");
        try {
            // mysql简单的查询语句。
            String sql = "select * from "+Tablename+" where account = ? and password = ?";
            if (connection != null){// connection不为null表示与数据库建立了连接
                //Log.e("Databaseutil","连接成功");
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null){
                    // 设置上面的sql语句中的？的值为name
                    ps.setString(1, p.account);
                    ps.setString(2, p.password);
                    // 执行sql查询语句并返回结果集
                    ResultSet rs = ps.executeQuery();
                    if (rs != null){
                        //取出数据的总列数
                        int count = rs.getMetaData().getColumnCount();
                        //Log.e("Databaseutil","列总数：" + count);

                        //取出数据的总行数
                        rs.last();// 移动到最后
                        int rcount=rs.getRow();// 获得结果集长度,要上下移动才能取得到这个行数
                        rs.beforeFirst();// 返回第一个（记住不是rs.frist()）,不写的话下面的循环里面没值
                        //Log.i("Databaseutil","行总数：" + rcount);

                        if(rcount==0)
                            return null;
                        HashMap<String, Object>[] map = new HashMap[rcount];
                        int j=0;
                        boolean flag=true;
                        //每一行
                        while (rs.next()){
                            map[j]=new HashMap<String, Object>();
                            // 每一列 --注意：下标是从1开始的
                            for (int i = 1;i <= count;i++){
                                //取出列名
                                String field = rs.getMetaData().getColumnName(i);
                                //插入其中
                                if(rs.getString(field)!=null)
                                    map[j].put(field, rs.getString(field));
                                else
                                    flag=false;
                            }
                            if(flag) j++;
                        }

                        rs.close();
                        connection.close();
                        ps.close();
                        return  map;
                    }else {
                        connection.close();
                        ps.close();
                        return null;
                    }
                }else {
                    connection.close();
                    return  null;
                }
            }else {
                //Log.e("Databaseutil","连接不成功");
                return  null;
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.e("Databaseutil","异常：" + e.getMessage());
            return null;
        }

    }
    public static boolean putInfo(String Tablename,String values[])
    {
        return true;
    }

    public static void taskInsert(task t)  {
        Connection connection = getConn("beta");
        String sql = "INSERT INTO task (taskID,主标题,联系方式,工资) VALUES(NULL,?,?,?)";		//插入sql语句
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            /**
             * 调用实体task类,获取需要插入的各个字段的值
             * 注意参数占位的位置
             * 通过set方法设置参数的位置
             * 通过get方法取参数的值
             */
            ps.setString(1, t.title);
            ps.setString(2, t.phone);
            ps.setString(3, t.salary);
            ps.executeUpdate();			//执行sql语句
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void postInsert(poster p)  {
        Connection connection = getConn("beta");
        String sql = "INSERT INTO poster (posterID,account,password) VALUES(NULL,?,?)";		//插入sql语句
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            /**
             * 调用实体task类,获取需要插入的各个字段的值
             * 注意参数占位的位置
             * 通过set方法设置参数的位置
             * 通过get方法取参数的值
             */
            ps.setString(1, p.account);
            ps.setString(2, p.password);
            //ps.setString(3, t.salary);
            ps.executeUpdate();			//执行sql语句
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Boolean taskDelete(String taskID) {
        Log.e("Databaseutil",taskID);
        // 定义标记
        boolean flag = false;
        PreparedStatement ps=null;
        // 获取连接对象
        Connection connection = getConn("beta");
        // 定义删除SQL语句
        String sql = "DELETE FROM task WHERE taskID = ?";
        try{
            if(connection!=null)
            {
                // 获取操作对象
                ps = connection.prepareStatement(sql);
                // 给SQL语句设置值
                ps.setString(1, taskID);
                // 执行SQL语句
                int state = ps.executeUpdate();
                if(state>0) {
                    // 删除成功 提交事务
                    flag = true;
                    //提交事务
                    //connection.commit();
                    connection.close();
                }else{
                    // 删除失败 回滚事务
                    flag = false;
                    //connection.rollback();
                    connection.close();
                }
            }
        } catch (SQLException e)
        {

        }
        return flag;
    }

}
