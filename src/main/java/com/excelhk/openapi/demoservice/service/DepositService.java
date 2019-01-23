package com.excelhk.openapi.demoservice.service;

import com.excelhk.openapi.demoservice.bean.Deposit;
import com.excelhk.openapi.demoservice.repository.DepositRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author anita
 *
 */

@Service
public class DepositService {

    @Autowired
    private DepositRepository depositRepository;

    /**
     *  Create Deposit record
     *
     * @param deposit
     */
    public void  createDeposit(Deposit deposit) {
        depositRepository.save(deposit);
    }

    /**
     * Batch to create Deposit records
     *
     * @param depositLst
     */
    public void  createDeposit(List<Deposit> depositLst) {
        depositRepository.saveAll(depositLst);
    }

    /**
     *
     * Retrieve record details by product id
     * @param prodId
     * @return
     */
    public List<Deposit> findByProdId(String prodId) {
        return depositRepository.findByProdId(prodId);
    }

    /**
     *  Retrieve all records
     * @return
     */
    public List<Deposit> findAll(){
        return depositRepository.findAll();
    }

    /**
     * Retrieve all product id
     *
     * @return
     */
    public List<Deposit> findAllProdId(){
        return depositRepository.findAllprodId();
    }
}
