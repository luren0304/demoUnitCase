package com.excelhk.openapi.demoservice.controller;

import com.excelhk.openapi.demoservice.bean.Deposit;
import com.excelhk.openapi.demoservice.service.DepositService;
import com.excelhk.openapi.demoservice.utils.CommonUtils;
import com.excelhk.openapi.demoservice.utils.constants.DemoConstants;
import org.hamcrest.Matchers;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.*;

@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = DemoApplication.class)
@WebFluxTest(DepositController.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DepositControllerWebMvcTest {

    @Autowired
    private WebTestClient webClient ;

    @MockBean
    private DepositService depositService;
    @MockBean
    private CommonUtils commonUtils;

    /*@Test
    public void givenDeposit_WhenGetDeposit_thenReturnJosonArray(){
        Deposit deposit = new Deposit();
        deposit.setProduct("Deposit");
        deposit.setProdId("D9");
        deposit.setCurrency("USD");
        deposit.setRemark("test case 9");
        deposit.setMinamount("1000");
        List<Deposit> deposits = Arrays.asList(deposit);
        System.out.println("deposits: " + deposits);
        Deposit deposit1 = new Deposit();
        deposit1.setProdId("D9");
        deposit1.setProduct(DemoConstants.PROD_TYPE_DEPOSIT);
        //
        BDDMockito.given(depositService.findByProdId("D9")).willReturn(deposits);
        BDDMockito.given(commonUtils.responseByFtp(deposit1)).willReturn(deposits);

        EntityExchangeResult<String> result = this.webClient.get().uri("/deposits/findone/prodid/D9")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class)
                .returnResult();

        System.out.println(result);
        this.webClient.get().uri("/deposits/findone/prodid/D9")
                .exchange()
                .expectStatus()
                .isOk().expectBody().jsonPath("$[0].prodId",deposit.getProdId());
        this.webClient.get().uri("/deposits/findone/prodid/D9")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Deposit.class)
                .hasSize(1)
                .isEqualTo(deposits);

        EntityExchangeResult<String> result1= this.webClient.get()
                .uri("/deposits/findone/prodid/D9")
                .header("connection-type", "ftp")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class)
                .returnResult();
        System.out.println(result);
        //Mockito.when()

    }*/

    @Test
    public void whenDeposit_thenReturnDetailOkWithConnectTypeFtp(){
        Deposit deposit = new Deposit();
        deposit.setProduct("Deposit");
        deposit.setProdId("D10");
        deposit.setCurrency("EUR");
        deposit.setRemark("test case 10");
        deposit.setMinamount("1000.25");
        List<Deposit> deposits = Arrays.asList(deposit);
        System.out.println("deposits: " + deposits);
        Deposit deposit1 = new Deposit();
        deposit1.setProdId("D10");
        deposit1.setProduct(DemoConstants.PROD_TYPE_DEPOSIT);
        Mockito.when(commonUtils.responseByFtp(deposit1)).thenReturn(deposits);

        //Testing the return min amount value
        this.webClient.get().uri("/deposits/findone/prodid/D10")
                .header("connection-type", "ftp")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$[0].minamount")
                .isEqualTo(deposit.getMinamount()).returnResult();
                //.jsonPath("$[0].minamount",deposit.getMinamount());


        //Testing the return all data
        this.webClient.get().uri("/deposits/findone/prodid/D10")
                .header("connection-type", "ftp")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList (Deposit.class)
                .hasSize(1)
                .isEqualTo(deposits);

        //print the response result
        System.out.println(this.webClient.get().uri("/deposits/findone/prodid/D10")
                .header("connection-type", "ftp")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList (Deposit.class).returnResult());

    }

    @Test
    public void whenDeposit_thenReturnDetailOkWithConnectTypeDB(){
        Deposit deposit = new Deposit();
        deposit.setProduct("Deposit");
        deposit.setProdId("D11");
        deposit.setCurrency("EUR");
        deposit.setRemark("test case 11");
        deposit.setMinamount("1000.25");
        List<Deposit> deposits = Arrays.asList(deposit);
        System.out.println("deposits: " + deposits);
        Mockito.when(depositService.findByProdId("D11")).thenReturn(deposits);

        //Testing the return min amount value
        this.webClient.get().uri("/deposits/findone/prodid/D11")
                .header("connection-type", "db")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$[0].remark",deposit.getRemark());

        //Testing the return all data
        this.webClient.get().uri("/deposits/findone/prodid/D11")
                .header("connection-type", "db")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList (Deposit.class)
                .hasSize(1)
                .isEqualTo(deposits);

        //print the response result
        System.out.println(this.webClient.get().uri("/deposits/findone/prodid/D11")
                .header("connection-type", "db")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList (Deposit.class).returnResult());

    }
    @Test
    public void whenDeposit_thenReturnDetailOkWithoutConnectType(){
        Deposit deposit = new Deposit();
        deposit.setProduct("Deposit");
        deposit.setProdId("D12");
        deposit.setCurrency("EUR");
        deposit.setRemark("test case 12");
        deposit.setMinamount("1000.25");
        List<Deposit> deposits = Arrays.asList(deposit);
        System.out.println("deposits: " + deposits);
        Mockito.when(depositService.findByProdId("D12")).thenReturn(deposits);

        //Testing the return min amount value
        this.webClient.get().uri("/deposits/findone/prodid/D12")
                //.header("connection-type", "db")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$[0].remark",deposit.getRemark());

        //Testing the return all data
        this.webClient.get().uri("/deposits/findone/prodid/D12")
                //.header("connection-type", "db")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList (Deposit.class)
                .hasSize(1)
                .isEqualTo(deposits);

        //print the response result
        System.out.println(this.webClient.get().uri("/deposits/findone/prodid/D12")
                //.header("connection-type", "db")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList (Deposit.class).returnResult());

    }

    @Test
    public void whenDeposit_thenReturnDetailErrorWithConnectTypeFtp(){
        Map<String, String> map = new HashMap<String, String>(1);
        map.put("error","ftp Connection refused");
        List<ResponseEntity> deposits = Arrays.asList(new ResponseEntity(map, HttpStatus.INTERNAL_SERVER_ERROR));
        Deposit deposit1 = new Deposit();
        deposit1.setProdId("D10");
        deposit1.setProduct(DemoConstants.PROD_TYPE_DEPOSIT);
        Mockito.when(commonUtils.responseByFtp(deposit1)).thenReturn(deposits);
        //print the response result
        System.out.println(this.webClient.get().uri("/deposits/findone/prodid/D10")
                .header("connection-type", "ftp")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class).returnResult());

    }

    @Test
    public void whenDeposit_thenReturnProdOkWithConnectTypeFtp(){
        Deposit deposit = new Deposit();
        deposit.setProdId("D1");
        Deposit deposit1 = new Deposit();
        deposit1.setProdId("D2");
        List<Deposit> deposits = new ArrayList<>();
        deposits.add(deposit);
        deposits.add(deposit1);
        Mockito.when(commonUtils.responseByFtp(DemoConstants.PROD_TYPE_DEPOSIT, new Deposit())).thenReturn(deposits);

        //Testing the return min amount value
        this.webClient.get().uri("/deposits/findProd")
                .header("connection-type", "ftp")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$[0].prodId",deposit.getProdId());
        //Testing the return all data
        this.webClient.get().uri("/deposits/findProd")
                .header("connection-type", "ftp")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList (Deposit.class)
                .hasSize(2)
                .isEqualTo(deposits);


        //print the response result
        System.out.println(this.webClient.get().uri("/deposits/findProd")
                .header("connection-type", "ftp")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList (Deposit.class).returnResult());

    }

    @Test
    public void whenDeposit_thenReturnProdOkWithConnectTypeDB(){
        Deposit deposit = new Deposit();
        deposit.setProdId("D3");
        Deposit deposit1 = new Deposit();
        deposit1.setProdId("D4");
        List<Deposit> deposits = new ArrayList<>();
        deposits.add(deposit);
        deposits.add(deposit1);
        Mockito.when(depositService.findAllProdId()).thenReturn(deposits);

        //Testing the return min amount value
        this.webClient.get().uri("/deposits/findProd")
                .header("connection-type", "db")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$[1].prodId",deposit1.getProdId());

        //Testing the return all data
        this.webClient.get().uri("/deposits/findProd")
                .header("connection-type", "db")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList (Deposit.class)
                .hasSize(2)
                .isEqualTo(deposits);

        //print the response result
        System.out.println(this.webClient.get().uri("/deposits/findProd")
                .header("connection-type", "db")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList (Deposit.class).returnResult());

    }
    @Test
    public void whenDeposit_thenReturnProdOkWithoutConnectType(){
        Deposit deposit = new Deposit();
        deposit.setProdId("D5");
        Deposit deposit1 = new Deposit();
        deposit1.setProdId("D6");
        List<Deposit> deposits = new ArrayList<>();
        deposits.add(deposit);
        deposits.add(deposit1);
        Mockito.when(depositService.findAllProdId()).thenReturn(deposits);

        //Testing the return min amount value
        this.webClient.get().uri("/deposits/findProd")
                //.header("connection-type", "db")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$[0].prodId",deposit.getProdId());

        //Testing the return all data
        this.webClient.get().uri("/deposits/findProd")
                //.header("connection-type", "db")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList (Deposit.class)
                .hasSize(2)
                .isEqualTo(deposits);

        //print the response result
        System.out.println(this.webClient.get().uri("/deposits/findProd")
                //.header("connection-type", "db")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList (Deposit.class).returnResult());

    }

    @Test
    public void whenDeposit_thenReturnProdErrorWithConnectTypeFtp(){
        Map<String, String> map = new HashMap<String, String>(1);
        map.put("error","ftp Connection refused");
        List<ResponseEntity> deposits = Arrays.asList(new ResponseEntity(map, HttpStatus.INTERNAL_SERVER_ERROR));
        Mockito.when(commonUtils.responseByFtp(DemoConstants.PROD_TYPE_DEPOSIT, new Deposit())).thenReturn(deposits);
        //print the response result
        System.out.println(this.webClient.get().uri("/deposits/findProd")
                .header("connection-type", "ftp")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class).returnResult());

    }

    @Test
    public void whenDeposits_thenReturnWithoutRecord() throws Exception {
        List<Deposit> deposits = new ArrayList<>();

        // Get production detail
        Deposit deposit = new Deposit();
        deposit.setProdId("L1");
        deposit.setProduct(DemoConstants.PROD_TYPE_LOANS);

        BDDMockito.given(commonUtils.responseByFtp(deposit)).willReturn(deposits);
        BDDMockito.given(depositService.findByProdId("L1")).willReturn(deposits);

        // Connection type = ftp
        this.webClient.get().uri("/deposits/findone/prodid/L1")
                .header("connection-type", "ftp")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$", Matchers.hasSize(0));


        // Connection type = db
        this.webClient.get().uri("/deposits/findone/prodid/L1")
                .header("connection-type", "db")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$", Matchers.hasSize(0));

        // no Connection type
        this.webClient.get().uri("/deposits/findone/prodid/L1")
                //.header("connection-type", "db")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$", Matchers.hasSize(0));


        // Get production id
        BDDMockito.given(commonUtils.responseByFtp(DemoConstants.PROD_TYPE_LOANS, new Deposit())).willReturn(deposits);
        BDDMockito.given(depositService.findAllProdId()).willReturn(deposits);
        // Connection type = ftp
        this.webClient.get().uri("/deposits/findProd")
                .header("connection-type", "ftp")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$", Matchers.hasSize(0));

        // Connection type = db
        this.webClient.get().uri("/deposits/findProd")
                .header("connection-type", "db")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$", Matchers.hasSize(0));

        // no Connection type
        this.webClient.get().uri("/deposits/findProd")
                //.header("connection-type", "db")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody().jsonPath("$", Matchers.hasSize(0));
    }
}