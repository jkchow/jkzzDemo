package com.maimaicn.goods.configurations;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * 配置数据源
 * Druid的DataResource配置类
 * 凡是被Spring管理的类，实现接口 EnvironmentAware 重写方法 setEnvironment 可以在工程启动时，
 * 获取到系统环境变量和application配置文件中的变量。 还有一种方式是采用注解的方式获取 @value("${变量的key值}")
 * 获取application配置文件中的变量。 这里采用第一种要方便些
 */
@Configuration
@EnableTransactionManagement
public class DatasourceGoodsConfig implements EnvironmentAware {

    private static final String mybatisMapperLocations = "classpath:mappers/*.xml";
    private Properties property;

    public void setEnvironment(Environment environment) {
        property = Binder.get(environment).bind("spring.datasource", Properties.class).get();
    }

    /**
     * 配置datasource
     *
     * @return
     */
    @Bean("dataSourceGoods")
    @Primary //在同样的DataSource中，首先使用被标注的DataSource
    public DataSource dataSource() {
        DruidDataSource dds = new DruidDataSource();
        dds.setUrl(property.getProperty("url"));
        dds.setUsername(property.getProperty("username"));
        dds.setPassword(property.getProperty("password"));
        dds.setDriverClassName(property.getProperty("driver-class-name"));

        //初始化、最小、最大链接数量
        dds.setInitialSize(10);
        dds.setMinIdle(10);
        dds.setMaxActive(1000);
        dds.setMaxWait(50000);//配置获取链接等待超时的时间
        dds.setTimeBetweenEvictionRunsMillis(50000);//配置多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
        dds.setMinEvictableIdleTimeMillis(300000);//配置连接在池中的最小生存时间
        dds.setValidationQuery("select 1 from DUAL");
        dds.setTestWhileIdle(true);
        dds.setTestOnBorrow(false);
        dds.setTestOnReturn(false);
        // #打开psCache，并且指定每个连接上psChache的大小
        dds.setPoolPreparedStatements(true);
        dds.setMaxPoolPreparedStatementPerConnectionSize(20);
        //配置监控统计拦截的filters，去掉后监控界面sql将无法统计，‘wall'用于防火墙
//        dds.setFilters("stat,wall,log4j");
//        dds.setConnectProperties("druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000");
        return dds;
    }

    /**
     *  注入mybatis的sqlSessionFactory和sqlSession，这里直接将2步和一步
     * @param dataSource
     * @return SqlSession
     * @throws Exception
     */
    @Bean("sqlSession")
    public SqlSession sqlSession(@Qualifier("dataSourceGoods") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);

        //配置mapper文件位置
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactory.setMapperLocations(resolver.getResources(mybatisMapperLocations));

        //配置sqlSession
        SqlSession sqlSession = new SqlSessionTemplate(sqlSessionFactory.getObject());
        return sqlSession;
    }

    /**
     * 配置事物管理
     * @param dataSource
     * @return DataSourceTransactionManager
     */
    @Bean("txManagerGoods")
    public DataSourceTransactionManager transactionManager(@Qualifier("dataSourceGoods") DataSource dataSource) {
        DataSourceTransactionManager dstm = new DataSourceTransactionManager();
        dstm.setDataSource(dataSource);
        return dstm;
    }


}
