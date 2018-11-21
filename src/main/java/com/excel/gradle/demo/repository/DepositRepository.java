package com.excel.gradle.demo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.excel.gradle.demo.bean.Deposit;

@Repository
public interface DepositRepository extends MongoRepository<Deposit, String> {

    public List<Deposit> findByProdId(String as_ProdId);

    @Query(value="{}", fields= "{'prodId' : 1,'_id' : 0}")
    public List<Deposit> findAllprodId();

}
