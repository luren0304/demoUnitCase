package com.excelhk.openapi.demoservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;


/**
 * @author anita
 *
 */
@SpringBootApplication
@IntegrationComponentScan
@EnableIntegration
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
    /*public @Bean
    Jackson2RepositoryPopulatorFactoryBean repositoryPopulator() {
        System.out.println("repositoryPopulator");
        Jackson2RepositoryPopulatorFactoryBean factoryBean = new Jackson2RepositoryPopulatorFactoryBean();
        //factoryBean.setResources(new Resource[] { new ClassPathResource("Deposits.json") });
        factoryBean.setResources(new Resource[] { new ClassPathResource("init-data.json") });
        return factoryBean;
    }*/
}
