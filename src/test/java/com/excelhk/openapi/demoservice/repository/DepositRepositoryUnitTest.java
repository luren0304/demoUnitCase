package com.excelhk.openapi.demoservice.repository;

import com.excelhk.openapi.demoservice.MongoTestConfiguration;
import com.excelhk.openapi.demoservice.bean.Deposit;
import com.excelhk.openapi.demoservice.utils.constants.DemoConstants;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@DataMongoTest
@ContextConfiguration(classes = { MongoTestConfiguration.class })
public class DepositRepositoryUnitTest {

    @Autowired
    private DepositRepository depositRepository;

    @Test
    public void whenFindByProdId_thenReturnDeposits(){

        // When
        List<Deposit> deposits = depositRepository.findByProdId("D1");
        System.out.println("deposits.size(): " + deposits.size());
        //then
        for (Deposit deposit:deposits
             ) {
            System.out.println(deposit);
            Assertions.assertThat(deposit.getProduct()).isEqualTo(DemoConstants.PROD_TYPE_DEPOSIT);
        }

    }

    @Test
    public void whenFindAllProdId_thenReturnDeposits(){

        List<String> stringList = Arrays.asList("D1","D2","D3");

        // When
        List<Deposit> deposits = depositRepository.findAllprodId();
        //then
        int i = -1;
        for (Deposit deposit:deposits
        ) {
            System.out.println(deposit);
            Assertions.assertThat(deposit.getProdId()).isEqualTo(stringList.get(++i));
        }

    }
}