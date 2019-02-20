package com.excelhk.openapi.demoservice.service;

import com.excelhk.openapi.demoservice.bean.FieldMapping;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FieldMappingServiceTest {

    @Autowired
    FieldMappingService fieldMappingService;

    @Test
    public void findFieldMappingByProduct() {
        List<FieldMapping> fieldMappings = fieldMappingService.findFieldMappingByProduct("Deposits");
        Assert.assertEquals(9, fieldMappings.size());
        Assert.assertEquals("prodId", fieldMappings.get(0).getField());
    }

    @Test
    public void findAllByProductAndShowOrderByOrder() {
        List<FieldMapping> fieldMappings = fieldMappingService.findAllByProductAndShowOrderByOrder("Loans", false);
        Assert.assertEquals(0, fieldMappings.size());
        fieldMappings = fieldMappingService.findAllByProductAndShowOrderByOrder("Loans", true);
        Assert.assertEquals(10, fieldMappings.size());
    }
}