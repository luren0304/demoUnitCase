package com.excelhk.openapi.demoservice.repository;

import com.excelhk.openapi.demoservice.bean.FieldMapping;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FieldMappingRepository extends MongoRepository<FieldMapping,String> {

    public List<FieldMapping> findAllByProductOrderByOrder(String as_product);

    public List<FieldMapping> findAllByProductAndShowOrderByOrder(String as_Product, boolean ab_isShow);
}
