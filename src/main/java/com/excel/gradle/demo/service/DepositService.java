package com.excel.gradle.demo.service;

import com.excel.gradle.demo.bean.Deposit;
import com.excel.gradle.demo.repository.DepositRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Field;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

//import org.springframework.data.mongodb.core.query.BasicQuery;
//import org.springframework.data.mongodb.core.query.Criteria;

@Service
public class DepositService {

    @Autowired
    private DepositRepository depositRepository;
    private MongoTemplate mongo;

    public void  createDeposit(Deposit deposit) {
        depositRepository.save(deposit);
    }
    public void  createDeposit(List<Deposit> depositLst) {
        depositRepository.saveAll(depositLst);
    }

    public List<Deposit> findByProdId(String as_ProdId) {
        return depositRepository.findByProdId(as_ProdId);
    }

    public List<Deposit> findAll(){
        return depositRepository.findAll();
    }

    public List<Deposit> findAllProdId(){
        return depositRepository.findAllprodId();
    }

    public List<Deposit> findfields(){
//		Criteria criteria = new Criteria();
//		Query query = new Query(criteria);
        Query query = new Query();
        Field fields = query.fields();
        fields.include("prodId");
//		List <Deposit>
        return mongo.find(query, Deposit.class);
    }


}
