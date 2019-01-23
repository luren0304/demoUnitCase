package com.excelhk.openapi.demoservice.repository;

import com.excelhk.openapi.demoservice.bean.FieldMapping;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author anita
 */

@Repository
public interface FieldMappingRepository extends MongoRepository<FieldMapping,String> {

    /**
     * Retrieve all records by product and order fields and sort by order
     * @param product
     * @return
     */
    public List<FieldMapping> findAllByProductOrderByOrder(String product);

    /**
     *    Retrieve all records by product and show fields and sort by order
     * @param product
     * @param isShow
     * @return
     */
    public List<FieldMapping> findAllByProductAndShowOrderByOrder(String product, boolean isShow);
}
