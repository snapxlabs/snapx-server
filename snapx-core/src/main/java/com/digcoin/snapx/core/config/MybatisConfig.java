package com.digcoin.snapx.core.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.digcoin.snapx.core.mybatis.SaveOrUpdateMetaObjectHandler;
import com.digcoin.snapx.core.web.CurrentUserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisConfig {

    @Value("${app.mybatis-plus.snowflake.worker:0}")
    private Long workerId;

    @Value("${app.mybatis-plus.snowflake.datacenter:0}")
    private Long datacenterId;

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setDbType(DbType.MYSQL);
        mybatisPlusInterceptor.addInnerInterceptor(paginationInnerInterceptor);
        return mybatisPlusInterceptor;
    }

    @Bean
    public ConfigurationCustomizer mybatisPlusConfigurationCustomizer(@Autowired(required = false) CurrentUserProvider currentUserProvider) {
        return configuration -> GlobalConfigUtils.getGlobalConfig(configuration)
                .setMetaObjectHandler(new SaveOrUpdateMetaObjectHandler(currentUserProvider))
                .setIdentifierGenerator(identifierGenerator())
                .getDbConfig()
                .setTableUnderline(true)
                .setCapitalMode(false)
                .setLogicDeleteField("deleted")
                .setLogicDeleteValue("UNIX_TIMESTAMP()")
                .setLogicNotDeleteValue("0")
                .setIdType(IdType.ASSIGN_ID)
                .setInsertStrategy(FieldStrategy.NOT_NULL)
                .setUpdateStrategy(FieldStrategy.NOT_NULL)
                .setWhereStrategy(FieldStrategy.NOT_NULL);
    }

    @Bean
    public IdentifierGenerator identifierGenerator() {
        return new DefaultIdentifierGenerator(workerId, datacenterId);
    }

}
