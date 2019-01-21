package com.excelhk.openapi.demoservice.repository;

import com.excelhk.openapi.demoservice.bean.Loan;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends MongoRepository<Loan, String> {
    public List<Loan> findByProdId(String as_ProdId);
    @Query(value="{}", fields= "{'prodId' : 1,'_id' : 0}")
    public List<Loan> findAllprodId();



}
