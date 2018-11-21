package com.excel.gradle.demo.service;

import com.excel.gradle.demo.bean.Loan;
import com.excel.gradle.demo.repository.LoanRepository;
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
