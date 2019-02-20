package com.excelhk.openapi.demoservice.controller;

import com.excelhk.openapi.demoservice.bean.Deposit;
import com.excelhk.openapi.demoservice.service.DepositService;
import com.excelhk.openapi.demoservice.utils.CommonUtils;
import com.excelhk.openapi.demoservice.utils.constants.DemoConstants;
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
//
//    @Autowired
//    private DepositRepository depositRepository;

    @MockBean
    private CommonUtils commonUtils;


    @Test
    public void givenDeposit_WhenGetDeposit_thenReturnJosonArray() throws Exception {

        //System.out.println("${sftp.conn.type}");
        Deposit deposit = new Deposit();
        deposit.setProduct("Deposit");
        deposit.setProdId("D4");
        deposit.setCurrency("USD");
        deposit.setRemark("test case 1");
        deposit.setMinamount("1000");
        List<Deposit> deposits = Arrays.asList(deposit);
        System.out.println("deposits: " + deposits);
        BDDMockito.given(depositService.findByProdId("D4")).willReturn(deposits);
        BDDMockito.given(commonUtils.responseFtpError(deposit)).willReturn(deposits);
        mockMvc.perform(MockMvcRequestBuilders.get("/deposits/findone/prodid/D4")
                .contentType(MediaType.APPLICATION_JSON)
                .header("connection-type", "ftp"))
                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].minamount",Matchers.is("1000")))
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
        BDDMockito.given(depositService.findAllProdId()).willReturn(deposits);
        BDDMockito.given(commonUtils.responseFtpError(DemoConstants.PROD_TYPE_DEPOSIT,new Deposit())).willReturn(deposit);
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