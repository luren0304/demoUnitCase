package com.excelhk.openapi.demoservice.controller;

import com.excelhk.openapi.demoservice.bean.Loan;
import com.excelhk.openapi.demoservice.service.LoanService;
import com.excelhk.openapi.demoservice.utils.CommonUtils;
import com.excelhk.openapi.demoservice.utils.constants.DemoConstants;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.*;

@RunWith(SpringRunner.class)
@WebMvcTest(LoanController.class)
public class LoanControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanService loanService;

    @MockBean
    private CommonUtils commonUtils;

    @Test
    public void whenLoans_thenReturnDetailWithConnectTypeDB() throws Exception {
        Loan loan = new Loan();
        loan.setProdId("L1");
        loan.setProduct("Loans");
        loan.setRemark("test loan case 1");
        List<Loan> loans = Arrays.asList(loan);
        BDDMockito.given(loanService.findByProdId("L1")).willReturn(loans);
        mockMvc.perform(MockMvcRequestBuilders.get("/loans/findone/prodid/L1")
                .header("connection-type", "db"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].remark",Matchers.is(loan.getRemark())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].prodId", Matchers.is(loan.getProdId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].product", Matchers.is(loan.getProduct())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void whenLoans_thenReturnDetailWithConnectTypeFtp() throws Exception {
        Loan loan = new Loan();
        loan.setProdId("L2");
        loan.setProduct("Loans");
        loan.setRemark("test loan case 2");
        List<Loan> loans = Arrays.asList(loan);
        Loan loan1 = new Loan();
        loan1.setProdId("L2");
        loan1.setProduct(DemoConstants.PROD_TYPE_LOANS);
        BDDMockito.given(commonUtils.responseByFtp(loan1)).willReturn(loans);
        mockMvc.perform(MockMvcRequestBuilders.get("/loans/findone/prodid/L2")
                .header("connection-type", "ftp"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].remark",Matchers.is(loan.getRemark())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].prodId", Matchers.is(loan.getProdId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].product", Matchers.is(loan.getProduct())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void whenLoans_thenReturnDetailWithoutConnectType() throws Exception {
        Loan loan = new Loan();
        loan.setProdId("L3");
        loan.setProduct("Loans");
        loan.setRemark("test loan case 3");
        List<Loan> loans = Arrays.asList(loan);
        Loan loan1 = new Loan();
        loan1.setProdId("L3");
        loan1.setProduct(DemoConstants.PROD_TYPE_LOANS);
        BDDMockito.given(loanService.findByProdId("L3")).willReturn(loans);
        mockMvc.perform(MockMvcRequestBuilders.get("/loans/findone/prodid/L3")
                //.header("connection-type", "ftp")
                 )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].remark",Matchers.is(loan.getRemark())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].prodId", Matchers.is(loan.getProdId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].product", Matchers.is(loan.getProduct())))
                .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void whenDeposit_thenReturnDetailErrorWithConnectTypeFtp() throws Exception {
        Map<String, String> map = new HashMap<String, String>(1);
        map.put("error","ftp Connection refused");
        List<ResponseEntity> loans = Arrays.asList(new ResponseEntity(map, HttpStatus.INTERNAL_SERVER_ERROR));
        Loan loan1 = new Loan();
        loan1.setProdId("L4");
        loan1.setProduct(DemoConstants.PROD_TYPE_LOANS);
        BDDMockito.given(commonUtils.responseByFtp(loan1)).willReturn(loans);
        mockMvc.perform(MockMvcRequestBuilders.get("/loans/findone/prodid/L4")
                .header("connection-type", "ftp")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].body.error",Matchers.is(map.get("error"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].statusCode", Matchers.is("INTERNAL_SERVER_ERROR")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].statusCodeValue", Matchers.is(500)))
                .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void whenLoans_thenReturnProdWithConnectTypeDB() throws Exception {
        Loan loan = new Loan();
        loan.setProdId("L1");
        Loan loan1 = new Loan();
        loan1.setProdId("L2");
        List<Loan> loans = Arrays.asList(loan, loan1);
        System.out.println(loans);
        BDDMockito.given(loanService.findAllProdId()).willReturn(loans);
        mockMvc.perform(MockMvcRequestBuilders.get("/loans/findProd")
                .header("connection-type", "db"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].prodId",Matchers.is(loan.getProdId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].prodId", Matchers.is(loan1.getProdId())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void whenLoans_thenReturnProdWithConnectTypeFtp() throws Exception {
        Loan loan = new Loan();
        loan.setProdId("L1");
        Loan loan1 = new Loan();
        loan1.setProdId("L2");
        List<Loan> loans = Arrays.asList(loan, loan1);
        System.out.println(loans);
        BDDMockito.given(commonUtils.responseByFtp(DemoConstants.PROD_TYPE_LOANS, new Loan())).willReturn(loans);
        mockMvc.perform(MockMvcRequestBuilders.get("/loans/findProd")
                .header("connection-type", "ftp"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].prodId",Matchers.is(loan.getProdId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].prodId", Matchers.is(loan1.getProdId())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void whenLoans_thenReturnProdWithoutConnectType() throws Exception {
        Loan loan = new Loan();
        loan.setProdId("L3");
        Loan loan1 = new Loan();
        loan1.setProdId("L4");
        List<Loan> loans = Arrays.asList(loan, loan1);
        BDDMockito.given(loanService.findAllProdId()).willReturn(loans);
        mockMvc.perform(MockMvcRequestBuilders.get("/loans/findProd")
                //.header("connection-type", "ftp")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].prodId",Matchers.is(loan.getProdId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].prodId", Matchers.is(loan1.getProdId())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void whenLoans_thenReturnProdErrorWithConnectTypeFtp() throws Exception {
        Map<String, String> map = new HashMap<String, String>(1);
        map.put("error","ftp Connection refused");
        List<ResponseEntity> loans = Arrays.asList(new ResponseEntity(map, HttpStatus.INTERNAL_SERVER_ERROR));
        BDDMockito.given(commonUtils.responseByFtp(DemoConstants.PROD_TYPE_LOANS, new Loan())).willReturn(loans);
        mockMvc.perform(MockMvcRequestBuilders.get("/loans/findProd")
                .header("connection-type", "ftp")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].body.error",Matchers.is(map.get("error"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].statusCode", Matchers.is("INTERNAL_SERVER_ERROR")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].statusCodeValue", Matchers.is(500)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void whenLoans_thenReturnWithoutRecord() throws Exception {
        List<Loan> loans = new ArrayList<>();

        // Get production detail
        Loan loan = new Loan();
        loan.setProdId("L1");
        loan.setProduct(DemoConstants.PROD_TYPE_LOANS);

        BDDMockito.given(commonUtils.responseByFtp(loan)).willReturn(loans);
        BDDMockito.given(loanService.findByProdId("L1")).willReturn(loans);

        // Connection type = ftp
        mockMvc.perform(MockMvcRequestBuilders.get("/loans/findone/prodid/L1")
                .header("connection-type", "ftp")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)))
                .andDo(MockMvcResultHandlers.print());

        // Connection type = db
        mockMvc.perform(MockMvcRequestBuilders.get("/loans/findone/prodid/L1")
                .header("connection-type", "db")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)))
                .andDo(MockMvcResultHandlers.print());

        // no Connection type
        mockMvc.perform(MockMvcRequestBuilders.get("/loans/findone/prodid/L1")
                //.header("connection-type", "db")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)))
                .andDo(MockMvcResultHandlers.print());


        // Get production id
        BDDMockito.given(commonUtils.responseByFtp(DemoConstants.PROD_TYPE_LOANS, new Loan())).willReturn(loans);
        BDDMockito.given(loanService.findAllProdId()).willReturn(loans);
        // Connection type = ftp
        mockMvc.perform(MockMvcRequestBuilders.get("/loans/findProd")
                .header("connection-type", "ftp")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)))
                .andDo(MockMvcResultHandlers.print());

        // Connection type = db
        mockMvc.perform(MockMvcRequestBuilders.get("/loans/findProd")
                .header("connection-type", "db")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)))
                .andDo(MockMvcResultHandlers.print());

        // no Connection type
        mockMvc.perform(MockMvcRequestBuilders.get("/loans/findProd")
                //.header("connection-type", "db")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)))
                .andDo(MockMvcResultHandlers.print());
    }
}