package com.excelhk.openapi.demoservice;

import com.excelhk.openapi.demoservice.bean.Loan;
import com.excelhk.openapi.demoservice.repository.LoanRepository;
import com.excelhk.openapi.demoservice.service.LoanService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

@RunWith(MockitoJUnitRunner.class)
//@SpringBootTest
public class LoanServiceTest {

    //@Autowired

    @InjectMocks
    private LoanService loanService ;
    @Mock
    private LoanRepository loanRepository;

   // @Mock
    //private Loan loan;

    @Test
    public void findAllTest(){
        //assertEquals(loanService.findAll().size() +"", 2 +"");
        //Assert.assertE
        //(loanRepository.findAll()).then
        List<Loan> loans = new ArrayList<>();
        Loan loan =  new Loan();
        loan.setFee("500");
        loan.setInterestRate("7.35");
        loan.setPrdinfo1("test");
        loan.setProdId("L1");
        loan.setProduct("Loans");
        loan.setType("test");
        loan.setSubtype("subtest");
        loan.setRemark("testRemark");
        loans.add(loan);
        Loan loan1 = new Loan();
        loan1.setFee("500");
        loan1.setInterestRate("7.35");
        loan1.setPrdinfo1("test2");
        loan1.setProdId("L2");
        loan1.setProduct("Loans");
        loan1.setType("test2");
        loan1.setSubtype("subtest2");
        loan1.setRemark("testRemark2");
        loans.add(loan1);

        Mockito.when(loanRepository.findAll()).thenReturn(loans);
         Assert.assertEquals("L2",loanService.findAll().get(1).getProdId());
        Mockito.when(loanRepository.findAll()).thenReturn(new ArrayList<>());
        Assert.assertEquals(0, loanService.findAll().size());

    }

}
