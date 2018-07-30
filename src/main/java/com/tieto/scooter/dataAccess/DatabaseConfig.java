package com.tieto.scooter.dataAccess;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Bean
    public SpringLiquibase liquibase(DataSource datasource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:db/changeLog.xml");
        liquibase.setDataSource(datasource);
        return liquibase;
    }

}
