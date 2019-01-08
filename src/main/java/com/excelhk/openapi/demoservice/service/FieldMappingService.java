package com.excelhk.openapi.demoservice.service;

import com.excelhk.openapi.demoservice.bean.FieldMapping;
import com.excelhk.openapi.demoservice.repository.FieldMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FieldMappingService {

    @Autowired
    private FieldMappingRepository  fieldMappingRepository;

    public List<FieldMapping> findFieldMappingByProduct(String as_Product){
      return fieldMappingRepository.findAllByProductOrderByOrder(as_Product);
    }

    public List<FieldMapping> findAllByProductAndShowOrderByOrder(String as_Product, boolean ab_show){
        return fieldMappingRepository.findAllByProductAndShowOrderByOrder(as_Product,ab_show);
    }
}
