package com.excelhk.openapi.demoservice.repository;

import com.excelhk.openapi.demoservice.bean.Loan;
import com.excelhk.openapi.demoservice.utils.constants.DemoConstants;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@DataMongoTest
public class LoanRepositoryUnitTest {

    @Autowired
    private LoanRepository loanRepository;

    @Test
    public void whenFindByProdId_thenReturnLoans(){

        // When
        List<Loan> loans = loanRepository.findByProdId("L1");

        // Then
        loans.forEach(loan -> {
            System.out.println(loan);
            Assert.assertEquals(DemoConstants.PROD_TYPE_LOANS, loan.getProduct());
        });
    }

    @Test
    public void whenFindAllProdId_thenReturnLoans(){

        // Expected data
        List<String> stringList = Arrays.asList("L1","L2");

        // When
        List<Loan> loans = loanRepository.findAllprodId();

        // Then
        int i = -1;
        for (Loan loan: loans
        ) {
            System.out.println(loan);
            Assert.assertEquals(stringList.get(++i), loan.getProdId());
        }
    }
}