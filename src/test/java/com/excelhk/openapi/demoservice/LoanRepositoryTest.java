package com.excelhk.openapi.demoservice;

import com.excelhk.openapi.demoservice.bean.Loan;
import com.excelhk.openapi.demoservice.repository.LoanRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@DataMongoTest
public class LoanRepositoryTest {

    @Autowired
    private LoanRepository loanRepository;
    private static Loan loan;
    static {
        loan = new Loan();
        loan.setProdId("test1");
        loan.setProduct("test");
        loan.setType("testType");
        loan.setSubtype("testSub");
        loan.setPrdinfo1("test product information 1");
        loan.setRemark("test case");
    }

    @Test
    public void testCreat(){
        loanRepository.save(loan);
        System.out.println(loanRepository.findAll());
       // assertEquals(loan);
        Assert.assertEquals("test1",loan.getProdId());

    }

    @Test
    public void testFindByProdId(){
        loanRepository.save(loan);

        System.out.println("testFindByProdId: loan.getProdId() " + loan.getProdId());
        List<Loan> loans = loanRepository.findByProdId(loan.getProdId());
        if(loans != null && loans.size() > 0){
            System.out.println("testFindByProdId: loans.size() " + loans.size());
            for (Loan loan1: loans
                 ) {
                System.out.println(loan1);
                Assert.assertEquals("test1",loan.getProdId());
            }
        }
    }

    @Test
    public void testFindAllprodId(){
        List<Loan> loanList = loanRepository.findAllprodId();

        Loan loan2 = new Loan();
        loan2.setProdId("L1");
        Loan loan3 = new Loan();
        loan3.setProdId("L2");

        if(loanList != null && loanList.size() > 0){
            System.out.println("testFindAllprodId: loanList.size() " + loanList.size());
            Assert.assertEquals(2,loanList.size());
            Assert.assertEquals("L1", loanList.get(0).getProdId());
            Assert.assertEquals("L2", loanList.get(1).getProdId());
        }

    }
}
