package com.xtyuns.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class BaseDao {
    private static String url;
    private static String user;
    private static String pwd;

    private static Connection conn;
    private static PreparedStatement pstm;

    // 初始化MySQL连接参数
    static {
        init();
    }

    private static void init() {
        InputStream is = BaseDao.class.getClassLoader().getResourceAsStream("mysql.properties");
        Properties params = new Properties();
        try {
            params.load(is);
            url = params.getProperty("url");
            user = params.getProperty("user");
            pwd = params.getProperty("pwd");
            Class.forName(params.getProperty("drive"));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // 获取数据库连接对象
    private static Connection getConnection() {
        conn = null;
        try {
            conn = DriverManager.getConnection(url, user, pwd);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return conn;
    }

    // 释放资源
    private static void closeResource() {
        if (pstm != null) {
            try {
                pstm.close();
                pstm = null;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
                conn = null;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    /**
     * 数据库查询操作
     * @param sql sql预执行语句
     * @param params sql语句参数
     * @return 返回查询结果集合
     * @throws SQLException sql语句执行异常
     */
    public static ResultSet executeQuery(String sql, Object[] params) throws SQLException {
        conn = getConnection();
        pstm = conn.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            pstm.setObject(i+1, params[i]);
        }

        return pstm.executeQuery();
    }

    /**
     * 数据库更新操作
     * @param sql sql预执行语句
     * @param params sql语句参数
     * @return 返回更新操作所影响的行数
     * @throws SQLException sql语句执行异常
     */
    public static int executeUpdate(String sql, Object[] params) throws SQLException {
        conn = getConnection();
        pstm = conn.prepareCall(sql);
        for (int i = 0; i < params.length; i++) {
            pstm.setObject(i+1, params[i]);
        }

        return pstm.executeUpdate();
    }

    /**
     * 释放查询结果集合的资源
     * @param rs sql查询结果集
     * @return 返回是否成功释放资源
     */
    public static boolean closeResultSet(ResultSet rs) {
        boolean flag = true;

        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                flag = false;
            }
        }

        closeResource();

        return flag;
    }
}
