package com.excelhk.openapi.demoservice;

import com.excelhk.openapi.demoservice.bean.Deposit;
import com.excelhk.openapi.demoservice.bean.FieldMapping;
import com.excelhk.openapi.demoservice.bean.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;

import javax.annotation.PreDestroy;

@Configuration
@EnableAutoConfiguration
public class MongoTestConfiguration {

    @Autowired
    MongoOperations operations;

   public @Bean Jackson2RepositoryPopulatorFactoryBean repositoryPopulator() {
       System.out.println("repositoryPopulator");
       Jackson2RepositoryPopulatorFactoryBean factoryBean = new Jackson2RepositoryPopulatorFactoryBean();
       factoryBean.setResources(new Resource[] { new ClassPathResource("init-data.json") });
       return factoryBean;
   }

    /**
     * Clean up after execution by dropping used test db instance.
     *
     */
    @PreDestroy
    void dropTestDB(){
        operations.dropCollection(Deposit.class);
        operations.dropCollection(Loan.class);
        operations.dropCollection(FieldMapping.class);
    }
}
