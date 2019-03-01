package com.excelhk.openapi.demoservice.utils;

import com.excelhk.openapi.demoservice.bean.Deposit;
import com.excelhk.openapi.demoservice.bean.Loan;
import com.excelhk.openapi.demoservice.utils.constants.DemoConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = {"sftp.port=10022"})
public class CommonUtilsUnitTest {

    @Autowired
    private CommonUtils commonUtils;

    @Test
    public void testGenerateFileWithBothString() {
//        CommonUtils commonUtils = new CommonUtils();
//        assertEquals(false,commonUtils.generateFile("test","test123"));
        assertEquals(true, commonUtils.generateFile("test", "test123"));
    }

    @Test
    public void testGenerateFileWithStrObj() {
        Loan loan = new Loan();
        loan.setProduct(DemoConstants.PROD_TYPE_LOANS);
        loan.setProdId("L1");
        assertEquals(true, commonUtils.generateFile("testOjb", loan));

        Deposit deposit = new Deposit();
        deposit.setProduct(DemoConstants.PROD_TYPE_DEPOSIT);
        deposit.setProdId("D2");
        assertEquals(true,commonUtils.generateFile("testDeposit", deposit));



    }

    @Test
    public void testGetProdsByFile() {
        Loan loan = new Loan();
        loan.setProduct(DemoConstants.PROD_TYPE_LOANS);
        String fileName = "test.loans";
        //List list = Arrays.asList(loan);
        List list =  new ArrayList();
        // no found file
       commonUtils.getProdsByFile(list,fileName,loan );
       assertEquals(0, list.size());
       // file exists
        fileName = "test.Loan";
        commonUtils.getProdsByFile(list, fileName, loan);

        assertEquals(4,list.size());

        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }

        for (Object aList: list
             ) {
            System.out.println(aList);
        }
        IntStream.range(0, list.size()).mapToObj((IntFunction<Object>) list::get).forEach(System.out::println);
        assertEquals("L1", ((Loan)list.get(0)).getProdId());
        assertEquals("L2", ((Loan)list.get(1)).getProdId());
        assertEquals("L3", ((Loan)list.get(2)).getProdId());
        assertEquals("L4", ((Loan)list.get(3)).getProdId());
    }

    @Test
    public void testGetDepositDetailByFile() {
        Deposit deposit = new Deposit();
        deposit.setProdId("D1");
        deposit.setProduct(DemoConstants.PROD_TYPE_DEPOSIT);
        List list = new ArrayList();
        String fileName = "test.deposits";
        commonUtils.getDepositDetailByFile(deposit,fileName,list);
        assertEquals(0,list.size());
        fileName = "test.deposit";
        commonUtils.getDepositDetailByFile(deposit,fileName,list);
        assertEquals(1,list.size());
        assertEquals("D1",((Deposit)list.get(0)).getProdId());
        assertEquals(DemoConstants.PROD_TYPE_DEPOSIT,((Deposit)list.get(0)).getProduct());
        assertEquals("Statement Savings Account",((Deposit)list.get(0)).getType());
        assertEquals("Saving", ((Deposit)list.get(0)).getSubtype());
        assertEquals("HKD",((Deposit)list.get(0)).getCurrency());
        assertEquals("2.00",((Deposit)list.get(0)).getInterestRate());
        assertEquals("5000",((Deposit)list.get(0)).getMinamount());
        assertEquals("50", ((Deposit)list.get(0)).getFee());
        assertEquals("No monthly service fee if you maintain a balance of more than HKD5,000 (rolling average of previous 3 months, if not you'll pay a fee of HKD50)",((Deposit)list.get(0)).getRemark());

    }

    @Test
    public void testGetLoanDetailByFile() {
        Loan loan = new Loan();
        loan.setProdId("L1");
        loan.setProduct(DemoConstants.PROD_TYPE_DEPOSIT);
        List list = new ArrayList();
        String fileName = "test.loans";
        commonUtils.getLoanDetailByFile(loan,fileName,list);
        assertEquals(0,list.size());
        fileName = "test.loan.L1";
        commonUtils.getLoanDetailByFile(loan,fileName,list);
        assertEquals(1,list.size());
        assertEquals("L1",((Loan)list.get(0)).getProdId());
        assertEquals(DemoConstants.PROD_TYPE_LOANS,((Loan)list.get(0)).getProduct());
        assertEquals("Premier Mastercard",((Loan)list.get(0)).getType());
        assertEquals("Mastercard",((Loan)list.get(0)).getSubtype());
        assertEquals("18.9% APR",((Loan)list.get(0)).getInterestRate());
        assertEquals("Extra RewardCash: - Enjoy up to 6X RewardCash for spending in Rewards of Your Choice category. Rebate rate at 2.4% ",
                ((Loan)list.get(0)).getPrdinfo1());
        assertEquals("Dining: - Buy 1 get 1 freeBuy 1 get 1 free This link will open in a new window dining privileges at Michelin Restaurants",
                ((Loan)list.get(0)).getPrdinfo2());
        assertEquals("Travel: - Stay 2 get 2 nights free on Hotel bookings - Earn 15 British Airways Avios / KrisFlyer Miles with just 1 RewardCash",
                ((Loan)list.get(0)).getPrdinfo3());
        assertEquals("$200 P.A.", ((Loan)list.get(0)).getFee());
        assertNull(((Loan)list.get(0)).getRemark());
        System.out.println("Remark: " + ((Loan)list.get(0)).getRemark());
//        assertEquals("", ((Loan)list.get(0)).getRemark());

    }
}
