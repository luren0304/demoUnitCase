package com.excelhk.openapi.demoservice.service;

import com.excelhk.openapi.demoservice.bean.Loan;
import com.excelhk.openapi.demoservice.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author anita
 */

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    /**
     * Create a loan record
     * @param loan
     */
    public void  createLoan(Loan loan) {
        loanRepository.save(loan);

    }

    /**
     *
     * Batch to create loan records
     * @param loanLst
     */
    public void  createLoan(List<Loan> loanLst) {
        loanRepository.saveAll(loanLst);
    }

    /**
     *
     * Retrieve record by product id
     * @param prodId
     * @return
     */
    public List<Loan> findByProdId(String prodId) {
        return loanRepository.findByProdId(prodId);
    }

    /**
     * Retrieve all product id
     * @return
     */
    public List<Loan> findAllProdId(){
        return loanRepository.findAllprodId();
    }

    /**
     *
     * Retrieve all records
     * @return
     */
    public List<Loan> findAll(){
        return loanRepository.findAll();
    }
}
