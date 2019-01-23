package com.excelhk.openapi.demoservice.service;

import com.excelhk.openapi.demoservice.bean.FieldMapping;
import com.excelhk.openapi.demoservice.repository.FieldMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author anita
 *
 */

@Service
public class FieldMappingService {

    @Autowired
    private FieldMappingRepository  fieldMappingRepository;

    /**
     *
     *  Retrieve field mapping record by product
     * @param product
     * @return
     */

    public List<FieldMapping> findFieldMappingByProduct(String product){
      return fieldMappingRepository.findAllByProductOrderByOrder(product);
    }

    /**
     * Retrieve field mapping record by product and show
     * @param product
     * @param isShow
     * @return
     */
    public List<FieldMapping> findAllByProductAndShowOrderByOrder(String product, boolean isShow){
        return fieldMappingRepository.findAllByProductAndShowOrderByOrder(product,isShow);
    }
}
