package com.excelhk.openapi.demoservice.service;

import com.excelhk.openapi.demoservice.bean.Loan;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LoanServiceTest {

    @Autowired
    LoanService loanService;

    @Test
    public void findByProdId() {
        List<Loan> loans = loanService.findByProdId("L1");
        Assert.assertEquals(1, loans.size());
        Assert.assertEquals("Loans", loans.get(0).getProduct());
    }

    @Test
    public void findAllProdId() {
        List<Loan> loans = loanService.findAllProdId();
        Assert.assertEquals(2,loans.size());
        Assert.assertEquals("L1", loans.get(0).getProdId());
        Assert.assertEquals("L2", loans.get(1).getProdId());
        Assert.assertNull(loans.get(0).getProduct());
        Assert.assertNull(loans.get(1).getProduct());
        Assert.assertNotEquals("Loans", loans.get(0).getProduct());
        Assert.assertNotEquals("Loans", loans.get(1).getProduct());

    }
}