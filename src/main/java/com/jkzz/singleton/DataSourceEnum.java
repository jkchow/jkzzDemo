package com.jkzz.singleton;

/**
 * 声明一个枚举，用于获取数据库连接。
 */
public enum DataSourceEnum {
    DATASOURCE;
    private DBConnection connection;
    DataSourceEnum(){
        connection=new DBConnection();
    }
    public DBConnection getConnection(){
        return  connection;
    }
}
