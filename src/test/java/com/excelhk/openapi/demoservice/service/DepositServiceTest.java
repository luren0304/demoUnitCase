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
    //@Autowired
    //DepositRepository depositRepository;


   /* @Test
    public void createDeposit() {
        Deposit deposit = new Deposit();
        deposit.setProdId("D1");
        depositService.createDeposit(deposit);
        Deposit deposit1 = depositService.findByProdId(deposit.getProdId()).get(0);
        Assert.assertEquals("D1", deposit1.getProdId());

    }

    @Test
    public void createDeposit1() {
        System.out.println(depositService.findAll());
        Assert.assertEquals(0, depositService.findAll().size());
        List<Deposit> deposits = new ArrayList<Deposit>();
        Deposit deposit = new Deposit();
        deposit.setProdId("D1");
        deposits.add(deposit);
        Deposit deposit1 = new Deposit();
        deposit1.setProdId("D2");
        deposits.add(deposit1);
        depositService.createDeposit(deposits);
       Assert.assertEquals(2, depositService.findAll().size());

    }

    @Test
    public void findByProdId() {
        System.out.println(depositService.findAll());

        Assert.assertEquals(0, depositService.findAll().size());
        List<Deposit> deposits = new ArrayList<Deposit>();
        Deposit deposit = new Deposit();
        deposit.setProdId("D1");
        deposit.setProduct("Deposit");
        deposit.setRemark("test case 1");
        deposits.add(deposit);
        Deposit deposit1 = new Deposit();
        deposit1.setProdId("D2");
        deposit1.setProduct("Deposit");
        deposit1.setRemark("test case 2");
        deposits.add(deposit1);
        depositService.createDeposit(deposits);
        List<Deposit> depositList = depositService.findByProdId("D1");
        deposit = depositList.get(0);
        System.out.println(deposit);
        Assert.assertEquals("D1", deposit.getProdId());
        Assert.assertEquals("Deposit", deposit.getProduct());
        Assert.assertEquals("test case 1", deposit.getRemark());
        depositList = depositService.findByProdId("D2");
        deposit = depositList.get(0);
        System.out.println(deposit);;
        Assert.assertEquals("D2", deposit.getProdId());
        Assert.assertEquals("Deposit", deposit.getProduct());
        Assert.assertEquals("test case 2", deposit.getRemark());
    }

    @Test
    public void findAll() {
        Assert.assertEquals(0, depositService.findAll().size());
        Deposit deposit = new Deposit();
        deposit.setProdId("D1");
        depositService.createDeposit(deposit);
        Assert.assertEquals(1, depositService.findAll().size());
    }

    @Test
    public void findAllProdId() {
        System.out.println(depositService.findAll());
        Assert.assertEquals(0, depositService.findAll().size());
        List<Deposit> deposits = new ArrayList<Deposit>();
        Deposit deposit = new Deposit();
        deposit.setProdId("D1");
        deposit.setProduct("Deposit");
        deposit.setRemark("test case 1");
        deposits.add(deposit);
        Deposit deposit1 = new Deposit();
        deposit1.setProdId("D2");
        deposit1.setProduct("Deposit");
        deposit1.setRemark("test case 2");
        deposits.add(deposit1);
        depositService.createDeposit(deposits);
        List<Deposit> depositList = depositService.findAllProdId();
        deposit = depositList.get(0);
        System.out.println(deposit);
        Assert.assertEquals("D1", deposit.getProdId());
        Assert.assertNotEquals("Deposit", deposit.getProduct());
        Assert.assertNotEquals("test case 1", deposit.getRemark());
        deposit = depositList.get(1);
        System.out.println(deposit);
        Assert.assertEquals("D2", deposit.getProdId());
        Assert.assertNotEquals("Deposit", deposit.getProduct());
        Assert.assertNotEquals("test case 2", deposit.getRemark());

    }
    */

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