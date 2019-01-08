package com.excelhk.openapi.demoservice.service;

import com.excelhk.openapi.demoservice.bean.Loan;
import com.excelhk.openapi.demoservice.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;
    public void  createLoan(Loan Loan) {
        loanRepository.save(Loan);

    }
    public void  createLoan(List<Loan> LoanLst) {
        loanRepository.saveAll(LoanLst);
    }

    public List<Loan> findByProdId(String as_ProdId) {
        return loanRepository.findByProdId(as_ProdId);
    }
    public List<Loan> findAllProdId(){
        return loanRepository.findAllprodId();
    }
    public List<Loan> findAll(){
        return loanRepository.findAll();
    }
}
