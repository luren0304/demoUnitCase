package com.excelhk.openapi.demoservice.controller;

import com.excelhk.openapi.demoservice.bean.Deposit;
import com.excelhk.openapi.demoservice.service.DepositService;
import com.excelhk.openapi.demoservice.utils.CommonUtils;
import com.excelhk.openapi.demoservice.utils.constants.DemoConstants;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(DepositController.class)
public class DepositControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepositService depositService;

    @MockBean
    private CommonUtils commonUtils;


    @Test
    public void givenDeposit_WhenGetDeposit_thenReturnJosonArray() throws Exception {

        //System.out.println("${sftp.conn.type}");
        Deposit deposit = new Deposit();
        deposit.setProduct("Deposit");
        deposit.setProdId("D9");
        deposit.setCurrency("USD");
        deposit.setRemark("test case 9");
        deposit.setMinamount("1000");
        List<Deposit> deposits = Arrays.asList(deposit);
        System.out.println("deposits: " + deposits);
        List depositsFtp = Arrays.asList(deposit);

        Deposit deposit1 = new Deposit();
        deposit1.setProdId("D9");
        deposit1.setProduct(DemoConstants.PROD_TYPE_DEPOSIT);

        BDDMockito.given(depositService.findByProdId("D9")).willReturn(deposits);
        BDDMockito.given(commonUtils.responseByFtp(deposit1)).willReturn(depositsFtp);

        mockMvc.perform(MockMvcRequestBuilders.get("/deposits/findone/prodid/D9")
                .contentType(MediaType.APPLICATION_JSON)
                .header("connection-type", "ftp"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].minamount", Matchers.is("1000")))
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(MockMvcRequestBuilders.get("/deposits/findone/prodid/D9")
                .contentType(MediaType.APPLICATION_JSON)
                .header("connection-type", "db"))
                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].minamount", Matchers.is("1000")))
                .andDo(MockMvcResultHandlers.print());



    }

    @Test
    public void findAllProd() throws Exception{
        Deposit deposit = new Deposit();
        deposit.setProduct("Deposit");
        deposit.setProdId("D5");
        deposit.setCurrency("USD");
        deposit.setRemark("test case 2");
        deposit.setMinamount("1000.05");
        //depositRepository.save(deposit);
        List<Deposit> deposits = Arrays.asList(deposit);
//        BDDMockito.given(depositService.findAllProdId()).willReturn(deposits);
//        BDDMockito.given(commonUtils.responseByFtp(DemoConstants.PROD_TYPE_DEPOSIT,new Deposit())).willReturn(deposits);
        mockMvc.perform( MockMvcRequestBuilders.get("/deposits/findProd")
                .contentType(MediaType.APPLICATION_JSON)
                .header("connection-type", "ftp"))
                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].minamount",Matchers.is("1000.05")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
}