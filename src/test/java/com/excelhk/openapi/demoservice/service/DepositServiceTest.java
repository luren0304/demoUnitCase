package com.excelhk.openapi.demoservice.service;

import com.excelhk.openapi.demoservice.bean.Deposit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DepositServiceTest {

    @Autowired
    DepositService depositService;

    @Test
    public void findByProdId() {
        System.out.println(depositService.findAll().size());
        List<Deposit> depositList = depositService.findByProdId("D1");
        Deposit deposit = depositList.get(0);
        System.out.println(deposit);
        Assert.assertEquals("D1", deposit.getProdId());
        Assert.assertEquals("Deposits", deposit.getProduct());
        Assert.assertEquals("No monthly service fee if you maintain a balance of more than HKD5,000 (rolling average of previous 3 months, if not you'll pay a fee of HKD50)", deposit.getRemark());
        depositList = depositService.findByProdId("D2");
        deposit = depositList.get(0);
        System.out.println(deposit);;
        Assert.assertEquals("D2", deposit.getProdId());
        Assert.assertEquals("Deposits", deposit.getProduct());
        Assert.assertEquals("Earn bonus interest on your account if your Total Relationship Balance is HKD1,000,000 or above", deposit.getRemark());
    }

    @Test
    public void findAllProdId() {
        System.out.println(depositService.findAll().size());
        List<Deposit> depositList = depositService.findAllProdId();
        Deposit deposit = depositList.get(0);
        System.out.println(deposit);
        Assert.assertEquals("D1", deposit.getProdId());
        Assert.assertNotEquals("Deposit", deposit.getProduct());
        Assert.assertNotEquals("No monthly service fee if you maintain a balance of more than HKD5,000 (rolling average of previous 3 months, if not you'll pay a fee of HKD50)", deposit.getRemark());
        Assert.assertNull(deposit.getProduct());
        Assert.assertNull(deposit.getRemark());
        deposit = depositList.get(1);
        System.out.println(deposit);
        Assert.assertEquals("D2", deposit.getProdId());
        Assert.assertNotEquals("Deposit", deposit.getProduct());
        Assert.assertNotEquals("Earn bonus interest on your account if your Total Relationship Balance is HKD1,000,000 or above", deposit.getRemark());
        Assert.assertNull(deposit.getProduct());
        Assert.assertNull(deposit.getRemark());
    }

}